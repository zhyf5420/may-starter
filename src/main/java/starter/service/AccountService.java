package starter.service;

import com.github.wenhao.jpa.Specifications;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import starter.base.constants.ResponseCode;
import starter.base.constants.Role;
import starter.base.dto.PageRequest;
import starter.base.dto.PageResponse;
import starter.base.exception.BusinessException;
import starter.base.utils.*;
import starter.base.utils.cache.ICache;
import starter.repository.AccountRepository;
import starter.dto.account.AccountPageRequest;
import starter.dto.account.AccountPasswordRequest;
import starter.dto.account.AccountRequest;
import starter.dto.account.AccountResponse;
import starter.dto.auth.LoginResponse;
import starter.entity.Account;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * 账户
 *
 * @author zhyf
 */
@Slf4j
@Service
public class AccountService {

    @Resource
    private ICache cache;

    @Resource
    private AuthService authService;

    @Resource
    private AccountRepository accountRepository;

    private void convert2Entity(AccountRequest requestBean, Account account) {
        ObjectUtil.copyNotNullBean(requestBean, account);
    }

    private AccountResponse convert2Response(Account account) {
        return ObjectUtil.getCopyBean(account, new AccountResponse());
    }

    /**
     * 添加
     */
    @Transactional
    public Long save(AccountRequest requestBean) {
        if (requestBean.getAccountName() == null) {
            requestBean.setAccountName(UUID.randomUUID().toString());
        }

        long count = accountRepository.countByAccountName(requestBean.getAccountName());
        if (count > 0) {
            log.error("新增系统用户失败，用户已存在 , {}", requestBean.getAccountName());
            throw new BusinessException(ResponseCode.SYSTEM_DATA_EXIST, "新增系统用户失败,用户名已存在");
        }

        Account account = new Account();
        convert2Entity(requestBean, account);
        account.setPassword(PasswordUtil.encode(requestBean.getPassword()));
        account = accountRepository.save(account);
        return account.getId();
    }

    /**
     * 批量删除
     */
    @Transactional
    public void deleteByIds(List<Long> ids) {
        ids.forEach(this::deleteById);
    }

    /**
     * id删除
     */
    @Transactional
    public void deleteById(Long id) {
        Account account = accountRepository.getAndCheck(id);

        authService.delAuthInfo(account.getToken());
        account.setIsDelete(true)
               .setToken(null)
               .setAccountName(account.getAccountName() + "_" + UUID.randomUUID());
        accountRepository.save(account);
    }

    /**
     * 修改
     */
    @Transactional
    public Long update(Long id, AccountRequest requestBean) {
        requestBean.setAccountName(null).setPassword(null);
        Account account = accountRepository.getAndCheck(id);

        boolean flag = false;
        if (!Objects.equals(requestBean.getUserName(), account.getUserName())
                || !Objects.equals(requestBean.getPhoneNum(), account.getPhoneNum())) {
            flag = true;
        }

        convert2Entity(requestBean, account);
        account = accountRepository.save(account);

        if (flag) {
            String token = account.getToken();
            if (StringUtil.isNotEmpty(token)) {
                LoginResponse o = (LoginResponse) cache.get(token);
                o.setUserName(requestBean.getUserName()).setPhoneNum(requestBean.getPhoneNum());
                cache.set(token, o);
            }
        }

        return account.getId();
    }

    /**
     * 修改密码
     */
    @Transactional
    public Long modifyPassword(long id, AccountPasswordRequest requestBean) {
        Account account = accountRepository.getAndCheck(id);
        if (!PasswordUtil.checkPassword(requestBean.getOldPassword(), account.getPassword())) {
            log.error("修改密码失败,原密码错误");
            throw new BusinessException(ResponseCode.SYSTEM_DATA_EXIST, "原密码错误");
        } else if (requestBean.getOldPassword().equals(requestBean.getNewPassword())) {
            log.error("修改密码失败,新密码跟原密码重复");
            throw new BusinessException(ResponseCode.SYSTEM_DATA_EXIST, "新密码跟原密码重复");
        }

        account.setPassword(PasswordUtil.encode(requestBean.getNewPassword()));
        accountRepository.save(account);
        return id;
    }

    public void initPassword(long id) {
        LoginResponse authInfo = authService.getAuthInfo();
        if (authInfo == null || authInfo.getRole() != Role.admin) {
            throw new BusinessException(ResponseCode.NO_PERMISSION);
        }

        Account account = accountRepository.getAndCheck(id);
        account.setPassword(PasswordUtil.encode(Md5Util.getMd5("123456")));
        accountRepository.save(account);
    }

    /**
     * id查询
     */
    public AccountResponse findById(Long id) {
        Account account = accountRepository.getAndCheck(id);
        return convert2Response(account);
    }

    /**
     * 分页查询
     */
    public PageResponse<AccountResponse> getPage(AccountPageRequest requestBean) {
        // 1、构建分页请求
        Pageable pageable = PageRequest.of(requestBean);

        // 2、构建查询条件
        Specification<Account> specification = Specifications.<Account>and()
                .eq("isDelete", false)
                .eq(ObjectUtil.isNotNull(requestBean.getRole()), "role", requestBean.getRole())
                .like(ObjectUtil.isNotNull(requestBean.getAccountName()), "accountName", "%" + requestBean.getAccountName() + "%")
                .like(ObjectUtil.isNotNull(requestBean.getUserName()), "userName", "%" + requestBean.getUserName() + "%")
                .like(ObjectUtil.isNotNull(requestBean.getPhoneNum()), "phoneNum", "%" + requestBean.getPhoneNum() + "%")
                .ge(ObjectUtil.isNotNull(requestBean.getCreateTimeStart()), "createTime", DateUtil.getDateStart(requestBean.getCreateTimeStart()))
                .le(ObjectUtil.isNotNull(requestBean.getCreateTimeEnd()), "createTime", DateUtil.getDateEnd(requestBean.getCreateTimeEnd()))
                .predicate(StringUtil.isNotEmpty(requestBean.getCondition()),
                        Specifications.or()
                                      .like("accountName", "%" + requestBean.getAccountName() + "%")
                                      .like("userName", "%" + requestBean.getCondition() + "%")
                                      .like("phoneNum", "%" + requestBean.getCondition() + "%")
                                      .build())
                .build();

        // 3、查询结果集
        Page<AccountResponse> responsePage = accountRepository.findAll(specification, pageable)
                                                              .map(this::convert2Response);
        return PageResponse.of(responsePage);
    }

}

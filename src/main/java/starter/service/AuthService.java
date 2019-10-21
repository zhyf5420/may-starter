package starter.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import starter.base.constants.ResponseCode;
import starter.base.exception.BusinessException;
import starter.base.utils.ObjectUtil;
import starter.base.utils.PasswordUtil;
import starter.base.utils.StringUtil;
import starter.base.utils.WebContextUtil;
import starter.base.utils.cache.ICache;
import starter.repository.AccountRepository;
import starter.dto.auth.LoginRequest;
import starter.dto.auth.LoginResponse;
import starter.entity.Account;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;

/**
 * create on 2019/3/11 0011
 *
 * @author zhyf
 */
@Slf4j
@Component
public class AuthService {

    @Resource
    private ICache cache;

    @Resource
    private AccountRepository accountRepository;

    /**
     * 登入
     */
    @Transactional
    public LoginResponse webLogin(@Valid @RequestBody LoginRequest requestBean) {
        Account account = accountRepository.findByAccountName(requestBean.getAccountName());
        if (account == null) {
            throw new BusinessException(ResponseCode.SYSTEM_DATA_NOT_FOUND, "登录失败，用户名或密码错误");
        } else if (!PasswordUtil.checkPassword(requestBean.getPassword(), account.getPassword())) {
            throw new BusinessException(ResponseCode.SYSTEM_DATA_NOT_FOUND, "登录失败，用户名或密码错误");
        }

        if (account.getIsDelete()) {
            throw new BusinessException(ResponseCode.USER_FORBIDDEN);
        }

        LoginResponse authInfo = getAuthInfoByToken(account.getToken());
        if (authInfo != null) {
            //已经登录，先注销
            delAuthInfo(account.getToken());
            account.setToken(null);
        }

        LoginResponse loginResponse = ObjectUtil.getCopyBean(account, new LoginResponse());
        String token = UUID.randomUUID().toString() + "-w";
        loginResponse.setToken(token);

        account.setToken(token);
        accountRepository.save(account);

        cache.set(token, loginResponse);

        return loginResponse;
    }

    /**
     * 登出
     */
    @Transactional
    public void logout() {
        LoginResponse authInfo = getAuthInfo();
        if (authInfo == null) {
            throw new BusinessException(ResponseCode.SYSTEM_NOT_LOGIN);
        }

        Account account = accountRepository.getAndCheck(authInfo.getId());
        if (authInfo.getToken().endsWith("-w")) {
            account.setToken(null);
        } else {
            throw new BusinessException(ResponseCode.SYSTEM_NOT_LOGIN, "token 错误");
        }
        accountRepository.save(account);

        delAuthInfo(authInfo.getToken());
    }

    /**
     * 删除当前用户的登陆缓存
     */
    public void delAuthInfo() {
        String token = getToken();
        cache.deleteKey(token);
    }

    /**
     * 删除当前用户的登陆缓存
     */
    public void delAuthInfo(String token) {
        if (token == null) {
            return;
        }
        cache.deleteKey(token);
    }

    /**
     * 获取当前用户信息
     */
    public LoginResponse getAuthInfo() {
        String token = getToken();
        return getAuthInfoByToken(token);
    }

    /**
     * 获取当前用户信息--根据token
     */
    private LoginResponse getAuthInfoByToken(String token) {
        if (token == null) {
            log.error("token==null");
            return null;
        }
        LoginResponse response = (LoginResponse) cache.get(token);
        if (response == null) {
            log.error("token获取用户信息失败 token={}", token);
        }
        return response;
    }

    /**
     * 获取当前用户token
     */
    private String getToken() {
        HttpServletRequest servletRequest = WebContextUtil.getHttpServletRequest();
        String token = servletRequest.getHeader("Auth");
        log.info("header Auth token={}", token);
        if (StringUtil.isEmpty(token)) {
            log.error("header获取 token=={}", token);
            throw new BusinessException(ResponseCode.SYSTEM_NOT_LOGIN);
        }
        return token;
    }

    /**
     * 校验当前用户是否登陆
     */
    public Boolean checkAuth() {
        String token = getToken();
        boolean exists = cache.exists(token);
        if (!exists) {
            log.error("token 不存在{}", token);
        }

        return exists;
    }

}

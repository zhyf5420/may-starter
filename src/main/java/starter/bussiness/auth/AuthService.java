package starter.bussiness.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import starter.base.RedisCachedService;
import starter.base.constants.ResponseCode;
import starter.base.exception.BusinessException;
import starter.base.utils.ObjectUtil;
import starter.base.utils.PasswordUtil;
import starter.base.utils.StringUtil;
import starter.base.utils.WebContextUtil;
import starter.bussiness.auth.dto.LoginRequest;
import starter.bussiness.auth.dto.LoginResponse;
import starter.dao.AccountRepository;
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

    @Resource(name = "redisCachedService")
    private RedisCachedService redisService;

    @Resource
    private AccountRepository accountRepository;

    public Boolean checkAuth() {
        String token = getToken();
        boolean exists = redisService.exists(token);
        if (!exists) {
            log.error("token 不存在{}", token);
        }

        return exists;
    }

    public String getToken() {
        HttpServletRequest servletRequest = WebContextUtil.getHttpServletRequest();
        String token = servletRequest.getHeader("Auth");
        log.info("header Auth token={}", token);
        if (StringUtil.isEmpty(token)) {
            log.error("header获取 token=={}", token);
            throw new BusinessException(ResponseCode.SYSTEM_NOT_LOGIN);
        }
        return token;
    }

    public void delAuthInfo() {
        String token = getToken();
        redisService.deleteKey(token);
    }

    public Account getAuthAccount() {
        String token = getToken();
        LoginResponse authInfo = getAuthInfo(token);
        if (authInfo == null) {
            throw new BusinessException(ResponseCode.SYSTEM_NOT_LOGIN);
        }
        return accountRepository.getAndCheck(authInfo.getId());
    }

    public LoginResponse getAuthInfo(String token) {
        if (token == null) {
            log.error("token==null");
            return null;
        }
        LoginResponse response = (LoginResponse) redisService.get(token);
        if (response == null) {
            log.error("token获取用户信息失败 token={}", token);
        }
        return response;
    }

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

    public LoginResponse getAuthInfo() {
        String token = getToken();
        return getAuthInfo(token);
    }

    public void delAuthInfo(String token) {
        if (token == null) {
            return;
        }
        redisService.deleteKey(token);
    }

    @Transactional
    public LoginResponse webLogin(@Valid @RequestBody LoginRequest requestBean) {
        Account account = getAndCheckAccount(requestBean);
        if (account.getIsDelete()) {
            throw new BusinessException(ResponseCode.USER_FORBIDDEN);
        }

        LoginResponse authInfo = getAuthInfo(account.getToken());
        if (authInfo != null) {
            //已经登录，先注销
            delAuthInfo(account.getToken());
            account.setToken(null);
        }

        LoginResponse loginResponse = ObjectUtil.getCopyBean(account, new LoginResponse());
        String token = UUID.randomUUID().toString() + "-w";
        loginResponse.setToken(token);

        setAuthInfo(token, loginResponse);

        account.setToken(token);
        accountRepository.save(account);

        return loginResponse;
    }

    private Account getAndCheckAccount(LoginRequest requestBean) {
        Account account = accountRepository.findByAccountName(requestBean.getAccountName());
        if (account == null) {
            throw new BusinessException(ResponseCode.SYSTEM_DATA_NOT_FOUND, "登录失败，用户名或密码错误");
        } else if (!PasswordUtil.checkPassword(requestBean.getPassword(), account.getPassword())) {
            throw new BusinessException(ResponseCode.SYSTEM_DATA_NOT_FOUND, "登录失败，用户名或密码错误");
        }
        return account;
    }

    public void setAuthInfo(String token, LoginResponse loginResponse) {
        if (token == null) {
            throw new BusinessException(ResponseCode.SYSTEM_DATA_ERROR, "token错误");
        }
        //token有效期
        redisService.set(token, loginResponse);
    }

}

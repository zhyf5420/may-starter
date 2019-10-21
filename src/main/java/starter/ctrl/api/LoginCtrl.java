package starter.ctrl.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import starter.base.constants.ResponseCode;
import starter.base.dto.ResponseEntity;
import starter.base.exception.BusinessException;
import starter.base.utils.acs.ACS;
import starter.dto.auth.LoginRequest;
import starter.dto.auth.LoginResponse;
import starter.service.AuthService;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 登录权限管理
 *
 * @author zhyf
 */
@RestController
@Api(produces = "application/json", tags = "登录权限管理")
public class LoginCtrl {

    @Resource
    private AuthService authService;

    @ACS(allowAnonymous = true)
    @ApiOperation(value = "web登录")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> webLogin(@Valid @RequestBody LoginRequest requestBean) {
        LoginResponse loginResponse = authService.webLogin(requestBean);
        return ResponseEntity.ok(loginResponse);
    }

    @ACS
    @ApiOperation(value = "注销")
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        authService.logout();
        return ResponseEntity.ok();
    }

    @ACS
    @ApiOperation(value = "获取当前用户信息")
    @GetMapping("/auth-info")
    public ResponseEntity<LoginResponse> getAuthInfo() {
        LoginResponse loginResponse = authService.getAuthInfo();
        if (loginResponse == null) {
            throw new BusinessException(ResponseCode.SYSTEM_NOT_LOGIN);
        }
        return ResponseEntity.ok(loginResponse);
    }

}

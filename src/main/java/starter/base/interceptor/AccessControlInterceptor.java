package starter.base.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import starter.base.constants.ResponseCode;
import starter.base.exception.BusinessException;
import starter.base.utils.CollectionUtil;
import starter.base.utils.acs.ACS;
import starter.bussiness.service.AuthService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 访问控制拦截器.
 *
 * @author zhyf
 */
@Component
public class AccessControlInterceptor extends HandlerInterceptorAdapter {

    private static final List<String> NO_LOGIN_RESOURCES = CollectionUtil.newArrayList(
            // swagger相关资源不需要登录
            "/label-starter/swagger-ui.html",
            "/label-starter/configuration",
            "/label-starter/swagger-resources",
            "/label-starter/api-docs",
            "/label-starter/v2/api-docs"
    );

    @Resource
    private AuthService authService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 不需要进行访问控制的资源过滤
        String uri = request.getRequestURI();
        for (String resource : NO_LOGIN_RESOURCES) {
            if (uri.startsWith(resource)) {
                return true;
            }
        }

        //return hasAccessPermission(handler, request);
        return true;
    }

    /**
     * 是否有权限访问
     */
    private boolean hasAccessPermission(Object handler, HttpServletRequest request) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            ACS acs = handlerMethod.getMethodAnnotation(ACS.class);
            // 没有 ACS 注解的默认不可以访问
            if (acs != null) {
                if (acs.allowAnonymous()) {
                    return true;
                }

                // 2、判断用户是否登录，测试时不用登陆
                Boolean checkAuth = authService.checkAuth();
                if (!checkAuth) {
                    throw new BusinessException(ResponseCode.SYSTEM_NOT_LOGIN);
                }
                return true;
            }

            return false;
        }

        throw new BusinessException(ResponseCode.NO_PERMISSION);
    }

}

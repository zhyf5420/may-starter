package starter.base.aop;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import starter.base.dto.IOperatorInfo;
import starter.bussiness.auth.AuthService;
import starter.bussiness.auth.dto.LoginResponse;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static starter.base.utils.StringUtil.getNewLine;

/**
 * 请求日志打印
 *
 * @author zhyf
 */
@Aspect
@Order(1)
@Component
@Slf4j
public class RequestLogAop {

    @Resource
    private AuthService authService;

    @Around("execution(* starter..*Ctrl.*(..))")
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        if (ra == null) {
            return null;
        }

        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        requestLog(joinPoint, request);
        return getResponseLog(joinPoint);
    }

    /**
     * 请求Log
     */
    private void requestLog(ProceedingJoinPoint joinPoint, HttpServletRequest request) {
        String newLine = getNewLine();
        log.info("[Request]: " + newLine +
                "URL: " + request.getRequestURL().toString() + " " + request.getMethod() + newLine +
                "CLASS_METHOD: " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
    }

    /**
     * 响应Log
     */
    private Object getResponseLog(ProceedingJoinPoint joinPoint) throws Throwable {
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof IOperatorInfo) {
                LoginResponse authInfo = authService.getAuthInfo();
                if (authInfo != null) {
                    ((IOperatorInfo) arg).setOperator(authInfo.getUserName());
                    ((IOperatorInfo) arg).setOperatorId(authInfo.getId());
                    log.info("注入当前用户信息成功");
                } else {
                    ((IOperatorInfo) arg).setOperator("");
                    ((IOperatorInfo) arg).setOperatorId(-1L);
                    log.error("注入当前用户信息失败");
                }
            }
        }

        Object result = joinPoint.proceed();
        if (result == null) {
            return null;
        }

        String newLine = getNewLine();
        StringBuilder content = new StringBuilder();
        content.append("[Response Data]: ").append(newLine);
        if (result instanceof String) {
            content.append(result);
        } else {
            content.append(JSON.toJSONString(result));
        }
        log.info(content.toString());
        return result;
    }

}

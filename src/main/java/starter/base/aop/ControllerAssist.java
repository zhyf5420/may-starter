package starter.base.aop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import starter.base.constants.ResponseCode;
import starter.base.dto.ResponseEntity;
import starter.base.exception.BusinessException;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;


/**
 * 异常响应处理。将异常包装为固定的格式并返回。
 *
 * @author zhyf
 */
@Slf4j
@ControllerAdvice
public class ControllerAssist {

    /**
     * 注册全局数据编辑器，若传递的数据为空字串 转成 null
     *
     * @param binder 数据绑定
     */
    @InitBinder
    public void registerCustomEditors(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<?> handleBusinessException(BusinessException exception, HttpServletRequest request) {
        log.error("", exception);
        return ResponseEntity.error(exception.getCode(), exception.getClass().getCanonicalName(),
                request.getRequestURI(), exception.getMessage());
    }

    /**
     * 处理请求参数验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        log.error("", exception);
        ResponseCode error = ResponseCode.SYSTEM_REQUEST_PARAM_ERROR;
        String message = error.getLabel();
        BindingResult bindingResult = exception.getBindingResult();
        if (bindingResult != null && bindingResult.hasErrors()) {
            List<ObjectError> objectErrorList = bindingResult.getAllErrors();
            if (!objectErrorList.isEmpty()) {
                message = objectErrorList.get(0).getDefaultMessage();
            }
        }
        return ResponseEntity.error(error, exception.getClass().getCanonicalName(), request.getRequestURI(), message);
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody
    public ResponseEntity<?> handleSQLDataException(HttpServletRequest request, Exception exception, Locale locale) {
        log.error("", exception);
        return ResponseEntity.error(ResponseCode.SYSTEM_DATA_ERROR, exception.getClass().getCanonicalName(), request.getRequestURI());
    }

    /**
     * 处理服务器端数据访问错误
     *
     * @param request   请求对象
     * @param exception 异常对象
     * @param locale    地理信息
     */
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({SQLException.class, DataAccessException.class, DataAccessResourceFailureException.class})
    @ResponseBody
    public ResponseEntity<?> handleSQLException(HttpServletRequest request, Exception exception, Locale locale) {
        log.error("", exception);
        return ResponseEntity.error(ResponseCode.SYSTEM_ERROR, exception.getClass().getCanonicalName(), request.getRequestURI());
    }

    /**
     * 处理服务器端RuntimeException
     *
     * @param request   请求对象
     * @param exception 异常对象
     */
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({RuntimeException.class})
    @ResponseBody
    public ResponseEntity<?> handleAllException(HttpServletRequest request, Exception exception) {
        log.error("", exception);
        return ResponseEntity.error(ResponseCode.SYSTEM_ERROR, exception.getClass().getCanonicalName(), request.getRequestURI(), exception.getMessage());
    }

}

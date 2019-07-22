package starter.base.exception;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import starter.base.constants.ResponseCode;

/**
 * 业务异常，处理业务抛出的异常 错误码和错误码说明，需要指定
 */
@Getter
@Setter
@NoArgsConstructor
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = -4310721413425427596L;

    /**
     * 错误码
     */
    private int code = ResponseCode.SYSTEM_ERROR.getCode();

    /**
     * 错误码的说明
     */
    private String label = ResponseCode.SYSTEM_ERROR.getLabel();

    public BusinessException(int responseCode, String codeLabel) {
        super(codeLabel);
        setCode(responseCode);
        setLabel(codeLabel);
    }

    public BusinessException(int responseCode, String codeLabel, String message) {
        super(message);
        setCode(responseCode);
        setLabel(codeLabel);
    }

    public BusinessException(int responseCode, String codeLabel, String message, Throwable cause) {
        super(message, cause);
        setCode(responseCode);
        setLabel(codeLabel);
    }

    public BusinessException(ResponseCode responseCode) {
        super(responseCode.getLabel());
        setCode(responseCode.getCode());
        setLabel(responseCode.getLabel());
    }

    public BusinessException(ResponseCode responseCode, String message) {
        super(message);
        setCode(responseCode.getCode());
        setLabel(responseCode.getLabel());
    }

    public BusinessException(ResponseCode responseCode, String message, Throwable cause) {
        super(message, cause);
        setCode(responseCode.getCode());
        setLabel(responseCode.getLabel());
    }

}

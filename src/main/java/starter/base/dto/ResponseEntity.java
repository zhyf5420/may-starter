package starter.base.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import starter.base.constants.ResponseCode;

import java.io.Serializable;


/**
 * API请求的返回结果 正常、错误响应实体
 */
@ApiModel
@Getter
@Setter
public class ResponseEntity<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "返回数据信息显示")
    private String message;

    @ApiModelProperty(value = "时间戳")
    private long timestamp = System.currentTimeMillis();

    @ApiModelProperty(value = "异常信息")
    private String exception;

    @ApiModelProperty(value = "返回数据编码，0成功 其他为失败")
    private int ecode = 0;

    @ApiModelProperty(value = "网络状态码-暂时不重要")
    private int httpStatus = 200;

    @ApiModelProperty(value = "返回请求路径")
    private String path;

    @ApiModelProperty(value = "返回请求数据{json}对象")
    private T data;

    public static <T> ResponseEntity<T> ok(T data) {
        ResponseEntity<T> responseEntity = new ResponseEntity<>();
        responseEntity.setData(data);
        responseEntity.setResponseCode(ResponseCode.SUCCESS);
        return responseEntity;
    }

    public static ResponseEntity<String> ok() {
        ResponseEntity<String> responseEntity = new ResponseEntity<>();
        responseEntity.setData("操作成功");
        responseEntity.setResponseCode(ResponseCode.SUCCESS);
        return responseEntity;
    }

    public static <T> ResponseEntity<T> error(ResponseCode responseCode) {
        ResponseEntity<T> responseEntity = new ResponseEntity<>();
        responseEntity.setData(null);
        responseEntity.setResponseCode(responseCode);
        return responseEntity;
    }

    public static <T> ResponseEntity<T> error(ResponseCode responseCode, String message) {
        ResponseEntity<T> responseEntity = new ResponseEntity<>();
        responseEntity.setData(null);
        responseEntity.setResponseCode(responseCode);
        responseEntity.setMessage(message);
        return responseEntity;
    }

    public static <T> ResponseEntity<T> errorData(ResponseCode responseCode, T data) {
        ResponseEntity<T> responseEntity = new ResponseEntity<>();
        responseEntity.setData(data);
        responseEntity.setResponseCode(responseCode);
        return responseEntity;
    }

    public static <T> ResponseEntity<T> error(ResponseCode responseCode, String exception, String path) {
        ResponseEntity<T> responseEntity = new ResponseEntity<>();
        responseEntity.setData(null);
        responseEntity.setResponseCode(responseCode);
        responseEntity.setException(exception);
        responseEntity.setPath(path);
        return responseEntity;
    }

    public static <T> ResponseEntity<T> error(ResponseCode responseCode, String exception, String path, String message) {
        ResponseEntity<T> responseEntity = new ResponseEntity<>();
        responseEntity.setData(null);
        responseEntity.setResponseCode(responseCode);
        responseEntity.setException(exception);
        responseEntity.setPath(path);
        responseEntity.setMessage(message);
        return responseEntity;
    }

    public static <T> ResponseEntity<T> error(int errorCode) {
        ResponseEntity<T> responseEntity = new ResponseEntity<>();
        ResponseCode responseCode = ResponseCode.findByCode(errorCode);
        responseEntity.setData(null);
        responseEntity.setResponseCode(responseCode);
        return responseEntity;
    }

    public static <T> ResponseEntity<T> error(int errorCode, String exception, String path) {
        ResponseCode responseCode = ResponseCode.findByCode(errorCode);
        ResponseEntity<T> responseEntity = new ResponseEntity<>();
        responseEntity.setData(null);
        responseEntity.setResponseCode(responseCode);
        responseEntity.setException(exception);
        responseEntity.setPath(path);
        return responseEntity;
    }

    public static <T> ResponseEntity<T> error(int errorCode, String exception, String path, String message) {
        ResponseCode responseCode = ResponseCode.findByCode(errorCode);
        ResponseEntity<T> responseEntity = new ResponseEntity<>();
        responseEntity.setData(null);
        responseEntity.setResponseCode(responseCode);
        responseEntity.setException(exception);
        responseEntity.setPath(path);
        responseEntity.setMessage(message);
        return responseEntity;
    }

    public static <T> ResponseEntity<T> error(ResponseEntity res) {
        ResponseCode responseCode = ResponseCode.findByCode(res.getEcode());
        ResponseEntity<T> responseEntity = new ResponseEntity<>();
        responseEntity.setData(null);
        responseEntity.setResponseCode(responseCode);
        responseEntity.setException(res.getException());
        responseEntity.setPath(res.getPath());
        responseEntity.setMessage(res.getMessage());
        return responseEntity;
    }

    private void setResponseCode(ResponseCode responseCode) {
        this.ecode = responseCode.getCode();
        this.message = responseCode.getLabel();
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "ResponseEntity{" + "message='" + message + '\'' +
                ", timestamp=" + timestamp +
                ", exception='" + exception + '\'' +
                ", ecode=" + ecode +
                ", httpStatus=" + httpStatus +
                ", path='" + path + '\'' +
                ", data=" + data +
                '}';
    }

}

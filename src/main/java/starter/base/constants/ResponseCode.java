package starter.base.constants;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created on 2018/12/19 0019.
 *
 * @author zhyf
 */
public enum ResponseCode {

    // 0-200 系统级
    SUCCESS(0, "操作成功"),
    SYSTEM_ERROR(1, "系统错误"),
    // 自定义错误，可以修改label
    SYSTEM_CUSTOM_ERROR(2, "自定义错误"),
    SYSTEM_DATA_ERROR(3, "数据异常"),
    SYSTEM_DATA_NOT_FOUND(4, "数据不存在"),
    SYSTEM_DATA_EXIST(5, "数据已存在"),
    SYSTEM_NOT_LOGIN(6, "请登录"),
    SYSTEM_UPDATE_ERROR(7, "数据更新失败"),
    SYSTEM_REQUEST_PARAM_ERROR(8, "请求参数错误"),
    SYSTEM_HYSTRIX_ERROR(9, "服务器忙,请稍后再试"),
    FAIL(10, "操作失败"),
    // 201-400，用户模块
    NO_PERMISSION(201, "无此权限"),
    USER_LOGIN_ERROR(202, "用户名或密码错误"),
    USER_REGISTER_CODE_ERROR(203, "验证码错误"),
    USER_REGISTER_CODE_LOSE_ERROR(204, "验证码已失效"),
    USER_UPDATE_PASSWORT_UNLIKENESS_EROOR(205, "原密码错误"),
    USER_UPDATE_PASSWORT_LIKENESS_ERROR(206, "新密码跟原密码重复，请重试"),
    USER_NOT_BIND(207, "请绑定账户"),
    USER_WX_AUTH_FAIL(208, "用户认证失败"),
    USER_WX_USER_INFO(209, "获取用户信息失败"),
    USER_FORBIDDEN(210, "账户无法使用"),
    ;

    private static final Map<Integer, ResponseCode> CODE_INDEX = Maps.newHashMapWithExpectedSize(ResponseCode.values().length);

    static {
        for (ResponseCode responseCode : ResponseCode.values()) {
            CODE_INDEX.put(responseCode.code, responseCode);
        }
    }

    private int code;

    private String label;

    ResponseCode(int code, String label) {
        this.code = code;
        this.label = label;
    }

    /**
     * 根据状态码获取枚举
     *
     * @param code 状态码
     *
     * @return ResponseCode
     */
    public static ResponseCode findByCode(int code) {
        return CODE_INDEX.get(code);
    }

    /**
     * 获取状态码描述
     *
     * @return 状态码描述，如果没有返回空串
     */
    public String getLabel() {
        return this.label;
    }

    public int getCode() {
        return this.code;
    }
}

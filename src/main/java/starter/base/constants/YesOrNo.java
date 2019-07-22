package starter.base.constants;


import com.fasterxml.jackson.annotation.JsonValue;
import starter.base.exception.BusinessException;

/**
 * 是否
 *
 * @author zhyf
 */
public enum YesOrNo {

    // 0
    NO("否"),
    // 1
    YES("是");

    private String value;

    YesOrNo(String value) {
        this.value = value;
    }

    /**
     * 按名字检索
     */
    public static YesOrNo getMatchByName(String name) {
        for (YesOrNo flag : values()) {
            if (flag.value.equalsIgnoreCase(name)) {
                return flag;
            }
        }
        throw new BusinessException(ResponseCode.SYSTEM_DATA_ERROR, "Enum类型错误，找不到对应的枚举：" + name);
    }

    /**
     * 按顺序检索
     */
    public static YesOrNo getMatchByOrdinal(Integer ordinal) {
        if (ordinal == null) {
            return null;
        }

        for (YesOrNo flag : values()) {
            if (flag.getKey().equals(ordinal)) {
                return flag;
            }
        }
        throw new BusinessException(ResponseCode.SYSTEM_DATA_ERROR, "Enum类型错误，找不到对应的枚举：" + ordinal);
    }

    public static YesOrNo toEnum(Boolean bool) {
        if (null == bool) {
            return null;
        }
        return bool ? YES : NO;
    }

    @JsonValue
    public Integer getKey() {
        return this.ordinal();
    }

    public String getValue() {
        return value;
    }

    /**
     * 转成boolean
     */
    public boolean toBoolean() {
        return this == YES;
    }
}
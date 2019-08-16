package starter.dto.account;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import starter.base.constants.Role;
import starter.base.dto.BasePageRequest;

import java.util.Date;

/**
 * 账户  分页查询请求
 *
 * @author zhyf
 */
@ApiModel
@Getter
@Setter
@Accessors(chain = true)
public class AccountPageRequest extends BasePageRequest {

    private static final long serialVersionUID = 7054503693623743757L;

    @ApiModelProperty(value = "账号")
    private String accountName;

    @ApiModelProperty(value = "姓名")
    private String userName;

    @ApiModelProperty(value = "手机号码")
    private String phoneNum;

    @ApiModelProperty("多合一查询 账号、姓名、手机号码")
    private String condition;

    @ApiModelProperty("创建时间 开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTimeStart;

    @ApiModelProperty("创建时间 结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTimeEnd;

    @ApiModelProperty(value = "角色")
    private Role role;

}
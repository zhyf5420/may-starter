package starter.bussiness.account.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import starter.base.constants.Role;
import starter.base.dto.BaseResponse;

/**
 * 账户
 *
 * @author zhyf
 */
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AccountResponse extends BaseResponse {

    @ApiModelProperty(value = "账号")
    private String accountName;

    @ApiModelProperty(value = "姓名")
    private String userName;

    @ApiModelProperty(value = "手机号码")
    private String phoneNum;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "角色")
    private Role role;

}
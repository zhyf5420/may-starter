package starter.bussiness.account.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import starter.base.constants.Role;

/**
 * 账户
 *
 * @author zhyf
 */
@ApiModel
@Data
@Accessors(chain = true)
public class AccountRequest {

    @ApiModelProperty(value = "账号")
    private String accountName;

    @ApiModelProperty(value = "密码 仅添加时有效")
    private String password;

    @ApiModelProperty(value = "姓名")
    private String userName;

    @ApiModelProperty(value = "手机号码")
    private String phoneNum;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "角色")
    private Role role;

}
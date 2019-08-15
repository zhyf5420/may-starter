package starter.bussiness.dto.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import starter.base.constants.Role;

/**
 * create on 2019/3/11 0011
 *
 * @author zhyf
 */
@ApiModel
@Data
@Accessors(chain = true)
public class LoginResponse {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "账号")
    private String accountName;

    @ApiModelProperty(value = "姓名")
    private String userName;

    @ApiModelProperty(value = "手机号码")
    private String phoneNum;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "token")
    private String token;

    @ApiModelProperty(value = "角色")
    private Role role;

}
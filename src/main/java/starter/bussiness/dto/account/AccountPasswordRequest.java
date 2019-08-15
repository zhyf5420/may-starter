package starter.bussiness.dto.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 账户
 *
 * @author zhyf
 */
@ApiModel
@Data
@Accessors(chain = true)
public class AccountPasswordRequest {

    @ApiModelProperty(value = "原密码")
    private String oldPassword;

    @ApiModelProperty(value = "新密码")
    private String newPassword;

}
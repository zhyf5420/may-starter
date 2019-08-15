package starter.bussiness.dto.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * create on 2019/3/11 0011
 *
 * @author zhyf
 */
@Data
@ApiModel
public class LoginRequest {

    @NotBlank(message = "登录名不能为空!")
    @ApiModelProperty(value = "用户名", required = true)
    private String accountName;

    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "密码", required = true, example = "e10adc3949ba59abbe56e057f20f883e")
    private String password;

}
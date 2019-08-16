package starter.dto.account;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import starter.base.constants.Role;
import starter.base.dto.BaseResponse;
import starter.base.utils.RandomUtil;

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

    public static AccountResponse getInstance() {
        AccountResponse response = new AccountResponse();
        response.setAccountName(RandomUtil.randomString(5));
        response.setUserName(RandomUtil.randomString(5));
        response.setPhoneNum(RandomUtil.randomString(12));
        response.setRemark(RandomUtil.randomString(10));
        response.setId((long) RandomUtil.randomInt(10000));
        response.setCreateTime(new Date());
        response.setUpdateTime(new Date());

        return response;
    }
}
package starter.bussiness.ctrl_rest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotBlank;
import starter.base.dto.BasePageRequest;

/**
 * Created on 2019/8/14.
 *
 * @author zhyf
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
@Accessors(chain = true)
public class DemoRequest extends BasePageRequest {

    @ApiModelProperty(value = "userName")
    private String userName;

}
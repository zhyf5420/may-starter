package starter.base.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Created on 2019/5/30.
 *
 * @author zhyf
 */
@Data
@ApiModel
@Accessors(chain = true)
public class IdListRequest {

    @ApiModelProperty(value = "id列表")
    private List<Long> idList;

}
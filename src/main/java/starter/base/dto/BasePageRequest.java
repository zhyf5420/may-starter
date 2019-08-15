package starter.base.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.Min;

/**
 * @author zhyf
 */
@ApiModel
@Getter
@Setter
@Accessors(chain = true)
public class BasePageRequest {

    private static final long serialVersionUID = 903382500971091968L;

    @Min(value = 1, message = "每页显示条数必须大于等于1")
    @ApiModelProperty(value = "每页多少条记录，默认为10", example = "10")
    private Integer size = 10;

    @Min(value = 0, message = "当前页必须大于等于0")
    @ApiModelProperty(value = "当前页码，从0开始，默认为0", example = "0")
    private Integer page = 0;

    @ApiModelProperty(value = "排序字段非必填，默认创建时间", example = "id")
    private String sort = "id";

    @ApiModelProperty(value = "排序顺序 asc desc，默认降序")
    private Sort.Direction order = Sort.Direction.DESC;

}

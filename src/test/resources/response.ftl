package ${pkg};

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.EqualsAndHashCode;
import starter.base.dto.BaseResponse;
import java.util.Date;
/**
* ${module}
*
* @author ${author}
* @date ${date}
*/
@Data
@ApiModel
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ${modelNameUpperCamel}Response extends BaseResponse {

<#list fields as item>
    @ApiModelProperty(value = "${item.value}")
    private ${item.field};

</#list>
}
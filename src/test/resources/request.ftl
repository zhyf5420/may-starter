package ${pkg};

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.util.Date;
import starter.base.dto.IOperatorInfo;

/**
* ${module}
*
* @author ${author}
* @date ${date}
*/
@Data
@ApiModel
@Accessors(chain = true)
public class ${modelNameUpperCamel}Request implements IOperatorInfo{

<#list fields as item>
    @ApiModelProperty(value = "${item.value}")
    private ${item.field};
</#list>

@ApiModelProperty(value = "操作人id", hidden = true)
private Long operatorId;

@ApiModelProperty(value = "操作人", hidden = true)
private String operator;

}
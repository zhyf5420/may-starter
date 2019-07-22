package ${pkg};

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import starter.base.dto.BasePageRequest;
import java.util.Date;

/**
* ${module}  分页查询请求
*
* @author ${author}
* @date ${date}
*/
@Getter
@Setter
@ApiModel
@Accessors(chain = true)
public class ${modelNameUpperCamel}PageRequest extends BasePageRequest {

private static final long serialVersionUID = 7054503693623743757L;

}
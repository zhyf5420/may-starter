package starter.bussiness.dto.file;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

/**
 * Created on 2018/9/28 0028.
 *
 * @author zhyf
 */
@Data
@ApiModel
@Accessors(chain = true)
public class ExcelRequest {

    @ApiModelProperty("list map 文档内容 优先使用")
    private List<Map<String, String>> mapContent;

    @ApiModelProperty("list list 文档内容 mapContent为空时使用")
    private List<List<String>> listContent;

    @ApiModelProperty("表头")
    private String headName;

}

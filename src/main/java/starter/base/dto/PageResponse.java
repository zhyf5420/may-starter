package starter.base.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhyf
 */
@ApiModel
@Data
@NoArgsConstructor
public class PageResponse<T> implements Serializable {

    private static final long serialVersionUID = 6887389993060457824L;

    @ApiModelProperty(value = "返回数据内容")
    private List<T> content = new ArrayList<>();

    @ApiModelProperty(value = "总条目数")
    private long totalElements;

    @ApiModelProperty(value = "总页数")
    private int totalPages;

    @ApiModelProperty(value = "是否是最后一页")
    private boolean last;

    @ApiModelProperty(value = "当前页码（从0开始）")
    private int number;

    @ApiModelProperty(value = "每页的条目数")
    private int size;

    @ApiModelProperty(value = "当前页的实际条目数")
    private int numberOfElements;

    @ApiModelProperty(value = "是否是第一页")
    private boolean first;

    public PageResponse(Page<T> page) {
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.first = page.isFirst();
        this.last = page.isLast();
        this.size = page.getSize();
        this.number = page.getNumber();
        this.numberOfElements = page.getNumberOfElements();
        this.content = page.getContent();
    }

    public static <T> PageResponse<T> of(Page<T> page) {
        return new PageResponse<>(page);
    }

}

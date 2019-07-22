package starter.base.dao;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;


/**
 * Created on 2019/5/15.
 *
 * @author zhyf
 */
@MappedSuperclass
@Data
@Accessors(chain = true)
public class BaseEntity {

    @ApiModelProperty("主键")
    @Id
    @Column(name = "id_", columnDefinition = "bigint COMMENT 'id'")
    @GeneratedValue
    private Long id;

    @ApiModelProperty("创建时间")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time_", updatable = false, columnDefinition = "DATETIME COMMENT '添加时间'")
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("修改时间")
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_time_", columnDefinition = "DATETIME COMMENT '修改时间'")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "操作人id")
    @Column(name = "operator_id_", columnDefinition = "bigint COMMENT '操作人id'")
    private Long operatorId;

    @ApiModelProperty(value = "操作人")
    @Column(name = "operator_", columnDefinition = "varchar(255) COMMENT '操作人'")
    private String operator;

}

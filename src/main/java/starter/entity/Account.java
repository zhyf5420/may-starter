package starter.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import starter.base.constants.Role;
import starter.base.dao.BaseEntity;

import javax.persistence.*;


/**
 * 账户表
 *
 * Created on 2019/4/26.
 *
 * @author zhyf
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Accessors(chain = true)
@Table(name = "account")
@org.hibernate.annotations.Table(appliesTo = "account", comment = "账户表")
public class Account extends BaseEntity {

    @ApiModelProperty(value = "账号")
    @Column(name = "account_name_", unique = true, nullable = false, columnDefinition = "varchar(255) COMMENT '账号'")
    private String accountName;

    @ApiModelProperty(value = "密码")
    @Column(name = "password_", nullable = false, columnDefinition = "varchar(100) COMMENT '密码'")
    private String password;

    @ApiModelProperty(value = "姓名")
    @Column(name = "user_name_", columnDefinition = "varchar(10) COMMENT '姓名'")
    private String userName;

    @ApiModelProperty(value = "手机号码")
    @Column(name = "phone_num_", columnDefinition = "varchar(11) COMMENT '手机号码'")
    private String phoneNum;

    @ApiModelProperty(value = "账户是否删除")
    @Column(name = "is_delete_", nullable = false, columnDefinition = "bit default false COMMENT '账户是否删除'")
    private Boolean isDelete = false;

    @ApiModelProperty(value = "账户认证的token")
    @Column(name = "token_", columnDefinition = "varchar(100) COMMENT '用户认证的token'")
    private String token;

    @ApiModelProperty(value = "备注")
    @Column(name = "remark_", columnDefinition = "varchar(255) COMMENT '备注'")
    private String remark;

    @ApiModelProperty(value = "角色 管理员：admin，标注员：operator")
    @Column(name = "role_", nullable = false, columnDefinition = "varchar(100) COMMENT '角色 管理员：admin，标注员：operator'")
    @Enumerated(EnumType.STRING)
    private Role role;

}

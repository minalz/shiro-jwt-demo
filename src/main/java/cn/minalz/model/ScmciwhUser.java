package cn.minalz.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @description: 用户表
 * @author: minalz
 * @date: 2020-08-12 22:56
 **/
@Data
@Entity
@Table(name = "scmciwh_user_auth")
public class ScmciwhUser implements Serializable {

    private static final long serialVersionUID = -4769892525769964176L;
    // 主键ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 用户登录名
    private String username;
    // 密码
    private String password;
    // 真实姓名
    @Column(name = "real_name")
    private String realName;
    // 加盐
    private String salt;
    // 邮箱
    private String email;
    // 手机号码
    private String phone;
    // 备注
    private String remark;
    // 初始密码
    private String initial;
    // 状态：0：不启用，1：启用，2：删除
    private Integer status = 0;
    // 更新用户
    @Column(name = "update_user")
    private String updateUser;
    // 更新时间
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "update_time")
    private Date updateTime;
    // 创建用户
    @Column(name = "create_user")
    private String createUser;
    // 创建时间
//    @DateTimeFormat(
//            pattern = "yyyy-MM-dd HH:mm:ss"
//            timezone = "GMT+8"
//    )
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    // 角色集合
    @OneToMany(targetEntity = ScmciwhRole.class,cascade = CascadeType.REMOVE,fetch = FetchType.EAGER)
    @JoinTable(name = "scmciwh_user_role_auth",
            //joinColumns,当前对象在中间表中的外键
            joinColumns = {@JoinColumn(name = "user_id",referencedColumnName = "id")},
            //inverseJoinColumns，对方对象在中间表的外键
            inverseJoinColumns = {@JoinColumn(name = "role_id",referencedColumnName = "id")}
    )
    private Set<ScmciwhRole> roles = new HashSet<>();

    // 工厂集合
    @OneToMany(targetEntity = ScmciwhWerk.class,cascade = CascadeType.REMOVE,fetch = FetchType.EAGER)
    @JoinTable(name = "scmciwh_user_werk_auth",
            //joinColumns,当前对象在中间表中的外键
            joinColumns = {@JoinColumn(name = "user_id",referencedColumnName = "id")},
            //inverseJoinColumns，对方对象在中间表的外键
            inverseJoinColumns = {@JoinColumn(name = "werk_id",referencedColumnName = "id")}
    )
    private Set<ScmciwhWerk> werks = new HashSet<>();


}

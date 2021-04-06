package cn.minalz.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 定时任务实体类
 */
@Data
@Entity
@Table(name = "scmciwh_quartz_task")
public class ScmciwhQuartzTaskModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // 主键
    @Column(name = "job_name")
    private String jobName; // 任务名
    private String description; // 任务描述
    @Column(name = "cron_expression")
    private String cronExpression; // cron表达式
    @Column(name = "job_class")
    private String jobClass; // 任务执行时调用哪个类的方法 包名+类名
    @Column(name = "job_status")
    private String jobStatus; // 任务状态
    @Column(name = "job_group")
    private String jobGroup; // 任务分组
    @Column(name = "create_user")
    private Integer createUser; // 创建者
    @Column(name = "create_time")
    private LocalDateTime createTime; // 创建时间
    @Column(name = "update_user")
    private Integer updateUser; // 更新者
    @Column(name = "update_time")
    private LocalDateTime updateTime; // 更新时间
}

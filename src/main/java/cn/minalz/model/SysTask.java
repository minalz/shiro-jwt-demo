package cn.minalz.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 定时任务实体类
 */
@Data
@Entity
@Table(name = "sys_task")
public class SysTask implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 主键
    @Column(name="job_name")
    private String jobName; // 任务名
    private String description; // 任务描述
    @Column(name="cron_expression")
    private String cronExpression; // cron表达式
    @Column(name="bean_class")
    private String beanClass; // 任务执行时调用哪个类的方法 包名+类名
    @Column(name="job_status")
    private String jobStatus; // 任务状
    @Column(name="job_group")
    private String jobGroup; // 任务分
    @Column(name="create_user")
    private String createUser; // 创建者
    @Column(name="create_time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private String createTime; // 创建时间
    @Column(name="update_user")
    private String updateUser; // 更新者
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name="update_time")
    private String updateTime; // 更新时间
}

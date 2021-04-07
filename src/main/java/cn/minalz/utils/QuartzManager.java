package cn.minalz.utils;

import cn.minalz.model.ScmciwhQuartzTaskModel;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Quartz管理类
 */
@Service
@Slf4j
public class QuartzManager {
    @Autowired
    private Scheduler scheduler;

    /**
     * 添加任务
     *
     * @param scheduleJob
     * @throws SchedulerException
     */
    @SuppressWarnings("unchecked")
    public void addJob(ScmciwhQuartzTaskModel task) throws Exception {
        // 创建jobDetail实例，绑定Job实现类
        // 指明job的名称，所在组的名称，以及绑定job类

        Class<? extends Job> jobClass = (Class<? extends Job>) (Class.forName(task.getJobClass()).newInstance()
                .getClass());
        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(task.getJobName(), task.getJobGroup())// 任务名称和组构成任务key
                .build();
        // 定义调度触发规则
        // 使用cornTrigger规则
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(task.getJobName(), task.getJobGroup())// 触发器key
                .startAt(DateBuilder.futureDate(1, DateBuilder.IntervalUnit.SECOND))
                .withSchedule(CronScheduleBuilder.cronSchedule(task.getCronExpression())).startNow().build();
        // 把作业和触发器注册到任务调度中
        scheduler.scheduleJob(jobDetail, trigger);
        // 启动
        if (!scheduler.isShutdown()) {
            scheduler.start();
        }
    }

    /**
     * 获取所有计划中的任务列表
     *
     * @return
     * @throws SchedulerException
     */
    public List<ScmciwhQuartzTaskModel> getAllJob() throws SchedulerException {
        GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
        Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
        List<ScmciwhQuartzTaskModel> jobList = new ArrayList<ScmciwhQuartzTaskModel>();
        for (JobKey jobKey : jobKeys) {
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
            for (Trigger trigger : triggers) {
                ScmciwhQuartzTaskModel job = new ScmciwhQuartzTaskModel();
                job.setJobName(jobKey.getName());
                job.setJobGroup(jobKey.getGroup());
                job.setDescription("触发器:" + trigger.getKey());
                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                job.setJobStatus(triggerState.name());
                if (trigger instanceof CronTrigger) {
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    String cronExpression = cronTrigger.getCronExpression();
                    job.setCronExpression(cronExpression);
                }
                jobList.add(job);
            }
        }
        return jobList;
    }

    /**
     * 所有正在运行的job
     *
     * @return
     * @throws SchedulerException
     */
    public List<ScmciwhQuartzTaskModel> getRunningJob() throws SchedulerException {
        List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
        List<ScmciwhQuartzTaskModel> jobList = new ArrayList<ScmciwhQuartzTaskModel>(executingJobs.size());
        for (JobExecutionContext executingJob : executingJobs) {
            ScmciwhQuartzTaskModel job = new ScmciwhQuartzTaskModel();
            JobDetail jobDetail = executingJob.getJobDetail();
            JobKey jobKey = jobDetail.getKey();
            Trigger trigger = executingJob.getTrigger();
            job.setJobName(jobKey.getName());
            job.setJobGroup(jobKey.getGroup());
            job.setDescription("触发器:" + trigger.getKey());
            Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
            job.setJobStatus(triggerState.name());
            if (trigger instanceof CronTrigger) {
                CronTrigger cronTrigger = (CronTrigger) trigger;
                String cronExpression = cronTrigger.getCronExpression();
                job.setCronExpression(cronExpression);
            }
            jobList.add(job);
        }
        return jobList;
    }

    /**
     * 暂停一个job
     *
     * @param task
     * @throws SchedulerException
     */
    public void pauseJob(ScmciwhQuartzTaskModel task) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(task.getJobName(), task.getJobGroup());
        scheduler.pauseJob(jobKey);
    }

    /**
     * 恢复一个job
     *
     * @param task
     * @throws SchedulerException
     */
    public void resumeJob(ScmciwhQuartzTaskModel task) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(task.getJobName(), task.getJobGroup());
        scheduler.resumeJob(jobKey);
    }

    /**
     * 删除一个job
     *
     * @param task
     * @throws SchedulerException
     */
    public void deleteJob(ScmciwhQuartzTaskModel task) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(task.getJobName(), task.getJobGroup());
        scheduler.deleteJob(jobKey);

    }

    /**
     * 立即执行job
     *
     * @param task
     * @throws SchedulerException
     */
    public void runJobNow(ScmciwhQuartzTaskModel task) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(task.getJobName(), task.getJobGroup());
        scheduler.triggerJob(jobKey);
    }

    /**
     * 更新job时间表达式
     *
     * @param task
     * @throws SchedulerException
     */
    public void updateJobCron(ScmciwhQuartzTaskModel task) throws SchedulerException {

        TriggerKey triggerKey = TriggerKey.triggerKey(task.getJobName(), task.getJobGroup());

        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

        // withMisfireHandlingInstructionDoNothing 启动的时候或者更新CronExpressiond的时候 不立即触发一次
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(task.getCronExpression()).withMisfireHandlingInstructionDoNothing();

        trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

        scheduler.rescheduleJob(triggerKey, trigger);
    }
}

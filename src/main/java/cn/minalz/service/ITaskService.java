package cn.minalz.service;

import org.quartz.SchedulerException;

/**
 * 定时任务接口类
 */
public interface ITaskService {

    void initSchedule() throws SchedulerException;

}

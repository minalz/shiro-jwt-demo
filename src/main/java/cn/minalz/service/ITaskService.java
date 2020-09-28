package cn.minalz.service;

import org.quartz.SchedulerException;

public interface ITaskService {
    
    void initSchedule() throws SchedulerException;

}
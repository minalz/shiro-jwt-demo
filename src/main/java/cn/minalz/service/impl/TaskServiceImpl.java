package cn.minalz.service.impl;

import cn.minalz.dao.TaskRepository;
import cn.minalz.model.SysTask;
import cn.minalz.quartz.JobStatusEnum;
import cn.minalz.service.ITaskService;
import cn.minalz.utils.QuartzManager;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 定时任务实现类
 */
@Service
public class TaskServiceImpl implements ITaskService {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    QuartzManager quartzManager;

    @Override
    public void initSchedule() throws SchedulerException {
        // 这里获取任务信息数据
        List<SysTask> jobList = taskRepository.findAll();
//        List<SysTask> jobList = taskMapper.list();
        for (SysTask task : jobList) {
            if (JobStatusEnum.RUNNING.getCode().equals(task.getJobStatus())) {
                quartzManager.addJob(task);
            }
        }
    }
}

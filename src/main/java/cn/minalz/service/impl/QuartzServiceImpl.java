package cn.minalz.service.impl;

import cn.minalz.common.ResponseData;
import cn.minalz.dao.QuartzRepository;
import cn.minalz.model.ScmciwhQuartzTaskModel;
import cn.minalz.quartz.JobStatusEnum;
import cn.minalz.service.IQuartzService;
import cn.minalz.utils.QuartzManager;
import org.apache.commons.lang3.StringUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * 定时任务实现类
 */
@Service
public class QuartzServiceImpl implements IQuartzService {

    @Autowired
    QuartzRepository quartzRepository;

    @Autowired
    QuartzManager quartzManager;

    @Override
    public void initSchedule() throws Exception {
        // 这里获取任务信息数据
        List<ScmciwhQuartzTaskModel> jobList = quartzRepository.findAll();
        for (ScmciwhQuartzTaskModel task : jobList) {
            if (JobStatusEnum.RUNNING.getCode().equals(task.getJobStatus())) {
                quartzManager.addJob(task);
            }
        }
    }

    @Override
    public ScmciwhQuartzTaskModel findById(Integer id) {
        Optional<ScmciwhQuartzTaskModel> byId = quartzRepository.findById(id);
        if (!byId.isPresent()) {
            throw new RuntimeException("数据不存在");
        }
        ScmciwhQuartzTaskModel task = byId.get();
        return task;
    }

    @Override
    public ResponseData addJob(ScmciwhQuartzTaskModel quartzTask) throws Exception {
        // 先更新到数据库中
        ScmciwhQuartzTaskModel saveTask = quartzRepository.save(quartzTask);
        if (JobStatusEnum.RUNNING.getCode().equals(saveTask.getJobStatus())) {
            // 当是运行状态的时候才加到schedule中
            quartzManager.addJob(saveTask);
        }
        return ResponseData.ofok();
    }

    @Override
    public ResponseData deleteJob(Integer[] ids) throws SchedulerException {
        List<ScmciwhQuartzTaskModel> tasks = quartzRepository.findByIdIn(ids);
        for (ScmciwhQuartzTaskModel task : tasks) {
            quartzManager.deleteJob(task);
            quartzRepository.delete(task);
        }
        return ResponseData.ofok();
    }

    @Override
    public ResponseData updateJob(ScmciwhQuartzTaskModel quartzTask) throws Exception {
        // 先查询数据库中的数据 是否被删掉了
        ScmciwhQuartzTaskModel task = findById(quartzTask.getId());
        // 然后更新到数据库中
        ScmciwhQuartzTaskModel saveTask = quartzRepository.save(quartzTask);
        // 先删除更新前的Job
        quartzManager.deleteJob(task);
        if (JobStatusEnum.RUNNING.getCode().equals(saveTask.getJobStatus())) {
            // 当是运行状态的时候才加到schedule中
            quartzManager.addJob(saveTask);
        }
        return ResponseData.ofok();
    }

    @Override
    public ResponseData findAllJob(String jobName, String jobStatus, String description, int pageNumber, int pageSize) {
        Page<ScmciwhQuartzTaskModel> page = quartzRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new LinkedList<>();
            if (StringUtils.isNotEmpty(jobName)) {
                predicates.add(criteriaBuilder.equal(root.get("jobName"), jobName));
            }
            if (StringUtils.isNotEmpty(jobStatus)) {
                predicates.add(criteriaBuilder.equal(root.get("jobStatus"), jobStatus));
            }
            if (StringUtils.isNotEmpty(description)) {
                predicates.add(criteriaBuilder.equal(root.get("description"), description));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        },PageRequest.of(pageNumber - 1, pageSize));
        return ResponseData.ofPage(page, pageNumber);
    }

    @Override
    public ResponseData pauseJob(Integer id) throws SchedulerException {
        ScmciwhQuartzTaskModel task = findById(id);
        task.setJobStatus(JobStatusEnum.STOP.getCode());
        ScmciwhQuartzTaskModel saveTask = quartzRepository.save(task);
        quartzManager.pauseJob(saveTask);
        return ResponseData.ofok();
    }

    @Override
    public ResponseData resumeJob(Integer id) throws SchedulerException {
        ScmciwhQuartzTaskModel task = findById(id);
        task.setJobStatus(JobStatusEnum.RUNNING.getCode());
        ScmciwhQuartzTaskModel saveTask = quartzRepository.save(task);
        quartzManager.resumeJob(saveTask);
        return ResponseData.ofok();
    }

    @Override
    public ResponseData runJobNow(Integer id) throws SchedulerException {
        ScmciwhQuartzTaskModel task = findById(id);
        quartzManager.runJobNow(task);
        return ResponseData.ofok();
    }

    @Override
    public ResponseData updateCronExpression(Integer id, String cronStr) throws SchedulerException {
        ScmciwhQuartzTaskModel task = findById(id);
        task.setCronExpression(cronStr);
        ScmciwhQuartzTaskModel saveTask = quartzRepository.save(task);
        quartzManager.updateJobCron(saveTask);
        return ResponseData.ofok();
    }

}

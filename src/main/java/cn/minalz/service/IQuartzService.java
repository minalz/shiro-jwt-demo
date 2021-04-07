package cn.minalz.service;

import cn.minalz.common.ResponseData;
import cn.minalz.model.ScmciwhQuartzTaskModel;
import org.quartz.SchedulerException;

/**
 * 定时任务接口
 */
public interface IQuartzService {

    void initSchedule() throws Exception;

    /**
     * 根据ID查询对应的任务配置信息
     * @param id
     * @return
     */
    ScmciwhQuartzTaskModel findById(Integer id);

    /**
     * 添加一个新的Job
     * @param quartzTask
     * @return
     * @throws SchedulerException
     */
    ResponseData addJob(ScmciwhQuartzTaskModel quartzTask) throws Exception;

    /**
     * 删除Job(一个或多个)
     * @param ids
     * @return
     * @throws SchedulerException
     */
    ResponseData deleteJob(Integer[] ids) throws SchedulerException;

    /**
     * 跟新一个Job
     * @param quartzTask
     * @return
     */
    ResponseData updateJob(ScmciwhQuartzTaskModel quartzTask) throws Exception;

    /**
     * 查询所有Job
     * @param jobName 名称
     * @param jobStatus 状态
     * @param description 描述
     * @return
     */
    ResponseData findAllJob(String jobName, String jobStatus, String description, int pageNumber, int pageSize);

    /**
     * 暂停一个Job
     * @param id
     * @return
     * @throws SchedulerException
     */
    ResponseData pauseJob(Integer id) throws SchedulerException;

    /**
     * 恢复一个Job
     * @param id
     * @return
     * @throws SchedulerException
     */
    ResponseData resumeJob(Integer id) throws SchedulerException;

    /**
     * 立即触发一个Job
     * @param id
     * @return
     * @throws SchedulerException
     */
    ResponseData runJobNow(Integer id) throws SchedulerException;

    /**
     * 更新Job的Cron表达式
     * @param id
     * @param cronStr Cron表达式
     * @return
     * @throws SchedulerException
     */
    ResponseData updateCronExpression(Integer id, String cronStr) throws SchedulerException;



}

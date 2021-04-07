package cn.minalz.controller;

import cn.minalz.common.ResponseData;
import cn.minalz.dao.QuartzRepository;
import cn.minalz.model.ScmciwhQuartzTaskModel;
import cn.minalz.service.IQuartzService;
import cn.minalz.utils.QuartzManager;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 定时任务控制器
 */
@RequestMapping("/quartz")
@RestController
public class QuartzController {

    @Autowired
    IQuartzService quartzService;

    @Autowired
    QuartzManager quartzManager;

    @Autowired
    QuartzRepository taskRepository;

    @ApiOperation("新增加一个Job")
    @PostMapping("/addJob")
    public ResponseData addJob(@ApiParam("ScmciwhQuartzTaskModel对象信息") ScmciwhQuartzTaskModel quartzTask) throws Exception {
        return quartzService.addJob(quartzTask);
    }

    @ApiOperation("删除Job(一个或者多个)")
    @PostMapping("/deleteJob")
    public ResponseData deleteJob(@ApiParam("ID数组") Integer[] ids) throws SchedulerException {
        return quartzService.deleteJob(ids);
    }

    @ApiOperation("更新Job信息")
    @PostMapping("/updateJob")
    public ResponseData updateJob(@ApiParam("ScmciwhQuartzTaskModel对象信息") ScmciwhQuartzTaskModel quartzTask) throws Exception {
        return quartzService.updateJob(quartzTask);
    }

    @ApiOperation("查询数据库中的所有Job")
    @PostMapping("/findAllJob")
    public ResponseData findAllJob(@ApiParam("名称 必须唯一") String jobName,
                                   @ApiParam("状态 0:关闭 1:启动") String jobStatus,
                                   @ApiParam("描述") String description,
                                   @ApiParam(value = "当前页码", defaultValue = "1") int pageNumber,
                                   @ApiParam(value = "页面大小", defaultValue = "10") int pageSize) throws SchedulerException {
        return quartzService.findAllJob(jobName, jobStatus, description, pageNumber, pageSize);
    }

    @ApiOperation("暂停一个Job")
    @PostMapping("/pauseJob")
    public ResponseData pauseJob(@ApiParam("ID") Integer id) throws SchedulerException {
        return quartzService.pauseJob(id);
    }

    @ApiOperation("恢复一个Job")
    @PostMapping("/resumeJob")
    public ResponseData resumeJob(@ApiParam("ID") Integer id) throws SchedulerException {
        return quartzService.resumeJob(id);
    }

    @ApiOperation("立即触发一个Job")
    @PostMapping("/runJobNow")
    public ResponseData runJobNow(@ApiParam("ID") Integer id) throws SchedulerException {
        return quartzService.runJobNow(id);
    }

    @ApiOperation("更新job的Cron表达式时间")
    @PostMapping("/updateCronExpression")
    public ResponseData updateCronExpression(@ApiParam("ID") Integer id, @ApiParam("Cron表达式") String cronStr) throws SchedulerException {
        return quartzService.updateCronExpression(id, cronStr);
    }
}

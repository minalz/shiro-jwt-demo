package cn.minalz.controller;

import cn.minalz.aspect.NoRepeatSubmit;
import cn.minalz.common.ResponseData;
import cn.minalz.dao.TaskRepository;
import cn.minalz.dao.UserRepository;
import cn.minalz.exception.CoreException;
import cn.minalz.model.SysTask;
import cn.minalz.utils.QuartzManager;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * 测试代码
 */
@RestController
public class TestController {

    @Autowired
    private UserRepository scmciwhUserRepository;

    @Autowired
    private QuartzManager quartzManager;

    @Autowired
    private TaskRepository taskRepository;

    @PostMapping("/demo")
    public ResponseData demo() {
        ResponseData responseData = new ResponseData();
        responseData.msg = "示例返";
        return responseData;
    }

    @PostMapping("/home")
    public ResponseData home() {
        ResponseData responseData = new ResponseData();
        responseData.msg = "我是首页";
        return responseData;
    }

    @PostMapping("/admin")
    @RequiresRoles("CJGLY")
    public ResponseData admin() {
        ResponseData responseData = new ResponseData();
        responseData.msg = "我是管理员";
        return responseData;
    }

    @PostMapping("/normal")
    @RequiresRoles("GLDP") // 加了这个注解 MyRealm中的授权就可以执行了
    public ResponseData normal() {
        ResponseData responseData = new ResponseData();
        responseData.msg = "我是普通用户";
        return responseData;
    }

    @PostMapping("/normal1")
    public ResponseData normal1() {
        ResponseData responseData = new ResponseData();
        responseData.msg = "我是普通用户1";
        return responseData;
    }

    @PostMapping("/normal2")
    public ResponseData normal2() {
        ResponseData responseData = new ResponseData();
        responseData.msg = "我是普通用户2";
        return responseData;
    }

    @PostMapping("/normal3")
    public ResponseData normal3() {
        ResponseData responseData = new ResponseData();
        responseData.msg = "我是普通用户3";
        return responseData;
    }

    @PostMapping("/normal4")
    public ResponseData normal4() {
        ResponseData responseData = new ResponseData();
        responseData.msg = "我是普通用户4";
        return responseData;
    }

    @PostMapping("/normal5")
    @NoRepeatSubmit(lockTime = 300)
    public ResponseData normal5() {
        ResponseData responseData = new ResponseData();
        responseData.msg = "我是普通用户5";
        return responseData;
    }

    @PostMapping("/updateJob")
    public ResponseData updateJobCron(Long id, String cronStr) throws SchedulerException {
        Optional<SysTask> byId = taskRepository.findById(id);
        if (!byId.isPresent()) {
            return ResponseData.oferror("数据不存在");
        }
        SysTask task = byId.get();
        task.setCronExpression(cronStr);
        SysTask saveTask = taskRepository.save(task);
        quartzManager.updateJobCron(saveTask);
        return ResponseData.ofok();
    }

    @NoRepeatSubmit
    @PostMapping("test")
    public ResponseData test(String tokenId){
        System.out.println("tokenId -- " + tokenId);
        throw CoreException.of("测试抛出的异常能否被切面抓取");

    }

}

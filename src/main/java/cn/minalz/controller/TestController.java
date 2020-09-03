package cn.minalz.controller;

import cn.minalz.common.ResponseData;
import cn.minalz.dao.UserRepository;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private UserRepository scmciwhUserRepository;

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

    @PostMapping("/normal2")
    public ResponseData normal2() {
        ResponseData responseData = new ResponseData();
        responseData.msg = "我是普通用户2";
        return responseData;
    }

}
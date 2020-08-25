package cn.minalz.controller;

import cn.minalz.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scmciwh")
public class TestController {

    @Autowired
    private UserRepository scmciwhUserRepository;

    @PostMapping("/demo")
    public String demo() {
        return "示例返回";
    }

    @PostMapping("/home")
    public String home() {
        return "我是首页";
    }

    @PostMapping("/admin")
    public Object admin() {
        return "我是管理员";
    }

    @PostMapping("/normal")
    public String normal() {
        return "我是普通用户";
    }

}
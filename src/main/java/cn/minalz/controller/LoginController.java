package cn.minalz.controller;

import cn.minalz.common.ResponseData;
import cn.minalz.config.redis.RedisConstant;
import cn.minalz.dao.UserRepository;
import cn.minalz.dto.UserRedisToken;
import cn.minalz.model.ScmciwhUser;
import cn.minalz.utils.Common;
import cn.minalz.utils.JwtUtil;
import cn.minalz.utils.RedisUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class LoginController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserRepository scmciwhUserRepository;

    @Autowired
    private RedisUtil redisUtil;

    @GetMapping("/login")
    public String loginPage() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.getPrincipal() != null) {
            return "redirect:/login_success";
        }
        return "login.html";
    }

    /*@ResponseBody
    @PostMapping("/login")
    public String login(HttpServletRequest request) {
        // <1> 判断是否已经登陆
        Subject subject = SecurityUtils.getSubject();
        if (subject.getPrincipal() != null) {
            return "你已经登陆账号：" + subject.getPrincipal();
        }

        // <2> 获得登陆失败的原因
        String shiroLoginFailure = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
        // 翻译成人类看的懂的提示
        String msg = "";
        if (UnknownAccountException.class.getName().equals(shiroLoginFailure)) {
            msg = "账号不存在";
        } else if (IncorrectCredentialsException.class.getName().equals(shiroLoginFailure)) {
            msg = "密码不正确";
        } else if (LockedAccountException.class.getName().equals(shiroLoginFailure)) {
            msg = "账号被锁定";
        } else if (ExpiredCredentialsException.class.getName().equals(shiroLoginFailure)) {
            msg = "账号已过期";
        } else {
            msg = "未知";
            logger.error("[login][未知登陆错误：{}]", shiroLoginFailure);
        }
        return "登陆失败，原因：" + msg;
    }*/

    @ResponseBody
    @PostMapping("/login")
    public ResponseData login(String username, String password) {
        ResponseData responseData = new ResponseData();
        // <1> 判断是否已经登陆
//        Subject subject = SecurityUtils.getSubject();
//        if (subject.getPrincipal() != null) {
//            responseData.msg = "你已经登陆账号：" + subject.getPrincipal();
////            return "你已经登陆账号：" + subject.getPrincipal();
//        }
        Optional<ScmciwhUser> topByUsername = scmciwhUserRepository.findTopByUsername(username);
        // 两个提示都一样 可以让用户不知道到底是账号还是密码错误 一定程度可以迷惑恶意攻击者
        if(!topByUsername.isPresent()){
            responseData.msg = "登录账号密码错误";
//            return "登录账号密码错误";
        }
        ScmciwhUser user = topByUsername.get();
        password = Common.getMD5(password);
        if(!user.getPassword().equals(password)){
            responseData.msg = "登录账号密码错误";
//            return "登录账号密码错误";
        }

        // 走到这里 说明账号密码是正确的了
        HashMap<String,Object> map = new HashMap<>();
        map.put("username",user.getUsername());
        String token = JwtUtil.generateToken(map);

        // 生成的token应该要存储到redis中
        UserRedisToken userRedisToken = new UserRedisToken(token,new Date().getTime());
        redisUtil.set(RedisConstant.REDIS_STORAGE_USERNAME_PREFIX + username + "_" + token,userRedisToken,RedisConstant.REDIS_STORAGE_USERNAME_TIME);
        logger.info("登录成功 --> 用户名：[{}] -- {}",username,userRedisToken);

        responseData.data.put("username",username);
//        responseData.data.put("isAdmin",true);
//        responseData.data.put("werks",werks);
        responseData.token = token;
        responseData.data.put("token",token);

        return responseData;
    }

    public static void main(String[] args) {
        HashMap<String,Object> map = new HashMap<>();
        map.put("username","cjgly");
        String token = JwtUtil.generateToken(map);
        System.out.println("token -- " + token);
    }

    /**
     * 退出操作由前端清除存储在缓存中的token即可
     * @return
     */
    /*@RequestMapping("/logout")
    @ResponseBody
    public ResponseData logout(){
        ResponseData responseData = new ResponseData();
        System.out.println("执行退出登录的操作");
        try {
            // 清除账号的信息 将shiro中存入的token清除掉
            Subject subject = SecurityUtils.getSubject();
            ScmciwhUser user = (ScmciwhUser)subject.getPrincipal();
            System.out.println("user -- " + user);
//            subject.logout();
            // 删除Redis中的token缓存
//            redisUtil.del(RedisConstant.REDIS_STORAGE_USERNAME_PREFIX);
            responseData.msg = "退出成功";
        } catch (Exception e) {
            e.printStackTrace();
            responseData.code = "01";
            responseData.msg = "退出失败";
        }
        return responseData;
    }*/

    @GetMapping("/login_success")
    @ResponseBody
    public ResponseData loginSuccess() {
        ResponseData responseData = new ResponseData();
        responseData.msg = "登录成功";
        return responseData;
    }

    @GetMapping("/unauthorized")
    @ResponseBody
    public ResponseData unauthorized() {
        ResponseData responseData = new ResponseData();
        responseData.code = "01";
        responseData.msg = "无权限403";
        return responseData;
    }

}
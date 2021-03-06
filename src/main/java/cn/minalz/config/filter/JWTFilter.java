package cn.minalz.config.filter;

import cn.minalz.config.jwt.JwtToken;
import cn.minalz.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义一个Filter，用来拦截所有的请求判断是否携带Token
 * isAccessAllowed()判断是否携带了有效的JwtToken
 * onAccessDenied()是没有携带JwtToken的时候进行账号密码登录，登录成功允许访问，登录失败拒绝访问
 * */
@Slf4j
public class JWTFilter extends AccessControlFilter {

    @Autowired
    private RedisUtil redisUtil;

    /*
     * 1. 返回true，shiro就直接允许访问url
     * 2. 返回false，shiro才会根据onAccessDenied的方法的返回值决定是否允许访问url
     * */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        log.warn("isAccessAllowed 方法被调用");
//        Subject subject = getSubject(request,response);
        String url = getPathWithinApplication(request);
        log.info("当前用户正在访问的 url ==> {}", url);
//        return subject.isPermitted(url);
        return false;
    }

    /**
     * 返回结果为true表明登录通过
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        log.warn("onAccessDenied 方法被调用");
        //这个地方和前端约定，要求前端将jwtToken放在请求的Header部分

        //所以以后发起请求的时候就需要在Header中放一个Authorization，值就是对应的Token
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String token = request.getHeader("Authorization");
        if (token == null) {
            onLoginFail401(servletResponse);
            //调用下面的方法向客户端返回错误信息
            return false;
        }
        log.info("请求的 Header 中藏有 token --> {}", token);
        JwtToken jwtToken = new JwtToken(token);
        /*
         * 下面就是固定写法
         * */
        try {
            // 委托 realm 进行登录认证
            //所以这个地方最终还是调用JwtRealm进行的认证
            Subject subject = getSubject(servletRequest, servletResponse);
            subject.login(jwtToken);
            // 如果这里登录成功  那么刷新token的过期时间
            // 方法级别的url的权限校验
            /*String url = getPathWithinApplication(request);
            log.info("当前用户正在访问的 url => " + url);
            boolean permitted = subject.isPermitted(url);
            if(!permitted){
                onLoginFail403(servletResponse);
                return false;
            }*/
            // 方法级别的url的权限校验
        } catch (Exception e) {
            e.printStackTrace();
            onLoginFail401(servletResponse);
            //调用下面的方法向客户端返回错误信息
            return false;
        }
        return true;
        //执行方法中没有抛出异常就表示登录成功
    }

    //登录失败时默认返回 401 状态码
    private void onLoginFail401(ServletResponse response) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setContentType("text/html;charset=utf-8");
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpResponse.getWriter().write("login error 请先登录");
    }

}

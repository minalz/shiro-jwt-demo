package cn.minalz.utils;

import cn.minalz.model.ScmciwhUser;
import org.apache.shiro.SecurityUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 关于登录用户的基础工具类
 * 可以获取用户id,工厂id等数据
 */
public class BaseUtils {

    /**
     * 获取当前登录用户
     */
    public static ScmciwhUser getCurrentUser() {
        ScmciwhUser user = (ScmciwhUser) SecurityUtils.getSubject().getPrincipal();
        return user;
    }

    /**
     * 获取当前登录用户ID
     */
    public static Long getCurrentId() {
        ScmciwhUser user = (ScmciwhUser) SecurityUtils.getSubject().getPrincipal();
        return user.getId();
    }

    /**
     * 获取登录用户的map
     *
     * @param httpServletRequest
     * @return
     */
    public static Map<String, Object> getUserParameters(HttpServletRequest httpServletRequest) {
        return JwtUtil.validateToken(httpServletRequest.getParameter("token"));
    }

    /**
     * 获取登录用户的工厂
     *
     * @param httpServletRequest
     * @return
     */
    public static String getWerks(HttpServletRequest httpServletRequest) {
        return getUserParameters(httpServletRequest).get("werks").toString();
    }

}

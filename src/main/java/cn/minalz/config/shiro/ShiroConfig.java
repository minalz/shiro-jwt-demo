package cn.minalz.config.shiro;

import cn.minalz.config.filter.JWTFilter;
import cn.minalz.config.filter.MyLogoutFilter;
import cn.minalz.config.jwt.JwtDefaultSubjectFactory;
import cn.minalz.config.jwt.UrlPermissionResolver;
import cn.minalz.dao.UserRepository;
import cn.minalz.utils.RedisUtil;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 执行流程：
 * 1. 客户端发起请求，shiro的过滤器生效，判断是否是login或logout的请求   如果是就直接执行请求   如果不是就进入JwtFilter
 * 2. JwtFilter执行流程
 * 2.1. 获取header是否有"Authorization"的键，有就获取，没有就抛出异常
 * 2.2. 将获取的jwt字符串封装在创建的JwtToken中，使用subject执行login()方法进行校验。这个方法会调用创建的JwtRealm
 * 2.3. 执行JwtRealm中的认证方法，使用validateToken(token)判断是否登录过
 * 2.4. 返回true就使基础执行下去
 */
@Configuration
@AutoConfigureAfter(ShiroLifecycleBeanPostProcessorConfig.class)
public class ShiroConfig {

    @Autowired
    private UserRepository scmciwhUserRepository;

    @Autowired
    private RedisUtil redisUtil;

    @Bean
    public Realm realm() {
        MyRealm myRealm = new MyRealm();
        myRealm.setPermissionResolver(new UrlPermissionResolver());
        //开启授权信息的缓存，默认开启
        myRealm.setAuthorizationCachingEnabled(true);
        myRealm.setAuthorizationCacheName("author");
        //开启认证信息的缓存，默认关闭，key是UserNamePasswordToken，value就是principle
        myRealm.setAuthenticationCachingEnabled(false);
        myRealm.setAuthenticationCacheName("authen");

        return myRealm;
    }

    @Bean
    public DefaultWebSecurityManager securityManager() {
        // 创建 DefaultWebSecurityManager 对象
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 设置其使用的 Realm
        securityManager.setRealm(this.realm());

        // 关闭 ShiroDAO 功能
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        // 不需要将 Shiro Session 中的东西存到任何地方（包括 Http Session 中）
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);
        //禁止Subject的getSession方法
        securityManager.setSubjectFactory(new JwtDefaultSubjectFactory());
        //设置缓存管理器
        securityManager.setCacheManager(new MyRedisCacheManager(redisUtil));
        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean() {
        // <1> 创建 ShiroFilterFactoryBean 对象，用于创建 ShiroFilter 过滤器
        ShiroFilterFactoryBean filterFactoryBean = new ShiroFilterFactoryBean();

        // <2> 设置 SecurityManager
        filterFactoryBean.setSecurityManager(this.securityManager());

        // <3> 设置 URL 们
        filterFactoryBean.setLoginUrl("/login"); // 登陆 URL
//        filterFactoryBean.setSuccessUrl("/login_success"); // 登陆成功 URL
//        filterFactoryBean.setUnauthorizedUrl("/unauthorized"); // 无权限 URL

        // 添加自定义的shiro注销过滤器
        filterFactoryBean.setFilters(myFilters());

        // <4> 设置 URL 的权限配置
        filterFactoryBean.setFilterChainDefinitionMap(this.filterChainDefinitionMap());

        return filterFactoryBean;
    }

    /**
     * 设置访问权限  访问xx资源 需要xx权限
     * @return
     */
    private Map<String, String> filterChainDefinitionMap() {
        Map<String, String> filterMap = new LinkedHashMap<>(); // 注意要使用有序的 LinkedHashMap ，顺序匹配
        filterMap.put("/login", "anon"); // 允许匿名访问
//        filterMap.put("/", "anon"); // 允许匿名访问
//        filterMap.put("/scmciwh/admin", "roles[CJGLY]"); // 超级管理员
//        filterMap.put("/scmciwh/normal", "roles[GLDP]"); // 需要 NORMAL 角色
        filterMap.put("/logout", "logout"); // 退出
        filterMap.put("/**", "jwtFilter"); // 默认剩余的 URL ，需要经过认证
//        filterMap.put("/**", "authc"); // 默认剩余的 URL ，需要经过认证
        return filterMap;
    }

    /**
     * 自定义配置拦截器
     * @return
     */
    private Map<String, Filter> myFilters(){
        Map<String, Filter> filtersMap = new LinkedHashMap<>();
        // 自定义JWTFilter
        filtersMap.put("jwtFilter",new JWTFilter());
        // 自定义authc权限验证的过滤器
//        filtersMap.put("authc", new MyFormAuthenticationFilter());
        // 配置自定义的shiro注销过滤器
        filtersMap.put("logout", new MyLogoutFilter());
        return filtersMap;
    }

}
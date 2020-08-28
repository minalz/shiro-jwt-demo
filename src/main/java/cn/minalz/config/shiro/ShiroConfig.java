package cn.minalz.config.shiro;

import cn.minalz.config.filter.JWTFilter;
import cn.minalz.config.filter.MyLogoutFilter;
import cn.minalz.config.jwt.JwtDefaultSubjectFactory;
import cn.minalz.dao.UserRepository;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.ExecutorServiceSessionValidationScheduler;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Autowired
    private UserRepository scmciwhUserRepository;

    @Bean
    public Realm realm() {
        MyRealm myRealm = new MyRealm();
        //告诉realm密码匹配方式
//        myRealm.setCredentialsMatcher(myCredentialsMatcher());
        //开启授权信息的缓存，默认开启
        myRealm.setAuthorizationCachingEnabled(true);
        myRealm.setAuthorizationCacheName("author");
        //开启认证信息的缓存，默认关闭，key是UserNamePasswordToken，value就是principle
        myRealm.setAuthenticationCachingEnabled(false);
        myRealm.setAuthenticationCacheName("authen");
        //设置缓存管理器
//        myRealm.setCacheManager(cacheManager());
        return myRealm;
    }

    @Bean
    public DefaultWebSecurityManager securityManager() {
        // 创建 DefaultWebSecurityManager 对象
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 配置 rememberMeCookie 查看源码可以知道，这里的rememberMeManager就仅仅是一个赋值，所以先执行
//        securityManager.setRememberMeManager(rememberMeManager());
        // 设置其使用的 Realm
        securityManager.setRealm(this.realm());
        securityManager.setSessionManager(sessionManager());

        // 关闭 ShiroDAO 功能
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        // 不需要将 Shiro Session 中的东西存到任何地方（包括 Http Session 中）
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);
        //禁止Subject的getSession方法
        securityManager.setSubjectFactory(new JwtDefaultSubjectFactory());
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
     * 配置自定义的加密方式
     * @return
     */
    @Bean
    public MyCredentialsMatcher myCredentialsMatcher() {
        return new MyCredentialsMatcher();
    }

    //缓存管理
    @Bean
    public CacheManager cacheManager(){
        MyRedisCacheManager cacheManager = new MyRedisCacheManager();

        return cacheManager;
    }

    /**
     * 自定义会话管理器
     * @return
     */
    @Bean
    public SessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        //是否开启定时调度器进行检测过期session 默认为true
        sessionManager.setSessionValidationSchedulerEnabled(false);

        return sessionManager;
    }

    /**
     * session会话验证调度器
     * @return session会话验证调度器
     */
    @Bean
    public ExecutorServiceSessionValidationScheduler configSessionValidationScheduler() {
        ExecutorServiceSessionValidationScheduler sessionValidationScheduler = new ExecutorServiceSessionValidationScheduler();
        //设置session的失效扫描间隔，单位为毫秒
        sessionValidationScheduler.setInterval(20*1000);
        return sessionValidationScheduler;
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

    /**
     * 注解支持
     */
    @Bean
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
        defaultAAP.setProxyTargetClass(true);
        return defaultAAP;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

}
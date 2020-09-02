package cn.minalz.config.shiro;

import cn.minalz.config.jwt.JwtToken;
import cn.minalz.dao.UserRepository;
import cn.minalz.model.ScmciwhPermission;
import cn.minalz.model.ScmciwhRole;
import cn.minalz.model.ScmciwhUser;
import cn.minalz.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @description: 自定义的Shiro Realm 用于认证
 * @author: minalz
 * @date: 2020-07-25 00:09
 **/
@Slf4j
public class MyRealm extends AuthorizingRealm {

    @Autowired
    private UserRepository scmciwhUserRepository;

    /*
     * 多重写一个support
     * 标识这个Realm是专门用来验证JwtToken
     * 不负责验证其他的token（UsernamePasswordToken）
     * */
    @Override
    public boolean supports(AuthenticationToken token) {
        //这个token就是从过滤器中传入的jwtToken
        return token instanceof JwtToken;
    }

    //获取权限信息的方法
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("走了权限查询方法");
        //获取用户名
        //此Principal就是彼Principal（认证时构造的）
        ScmciwhUser user = (ScmciwhUser)principals.getPrimaryPrincipal();
//        String username = user.getUsername();
        //从数据库查询用户的权限和角色
//        List<ScmciwhRoleModel> roles = scmciwhUserRepository.findRolesByUsername(username);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        Set<ScmciwhRole> roles = user.getRoles();
        Set<String> rolesSet = roles.stream().map(ScmciwhRole::getRoleCode).collect(Collectors.toSet());
        if(rolesSet != null && rolesSet.size() > 0){
            simpleAuthorizationInfo.addRoles(rolesSet);
        }
        Set<String> permissionsSet = new HashSet<>();
        roles.forEach(x -> {
            Set<ScmciwhPermission> permissions = x.getPermissions();
            Set<String> perms = permissions.stream().filter(p -> {
                return StringUtils.isNotEmpty(p.getUrl());
            }).map(ScmciwhPermission::getUrl).collect(Collectors.toSet());
            permissionsSet.addAll(perms);
        });
        if(permissionsSet != null && permissionsSet.size() > 0){
            simpleAuthorizationInfo.addStringPermissions(permissionsSet);
        }

        // 以上完成了动态地对用户授权
        log.info("rolesSet => " + rolesSet);
        log.info("permissionsSet => " + permissionsSet);
        return simpleAuthorizationInfo;
    }

    //获取认证信息的方法
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        //token是封装好的用户提交的用户名密码
        JwtToken jwtToken = (JwtToken) auth;
        String token = (String)jwtToken.getPrincipal();
        if(token == null){
            return null;
        }
        Map<String, Object> tokenMap = JwtUtil.validateToken(token);
        String username = (String)tokenMap.get("username");
        //获取用户
        Optional<ScmciwhUser> topByUsername = scmciwhUserRepository.findTopByUsername(username);
        if(!topByUsername.isPresent()){
            return null;
        }else{
            ScmciwhUser user = topByUsername.get();
            //封装AuthenticationInfo
//            ByteSource bsSalt = new SimpleByteSource(user.getPrivateSalt());
//            new MySimpleByteSource(salt)
//            SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(token, token, getName());
            SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user,token,null,getName());
//            SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(token,token,null,getName());
            return authenticationInfo;
        }
    }

    /**
     * 建议重写此方法，提供唯一的缓存Key
     */
    @Override
    protected Object getAuthorizationCacheKey(PrincipalCollection principals) {
        ScmciwhUser user = (ScmciwhUser) principals.getPrimaryPrincipal();
        return user.getUsername();
//        return super.getAuthorizationCacheKey(principals);
    }

    /**
     * 建议重写此方法，提供唯一的缓存Key
     */
    @Override
    protected Object getAuthenticationCacheKey(PrincipalCollection principals) {
        ScmciwhUser user = (ScmciwhUser) principals.getPrimaryPrincipal();
        return user.getUsername();
//        return super.getAuthenticationCacheKey(principals);
    }

    /**
     * 重写方法,清除当前用户的的 授权缓存
     * @param principals
     */
    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    /**
     * 重写方法，清除当前用户的 认证缓存
     * @param principals
     */
    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

    /**
     * 自定义方法：清除所有 授权缓存
     */
    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    /**
     * 自定义方法：清除所有 认证缓存
     */
    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    /**
     * 自定义方法：清除所有的  认证缓存  和 授权缓存
     */
    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }
}

package cn.minalz.config.jwt;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.PermissionResolver;
import org.apache.shiro.authz.permission.WildcardPermission;

@Slf4j
public class UrlPermissionResolver implements PermissionResolver {

    /**
     * 经过调试发现
     * subject.isPermitted(url) 中传入的字符串
     * 和自定义 Realm 中传入的权限字符串集合都要经过这个 resolver
     *
     * @param s
     * @return
     */
    @Override
    public Permission resolvePermission(String s) {
        log.debug("s => " + s);

        if (s.startsWith("/")) {
            return new UrlPermission(s);
        }
        return new WildcardPermission(s);
    }
}
package cn.minalz.config.redis;

/**
 * 用于Redis存储的常量设置
 */
public class RedisConstant {

    // 用于存储每个用户token的key的前缀 结构是String 因为可以设置 过期时间
    // 用户名的存储结构为 key前缀_用户名_token --> REDIS_STORAGE_用户名_token
    public static final String REDIS_STORAGE_USERNAME_PREFIX = "REDIS_STORAGE_";

    // 用于存储每个用户token的key的有效时间  存Redis中 是秒
    public static final Long REDIS_STORAGE_USERNAME_TIME = 60 * 60 * 2L;

    // 用于存储每个用户token的key的有效时间  存Token中 是毫秒
    public static final Long TOKEN_STORAGE_USERNAME_TIME = REDIS_STORAGE_USERNAME_TIME * 1000;

    // shiro中授权的缓存key
    public static final String REDIS_SHIRO_CACHE_KEY_PREFIX = "SHIRO_CACHE_KEY_";

    // shiro中授权的缓存key 过期时间(秒) 暂不建议进行授权的缓存 每次查询一遍  并不怎么影响性能 因为用户数不多
    // 这里需要注意 一旦设置了长时间的授权缓存  那么就要对缓存进行更新管理了  防止数据库人为更新或者利用接口更新
    public static final Long REDIS_SHIRO_CACHE_KEY_TIME = 60 * 2L;

}

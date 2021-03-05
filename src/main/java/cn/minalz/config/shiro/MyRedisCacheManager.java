package cn.minalz.config.shiro;

import cn.minalz.config.redis.ShiroRedisCache;
import cn.minalz.utils.RedisUtil;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

/**
 * 自定义Redis的缓存管理器
 */
public class MyRedisCacheManager implements CacheManager {

    private RedisUtil redisUtil;

    public MyRedisCacheManager(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        return new ShiroRedisCache(name, redisUtil);
    }
}
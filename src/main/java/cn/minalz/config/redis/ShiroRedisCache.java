package cn.minalz.config.redis;

import cn.minalz.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 自定义shiro的Redis缓存
 * @param <K> key
 * @param <V> value
 */
@Slf4j
public class ShiroRedisCache<K, V> implements Cache<K, V> {

    /**
     * cache name
     */
    private String name;

    /**
     * redis工具类
     */
    private RedisUtil redisUtil;

    public ShiroRedisCache(String name, RedisUtil redisUtil) {
        this.name = name;
        this.redisUtil = redisUtil;
    }

    @Override
    public V get(K key) throws CacheException {
        log.info("shiro redis cache get.{} K={}", name, key);
        V result = (V) redisUtil.get(generateKey(key));
        return result;
    }

    @Override
    public V put(K key, V value) throws CacheException {
        log.info("shiro redis cache put.{} K={} V={}", name, key, value);
        V result = (V) redisUtil.get(generateKey(key));
        redisUtil.set(generateKey(key), value, RedisConstant.REDIS_SHIRO_CACHE_KEY_TIME);
        return result;
    }

    @Override
    public V remove(K key) throws CacheException {
        log.info("shiro redis cache remove.{} K={}", name, key);
        V result = (V) redisUtil.get(generateKey(key));
        redisUtil.del(generateKey(key));
        return result;
    }

    @Override
    public void clear() throws CacheException {
        log.info("shiro redis cache clear.{}", name);
        Set<String> keys = redisUtil.keys(RedisConstant.REDIS_SHIRO_CACHE_KEY_PREFIX + "*");
        if (keys.size() == 0) {
            return;
        }
        keys.forEach(x -> {
            redisUtil.del(x);
        });
    }

    @Override
    public int size() {
        log.info("shiro redis cache size.{}", name);
        int length = redisUtil.count(RedisConstant.REDIS_SHIRO_CACHE_KEY_PREFIX + "*").intValue();
        return length;
    }

    @Override
    public Set keys() {
        log.info("shiro redis cache keys.{}", name);
        Set<String> keys = redisUtil.keys(RedisConstant.REDIS_SHIRO_CACHE_KEY_PREFIX + "*");
        return keys;
    }

    @Override
    public Collection values() {
        Set keys = this.keys();
        List<Object> values = new ArrayList<Object>();
        for (Object key : keys) {
            Object value = redisUtil.get(key.toString());
            values.add(value);
        }
        return values;
    }

    /**
     * 重组key
     * 区别其他使用环境的key
     *
     * @param key
     * @return
     */
    private String generateKey(K key) {
        return RedisConstant.REDIS_SHIRO_CACHE_KEY_PREFIX + name + "_" + key;
    }

}

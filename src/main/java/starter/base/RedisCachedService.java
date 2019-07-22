package starter.base;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 缓存service
 *
 * @author zhyf
 */
@Repository
public class RedisCachedService {

    @Resource
    private RedisTemplate<Object, Object> redisTemplate;

    @Resource
    private ValueOperations<String, Object> valueOperations;

    /**
     * 设置缓存,没有过期时间
     */
    public void set(String key, Object value) {
        set(key, value, null);
    }

    /**
     * 设置缓存
     *
     * @param expireTime：过期时间，单位秒（例如exprieTime=30，为30秒） 填0将不设置
     */
    public void set(String key, Object value, Integer expireTime) {
        valueOperations.set(key, value);
        if (expireTime != null && expireTime > 0) {
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
        }
    }

    /**
     * 获取缓存值
     */
    public Object get(String key) {
        return valueOperations.get(key);
    }

    /**
     * 获得缓存中的数据并重置其过期时间.
     */
    public Object getAndTouch(String key, int expireTime) {
        Object value = valueOperations.get(key);
        if (value != null) {
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
        }
        return value;
    }

    /**
     * 判断缓存中是否有对应的value
     */
    public boolean exists(final String key) {
        boolean hasOk = redisTemplate.hasKey(key);
        if (hasOk) {
            Long expireTime = redisTemplate.getExpire(key, TimeUnit.SECONDS);
            //已经到期尚未删除的Key。
            if (expireTime == 0) {
                return false;
            }
        }
        return hasOk;
    }

    /**
     * 删除key
     */
    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 删除多个key
     */
    public void deleteKey(String... keys) {
        Set<String> kSet = Stream.of(keys).collect(Collectors.toSet());
        redisTemplate.delete(kSet);
    }

    /**
     * 删除Key的集合
     */
    public void deleteKey(Collection<String> keys) {
        Set<String> kSet = new HashSet<>(keys);
        redisTemplate.delete(kSet);
    }

    /**
     * 设置key的生命周期
     */
    public void expireKey(String key, long time, TimeUnit timeUnit) {
        redisTemplate.expire(key, time, timeUnit);
    }

    /**
     * 指定key在指定的日期过期
     */
    public void expireKeyAt(String key, Date date) {
        redisTemplate.expireAt(key, date);
    }

    /**
     * 将key设置为永久有效
     */
    public void persistKey(String key) {
        redisTemplate.persist(key);
    }

    /**
     * 查询key的生命周期
     */
    public long getKeyExpire(String key, TimeUnit timeUnit) {
        return redisTemplate.getExpire(key, timeUnit);
    }

}

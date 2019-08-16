package starter.base.utils.cache;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.date.DateUnit;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * Created on 2019/7/5.
 *
 * @author zhyf
 */
@Primary
@Component
public class MapCache implements ICache {

    private static TimedCache<String, Object> timedCache = CacheUtil.newTimedCache(DateUnit.HOUR.getMillis() * 12);

    static {
        timedCache.schedulePrune(DateUnit.MINUTE.getMillis());
    }

    /**
     * 判断缓存中是否有对应的value
     */
    @Override
    public Boolean exists(String key) {
        return timedCache.containsKey(key);
    }

    /**
     * 获取缓存值
     */
    @Override
    public Object get(String key) {
        return timedCache.get(key);
    }

    /**
     * 删除缓存
     */
    @Override
    public void deleteKey(String key) {
        timedCache.remove(key);
    }

    /**
     * 设置缓存,默认过期时间
     */
    @Override
    public void set(String key, Object value) {
        timedCache.put(key, value);
    }

    /**
     * 设置缓存
     */
    @Override
    public void set(String key, Object value, Integer expireTime) {
        timedCache.put(key, value, DateUnit.SECOND.getMillis() * expireTime);
    }

}

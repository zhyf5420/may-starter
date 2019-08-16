package starter.base.utils.cache;

/**
 * Created on 2019/7/5.
 *
 * @author zhyf
 */
public interface ICache {

    Boolean exists(final String key);

    Object get(String key);

    void deleteKey(String key);

    void set(String key, Object value);

    void set(String key, Object value, Integer expireTime);

}

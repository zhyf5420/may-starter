package starter.base.utils;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created on 2019/1/3 0003.
 *
 * @author zhyf
 */
public class Functions {

    /**
     * .filter(Functions.distinctByKey(o->o.getFullName()))
     */
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

}

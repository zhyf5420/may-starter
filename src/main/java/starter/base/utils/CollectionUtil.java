package starter.base.utils;

import java.util.*;
import java.util.Map.Entry;

/**
 * 集合相关工具类，包括数组
 *
 * @author zhyf
 */
public final class CollectionUtil {

    private CollectionUtil() {
    }

    /**
     * 多个集合的并集<br> 针对一个集合中存在多个相同元素的情况，计算两个集合中此元素的个数，保留最多的个数<br> 例如：集合1：[a, b, c, c, c]，集合2：[a, b, c, c]<br> 结果：[a, b, c,
     * c, c]，此结果中只保留了三个c
     *
     * @param coll1      集合1
     * @param coll2      集合2
     * @param otherColls 其它集合
     *
     * @return 并集的集合，返回 {@link ArrayList}
     */
    @SafeVarargs
    public static <T> Collection<T> union(final Collection<T> coll1, final Collection<T> coll2, final Collection<T>... otherColls) {
        Collection<T> union = union(coll1, coll2);
        for (Collection<T> coll : otherColls) {
            union = union(union, coll);
        }
        return union;
    }

    /**
     * 两个集合的并集<br> 针对一个集合中存在多个相同元素的情况，计算两个集合中此元素的个数，保留最多的个数<br> 例如：集合1：[a, b, c, c, c]，集合2：[a, b, c, c]<br> 结果：[a, b, c,
     * c, c]，此结果中只保留了三个c
     *
     * @param coll1 集合1
     * @param coll2 集合2
     *
     * @return 并集的集合，返回 {@link ArrayList}
     */
    public static <T> Collection<T> union(final Collection<T> coll1, final Collection<T> coll2) {
        final ArrayList<T> list = new ArrayList<>();
        if (isEmpty(coll1)) {
            list.addAll(coll2);
        } else if (isEmpty(coll2)) {
            list.addAll(coll1);
        } else {
            final Map<T, Integer> map1 = countMap(coll1);
            final Map<T, Integer> map2 = countMap(coll2);
            final Set<T> elts = newHashSet(coll2);
            for (T t : elts) {
                for (int i = 0, m = Math.max(toInt(map1.get(t)), toInt(map2.get(t))); i < m; i++) {
                    list.add(t);
                }
            }
        }
        return list;
    }

    /**
     * 集合是否为空
     *
     * @param collection 集合
     *
     * @return 是否为空
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 根据集合返回一个元素计数的 {@link Map}<br> 所谓元素计数就是假如这个集合中某个元素出现了n次，那将这个元素做为key，n做为value<br> 例如：[a,b,c,c,c] 得到：<br> a: 1<br>
     * b: 1<br> c: 3<br>
     *
     * @param collection 集合
     *
     * @return {@link Map}
     */
    public static <T> Map<T, Integer> countMap(Collection<T> collection) {
        HashMap<T, Integer> countMap = new HashMap<>(16);
        Integer count;
        for (T t : collection) {
            count = countMap.get(t);
            countMap.merge(t, 1, (a, b) -> a + b);
        }
        return countMap;
    }

    /**
     * 新建一个HashSet
     *
     * @return HashSet对象
     */
    public static <T> HashSet<T> newHashSet(Collection<T> collection) {
        return new HashSet<>(collection);
    }

    private static int toInt(Integer integer) {
        if (integer == null) {
            return 0;
        }
        return integer;
    }

    /**
     * 多个集合的交集<br> 针对一个集合中存在多个相同元素的情况，计算两个集合中此元素的个数，保留最少的个数<br> 例如：集合1：[a, b, c, c, c]，集合2：[a, b, c, c]<br> 结果：[a, b, c,
     * c]，此结果中只保留了两个c
     *
     * @param coll1      集合1
     * @param coll2      集合2
     * @param otherColls 其它集合
     *
     * @return 并集的集合，返回 {@link ArrayList}
     */
    @SafeVarargs
    public static <T> Collection<T> intersection(final Collection<T> coll1, final Collection<T> coll2, final Collection<T>... otherColls) {
        Collection<T> intersection = intersection(coll1, coll2);
        if (isEmpty(intersection)) {
            return intersection;
        }
        for (Collection<T> coll : otherColls) {
            intersection = intersection(intersection, coll);
            if (isEmpty(intersection)) {
                return intersection;
            }
        }
        return intersection;
    }

    /**
     * 两个集合的交集<br> 针对一个集合中存在多个相同元素的情况，计算两个集合中此元素的个数，保留最少的个数<br> 例如：集合1：[a, b, c, c, c]，集合2：[a, b, c, c]<br> 结果：[a, b, c,
     * c]，此结果中只保留了两个c
     *
     * @param coll1 集合1
     * @param coll2 集合2
     *
     * @return 交集的集合，返回 {@link ArrayList}
     */
    public static <T> Collection<T> intersection(final Collection<T> coll1, final Collection<T> coll2) {
        final ArrayList<T> list = new ArrayList<>();
        if (isNotEmpty(coll1) && isNotEmpty(coll2)) {
            final Map<T, Integer> map1 = countMap(coll1);
            final Map<T, Integer> map2 = countMap(coll2);
            final Set<T> elts = newHashSet(coll2);
            for (T t : elts) {
                for (int i = 0, m = Math.min(toInt(map1.get(t)), toInt(map2.get(t))); i < m; i++) {
                    list.add(t);
                }
            }
        }
        return list;
    }

    /**
     * 集合是否为非空
     *
     * @param collection 集合
     *
     * @return 是否为非空
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * 两个集合的差集<br> 针对一个集合中存在多个相同元素的情况，计算两个集合中此元素的个数，保留两个集合中此元素个数差的个数<br> 例如：集合1：[a, b, c, c, c]，集合2：[a, b, c, c]<br>
     * 结果：[c]，此结果中只保留了一个
     *
     * @param coll1 集合1
     * @param coll2 集合2
     *
     * @return 差集的集合，返回 {@link ArrayList}
     */
    public static <T> Collection<T> disjunction(final Collection<T> coll1, final Collection<T> coll2) {
        final ArrayList<T> list = new ArrayList<>();
        if (isNotEmpty(coll1) && isNotEmpty(coll2)) {
            final Map<T, Integer> map1 = countMap(coll1);
            final Map<T, Integer> map2 = countMap(coll2);
            final Set<T> elts = newHashSet(coll2);
            for (T t : elts) {
                for (int i = 0, m = Math.max(toInt(map1.get(t)), toInt(map2.get(t))) - Math.min(toInt(map1.get(t)), toInt(map2.get(t))); i < m; i++) {
                    list.add(t);
                }
            }
        }
        return list;
    }

    /**
     * 其中一个集合在另一个集合中是否至少包含一个元素，既是两个集合是否至少有一个共同的元素
     *
     * @param coll1 集合1
     * @param coll2 集合2
     *
     * @return 其中一个集合在另一个集合中是否至少包含一个元素
     * @see #intersection
     * @since 2.1
     */
    public static boolean containsAny(final Collection<?> coll1, final Collection<?> coll2) {
        if (isEmpty(coll1) || isEmpty(coll2)) {
            return false;
        }
        if (coll1.size() < coll2.size()) {
            for (Object object : coll1) {
                if (coll2.contains(object)) {
                    return true;
                }
            }
        } else {
            for (Object object : coll2) {
                if (coll1.contains(object)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 以 conjunction 为分隔符将集合转换为字符串
     *
     * @param <T>         被处理的集合
     * @param iterable    {@link Iterable}
     * @param conjunction 分隔符
     *
     * @return 连接后的字符串
     */
    public static <T> String join(Iterable<T> iterable, String conjunction) {
        if (null == iterable) {
            return null;
        }
        return join(iterable.iterator(), conjunction);
    }

    /**
     * 以 conjunction 为分隔符将集合转换为字符串
     *
     * @param <T>         被处理的集合
     * @param iterator    集合
     * @param conjunction 分隔符
     *
     * @return 连接后的字符串
     */
    public static <T> String join(Iterator<T> iterator, String conjunction) {
        if (null == iterator) {
            return null;
        }

        final StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        T item;
        while (iterator.hasNext()) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(conjunction);
            }

            item = iterator.next();
            if (ArrayUtil.isArray(item)) {
                sb.append(ArrayUtil.join(ArrayUtil.wrap(item), conjunction));
            } else if (item instanceof Iterable<?>) {
                sb.append(join((Iterable<?>) item, conjunction));
            } else if (item instanceof Iterator<?>) {
                sb.append(join((Iterator<?>) item, conjunction));
            } else {
                sb.append(item);
            }
        }
        return sb.toString();
    }

    /**
     * 将Set排序（根据Entry的值）
     *
     * @param set 被排序的Set
     *
     * @return 排序后的Set
     */
    public static List<Entry<Long, Long>> sortEntrySetToList(Set<Entry<Long, Long>> set) {
        List<Entry<Long, Long>> list = new LinkedList<>(set);
        list.sort((o1, o2) -> {
            if (o1.getValue() > o2.getValue()) {
                return 1;
            }
            if (o1.getValue() < o2.getValue()) {
                return -1;
            }
            return 0;
        });
        return list;
    }

    /**
     * 新建一个HashMap
     *
     * @return HashMap对象
     */
    public static <T, K> Map<T, K> newHashMap() {
        return new HashMap<>(16);
    }

    /**
     * 新建一个HashMap
     *
     * @param isSorted Map的Key是否有序，有序返回 {@link LinkedHashMap}，否则返回 {@link HashMap}
     *
     * @return HashMap对象
     */
    public static <T, K> Map<T, K> newHashMap(boolean isSorted) {
        return isSorted ? new LinkedHashMap<>() : new HashMap<>(16);
    }

    /**
     * 新建一个HashMap
     *
     * @param size 初始大小，由于默认负载因子0.75，传入的size会实际初始大小为size / 0.75
     *
     * @return HashMap对象
     */
    public static <T, K> Map<T, K> newHashMap(int size) {
        return new HashMap<>((int) (size / 0.75));
    }

    /**
     * 新建一个HashSet
     *
     * @param ts 元素数组
     *
     * @return HashSet对象
     */
    @SafeVarargs
    public static <T> Set<T> newHashSet(T... ts) {
        HashSet<T> set = new HashSet<>(Math.max((int) (ts.length / .75f) + 1, 16));
        Collections.addAll(set, ts);
        return set;
    }

    /**
     * 新建一个HashSet
     *
     * @param isSorted 是否有序，有序返回 {@link LinkedHashSet}，否则返回 {@link HashSet}
     * @param ts       元素数组
     *
     * @return HashSet对象
     */
    @SafeVarargs
    public static <T> Set<T> newHashSet(boolean isSorted, T... ts) {
        int initialCapacity = Math.max((int) (ts.length / .75f) + 1, 16);
        Set<T> set = isSorted ? new LinkedHashSet<>(initialCapacity) : new HashSet<>(initialCapacity);
        Collections.addAll(set, ts);
        return set;
    }

    /**
     * 新建一个HashSet
     *
     * @return HashSet对象
     */
    public static <T> Set<T> newHashSet(boolean isSorted, Collection<T> collection) {
        return isSorted ? new LinkedHashSet<>() : new HashSet<>(collection);
    }

    @SafeVarargs
    public static <T> List<T> newLinkedList(T... values) {
        List<T> list = new LinkedList<>();
        Collections.addAll(list, values);
        return list;
    }

    /**
     * 新建一个ArrayList
     *
     * @param values 数组
     *
     * @return ArrayList对象
     */
    @SafeVarargs
    public static <T> List<T> newArrayList(T... values) {
        List<T> arrayList = new ArrayList<>(values.length);
        Collections.addAll(arrayList, values);
        return arrayList;
    }

    /**
     * 新建一个ArrayList
     *
     * @param collection 集合
     *
     * @return ArrayList对象
     */
    public static <T> List<T> newArrayList(Collection<T> collection) {
        return new ArrayList<>(collection);
    }

    /**
     * 去重集合
     *
     * @param collection 集合
     *
     * @return {@link ArrayList}
     */
    public static <T> ArrayList<T> distinct(Collection<T> collection) {
        if (isEmpty(collection)) {
            return new ArrayList<>();
        } else if (collection instanceof Set) {
            return new ArrayList<>(collection);
        } else {
            return new ArrayList<>(new LinkedHashSet<>(collection));
        }
    }


    // ---------------------------------------------------------------------- isEmpty

    /**
     * 截取集合的部分
     *
     * @param list  被截取的数组
     * @param start 开始位置（包含）
     * @param end   结束位置（不包含）
     *
     * @return 截取后的数组，当开始位置超过最大时，返回null
     */
    public static <T> List<T> sub(Collection<T> list, int start, int end) {
        if (list == null || list.isEmpty()) {
            return null;
        }

        return sub(new ArrayList<>(list), start, end);
    }

    /**
     * 截取数组的部分
     *
     * @param list  被截取的数组
     * @param start 开始位置（包含）
     * @param end   结束位置（不包含）
     *
     * @return 截取后的数组，当开始位置超过最大时，返回null
     */
    public static <T> List<T> sub(List<T> list, int start, int end) {
        if (list == null || list.isEmpty()) {
            return null;
        }

        if (start < 0) {
            start = 0;
        }
        if (end < 0) {
            end = 0;
        }

        if (start > end) {
            int tmp = start;
            start = end;
            end = tmp;
        }

        final int size = list.size();
        if (end > size) {
            if (start >= size) {
                return null;
            }
            end = size;
        }

        return list.subList(start, end);
    }

    /**
     * 对集合按照指定长度分段，每一个段为单独的集合，返回这个集合的列表
     *
     * @param collection 集合
     * @param size       每个段的长度
     *
     * @return 分段列表
     */
    public static <T> List<List<T>> split(Collection<T> collection, int size) {
        final List<List<T>> result = new ArrayList<>();

        ArrayList<T> subList = new ArrayList<>(size);
        for (T t : collection) {
            if (subList.size() > size) {
                result.add(subList);
                subList = new ArrayList<>(size);
            }
            subList.add(t);
        }
        result.add(subList);
        return result;
    }

    /**
     * Iterable是否为空
     *
     * @param iterable Iterable对象
     *
     * @return 是否为空
     */
    public static boolean isEmpty(Iterable<?> iterable) {
        return null == iterable || isEmpty(iterable.iterator());
    }

    /**
     * Iterator是否为空
     *
     * @param iterator Iterator对象
     *
     * @return 是否为空
     */
    public static boolean isEmpty(Iterator<?> iterator) {
        return null == iterator || !iterator.hasNext();
    }


    // ---------------------------------------------------------------------- isNotEmpty

    /**
     * Enumeration是否为空
     *
     * @param enumeration {@link Enumeration}
     *
     * @return 是否为空
     */
    public static boolean isEmpty(Enumeration<?> enumeration) {
        return null == enumeration || !enumeration.hasMoreElements();
    }

    /**
     * Map是否为非空
     *
     * @param map 集合
     *
     * @return 是否为非空
     */
    public static <T> boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * Map是否为空
     *
     * @param map 集合
     *
     * @return 是否为空
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * Iterable是否为空
     *
     * @param iterable Iterable对象
     *
     * @return 是否为空
     */
    public static boolean isNotEmpty(Iterable<?> iterable) {
        return null != iterable && isNotEmpty(iterable.iterator());
    }

    /**
     * Iterator是否为空
     *
     * @param iterator Iterator对象
     *
     * @return 是否为空
     */
    public static boolean isNotEmpty(Iterator<?> iterator) {
        return null != iterator && iterator.hasNext();
    }


    // ---------------------------------------------------------------------- zip

    /**
     * Enumeration是否为空
     *
     * @param enumeration {@link Enumeration}
     *
     * @return 是否为空
     */
    public static boolean isNotEmpty(Enumeration<?> enumeration) {
        return null != enumeration && enumeration.hasMoreElements();
    }

    /**
     * 映射键值（参考Python的zip()函数）<br> 例如：<br> keys = a,b,c,d<br> values = 1,2,3,4<br> delimiter = , 则得到的Map是 {a=1, b=2, c=3,
     * d=4}<br> 如果两个数组长度不同，则只对应最短部分
     *
     * @param keys   键列表
     * @param values 值列表
     *
     * @return Map
     */
    public static Map<String, String> zip(String keys, String values, String delimiter) {
        return ArrayUtil.zip(StringUtil.split(keys, delimiter), StringUtil.split(values, delimiter));
    }

    /**
     * 映射键值（参考Python的zip()函数）<br> 例如：<br> keys = [a,b,c,d]<br> values = [1,2,3,4]<br> 则得到的Map是 {a=1, b=2, c=3, d=4}<br>
     * 如果两个数组长度不同，则只对应最短部分
     *
     * @param keys   键列表
     * @param values 值列表
     *
     * @return Map
     */
    public static <T, K> Map<T, K> zip(Collection<T> keys, Collection<K> values) {
        if (isEmpty(keys) || isEmpty(values)) {
            return null;
        }

        final List<T> keyList = new ArrayList<T>(keys);
        final List<K> valueList = new ArrayList<K>(values);

        final int size = Math.min(keys.size(), values.size());
        final Map<T, K> map = new HashMap<>((int) (size / 0.75));
        for (int i = 0; i < size; i++) {
            map.put(keyList.get(i), valueList.get(i));
        }

        return map;
    }

    /**
     * 将Entry集合转换为HashMap
     *
     * @param entryCollection entry集合
     *
     * @return Map
     */
    public static <T, K> HashMap<T, K> toMap(Collection<Entry<T, K>> entryCollection) {
        HashMap<T, K> map = new HashMap<>(16);
        for (Entry<T, K> entry : entryCollection) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }

    /**
     * 将集合转换为排序后的TreeSet
     *
     * @param collection 集合
     * @param comparator 比较器
     *
     * @return treeSet
     */
    public static <T> TreeSet<T> toTreeSet(Collection<T> collection, Comparator<T> comparator) {
        final TreeSet<T> treeSet = new TreeSet<>(comparator);
        treeSet.addAll(collection);
        return treeSet;
    }

    /**
     * 排序集合
     *
     * @param collection 集合
     * @param comparator 比较器
     *
     * @return treeSet
     */
    public static <T> List<T> sort(Collection<T> collection, Comparator<T> comparator) {
        List<T> list = new ArrayList<>(collection);
        list.sort(comparator);
        return list;
    }

    /**
     * Iterator转换为Enumeration Adapt the specified <code>Iterator</code> to the <code>Enumeration</code> interface.
     *
     * @param iter Iterator
     *
     * @return Enumeration
     */
    public static <E> Enumeration<E> asEnumeration(final Iterator<E> iter) {
        return new Enumeration<E>() {
            @Override
            public boolean hasMoreElements() {
                return iter.hasNext();
            }

            @Override
            public E nextElement() {
                return iter.next();
            }
        };
    }

    /**
     * Enumeration转换为Iterator<br> Adapt the specified <code>Enumeration</code> to the <code>Iterator</code> interface
     *
     * @param e Enumeration
     *
     * @return Iterator
     */
    public static <E> Iterator<E> asIterator(final Enumeration<E> e) {
        return new Iterator<E>() {
            @Override
            public boolean hasNext() {
                return e.hasMoreElements();
            }

            @Override
            public E next() {
                return e.nextElement();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    /**
     * 加入全部
     *
     * @param collection 被加入的集合 {@link Collection}
     * @param iterable   要加入的内容{@link Iterable}
     *
     * @return 原集合
     */
    public static <T> Collection<T> addAll(Collection<T> collection, Iterable<T> iterable) {
        return addAll(collection, iterable.iterator());
    }

    /**
     * 加入全部
     *
     * @param collection 被加入的集合 {@link Collection}
     * @param iterator   要加入的{@link Iterator}
     *
     * @return 原集合
     */
    public static <T> Collection<T> addAll(Collection<T> collection, Iterator<T> iterator) {
        if (null != collection && null != iterator) {
            while (iterator.hasNext()) {
                collection.add(iterator.next());
            }
        }
        return collection;
    }

    /**
     * 加入全部
     *
     * @param collection  被加入的集合 {@link Collection}
     * @param enumeration 要加入的内容{@link Enumeration}
     *
     * @return 原集合
     */
    public static <T> Collection<T> addAll(Collection<T> collection, Enumeration<T> enumeration) {
        if (null != collection && null != enumeration) {
            while (enumeration.hasMoreElements()) {
                collection.add(enumeration.nextElement());
            }
        }
        return collection;
    }

    public static <T> List<T> reverse(List<T> list) {
        if (CollectionUtil.isEmpty(list)) {
            return list;
        }

        List<T> res = new ArrayList<>();
        for (int i = list.size(); i > 0; i--) {
            T o = list.get(i - 1);
            res.add(o);
        }

        return res;
    }

    /**
     * 将另一个列表中的元素加入到列表中，如果列表中已经存在此元素则忽略之
     *
     * @param list      列表
     * @param otherList 其它列表
     *
     * @return 此列表
     */
    public static <T> List<T> addAllIfNotContains(List<T> list, List<T> otherList) {
        for (T t : otherList) {
            if (!list.contains(t)) {
                list.add(t);
            }
        }
        return list;
    }

    /**
     * 获取集合的第一个元素
     *
     * @param iterable {@link Iterable}
     *
     * @return 第一个元素
     * @since 3.0.1
     */
    public static <T> T getFirst(Iterable<T> iterable) {
        if (null != iterable) {
            return getFirst(iterable.iterator());
        }
        return null;
    }


    //------------------------------------------------------------------------------------------------- forEach

    /**
     * 获取集合的第一个元素
     *
     * @param iterator {@link Iterator}
     *
     * @return 第一个元素
     * @since 3.0.1
     */
    public static <T> T getFirst(Iterator<T> iterator) {
        if (null != iterator && iterator.hasNext()) {
            return iterator.next();
        }
        return null;
    }

    /**
     * 循环遍历 {@link Iterator}，使用{@link Consumer} 接受遍历的每条数据，并针对每条数据做处理
     *
     * @param iterator {@link Iterator}
     * @param consumer {@link Consumer} 遍历的每条数据处理器
     */
    public static <T> void forEach(Iterator<T> iterator, Consumer<T> consumer) {
        int index = 0;
        while (iterator.hasNext()) {
            consumer.accept(iterator.next(), index);
            index++;
        }
    }

    /**
     * 循环遍历 {@link Enumeration}，使用{@link Consumer} 接受遍历的每条数据，并针对每条数据做处理
     *
     * @param enumeration {@link Enumeration}
     * @param consumer    {@link Consumer} 遍历的每条数据处理器
     */
    public static <T> void forEach(Enumeration<T> enumeration, Consumer<T> consumer) {
        int index = 0;
        while (enumeration.hasMoreElements()) {
            consumer.accept(enumeration.nextElement(), index);
            index++;
        }
    }

    /**
     * 循环遍历Map，使用{@link KVConsumer} 接受遍历的每条数据，并针对每条数据做处理
     *
     * @param map        {@link Map}
     * @param kvConsumer {@link KVConsumer} 遍历的每条数据处理器
     */
    public static <K, V> void forEach(Map<K, V> map, KVConsumer<K, V> kvConsumer) {
        int index = 0;
        for (Entry<K, V> entry : map.entrySet()) {
            kvConsumer.accept(entry.getKey(), entry.getValue(), index);
            index++;
        }
    }

    /**
     * 针对一个参数做相应的操作
     *
     * @param <T> 处理参数类型
     *
     * @author Looly
     */
    public interface Consumer<T> {

        /**
         * 接受并处理一个参数
         *
         * @param value 参数值
         * @param index 参数在集合中的索引
         */
        void accept(T value, int index);

    }

    /**
     * 针对两个参数做相应的操作，例如Map中的KEY和VALUE
     *
     * @param <K> KEY类型
     * @param <V> VALUE类型
     *
     * @author Looly
     */
    public interface KVConsumer<K, V> {

        /**
         * 接受并处理一对参数
         *
         * @param key   键
         * @param value 值
         * @param index 参数在集合中的索引
         */
        void accept(K key, V value, int index);

    }

}

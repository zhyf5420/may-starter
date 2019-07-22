import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * create on 2019/4/11 0011
 *
 * @author zhyf
 */
public class Test {

    public static void main(String[] args) {
        Multimap<Integer, Integer> multimap = HashMultimap.create();
        multimap.put(1, 1);
        multimap.put(1, 2);
        multimap.put(1, 3);
        multimap.put(1, 1);
        multimap.put(2, 1);

        System.out.println(multimap.keySet().size());

    }

}

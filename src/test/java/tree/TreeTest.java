package tree;

import starter.base.utils.CollectionUtil;
import starter.base.utils.tree.TreeUtil;

import java.util.List;

/**
 * Created on 2019/5/15.
 *
 * @author zhyf
 */
public class TreeTest {

    public static void main(String[] args) {
        MyTreeBean tree = new MyTreeBean(1L, null);
        MyTreeBean tree1 = new MyTreeBean(2L, 1L);
        MyTreeBean tree2 = new MyTreeBean(3L, 1L);
        MyTreeBean tree3 = new MyTreeBean(4L, 1L);
        MyTreeBean tree4 = new MyTreeBean(5L, 4L);

        List<MyTreeBean> list = CollectionUtil.newArrayList(
                tree,
                tree1,
                tree2,
                tree3,
                tree4
        );

        List<MyTreeBean> treeList = TreeUtil.getTree(list);
        System.out.println(treeList.size());

    }

}

package starter.base.utils.tree;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

/**
 * Created on 2018.7.17.
 *
 * @author zhyf
 */
public class TreeUtil {

    /**
     * 返回排序好的树形结构 一棵或多棵树
     *
     * @param treeItemList 待排序的树节点列表
     */
    public static <T extends Tree> List<T> getTree(List<T> treeItemList) {
        treeItemList.sort(Comparator.comparingInt(Tree::getOrderNum));

        Set<Long> idSet = treeItemList.stream().map(Tree::getId).collect(toSet());
        List<T> rootList = treeItemList.stream()
                                       .filter(treeItem -> !idSet.contains(treeItem.getParentId()))
                                       .collect(toList());

        treeItemList.removeAll(rootList);

        rootList.forEach(rootItem -> {
            rootItem.setLayer(0);
            rootItem.setParentId(null);
            addChildren(rootItem, treeItemList, 1);
        });

        return rootList;
    }

    /**
     * 给父节点添加子节点
     *
     * @param parentItem   父节点
     * @param treeItemList 节点列表
     * @param layer        子节点层级
     */
    @SuppressWarnings("unchecked")
    private static <T extends Tree> void addChildren(T parentItem, List<T> treeItemList, int layer) {
        //找出parentItem的子节点列表
        List<T> childList = treeItemList.stream()
                                        .filter(t -> t.getParentId().equals(parentItem.getId()))
                                        .collect(toList());

        //去除parentItem的子节点列表
        treeItemList.removeAll(childList);

        childList.forEach(treeItem -> {
            treeItem.setLayer(layer);
            treeItem.setParentId(parentItem.getId());
            addChildren(treeItem, treeItemList, layer + 1);
        });

        parentItem.setChildren(childList);
    }

}

package starter.base.utils.tree;

import java.util.List;

/**
 * 树形结构定义
 *
 * @author zhyf
 */
public interface Tree<T extends Tree> {

    Long getId();

    Long getParentId();

    void setParentId(Long parentId);

    void setLayer(int layer);

    int getOrderNum();

    void setChildren(List<T> children);

}

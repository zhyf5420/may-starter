package tree;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import starter.base.utils.tree.Tree;

import java.util.List;

/**
 * Created on 2019/5/15.
 *
 * @author zhyf
 */
@Getter
@Setter
@NoArgsConstructor
public class MyTreeBean implements Tree<MyTreeBean> {

    private Long id;

    private Long parentId;

    private int layer;

    private int orderNum;

    private List<MyTreeBean> children;

    public MyTreeBean(Long id, Long parentId) {
        this.id = id;
        this.parentId = parentId;
    }

}

package kz.maks.core.shared.models;

import java.io.Serializable;
import java.util.List;

public class TreeNode implements ITreeNode<TreeNode>, Serializable {
    public final Long id;
    public final String name;
    public final List<TreeNode> children;

    public TreeNode(Long id, String name, List<TreeNode> children) {
        this.id = id;
        this.name = name;
        this.children = children;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<TreeNode> getChildren() {
        return children;
    }

    @Override
    public TreeNode getParent() {
        throw new RuntimeException("Not supported yet");
    }

    @Override
    public String toString() {
        return name;
    }
}

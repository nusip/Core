package kz.maks.core.shared.models;

import java.util.List;

public interface ITreeNode<T extends ITreeNode> {

    Long getId();

    String getName();

    List<T> getChildren();

    T getParent();

}

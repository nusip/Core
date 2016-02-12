package kz.maks.core.back;

import kz.maks.core.shared.models.ITreeNode;

public class TreeEntities {

    /**
     * this array is filled with Entity classes implementing ITreeNode
     * from DIServerCore using reflection
     */
    private static Class<? extends ITreeNode>[] trees;

    public static Class<? extends ITreeNode>[] get() {
        return trees;
    }

}

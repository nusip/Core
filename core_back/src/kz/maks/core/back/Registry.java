package kz.maks.core.back;

import kz.maks.core.back.entities.AbstractBaseEntity;
import kz.maks.core.shared.models.ICombo;
import kz.maks.core.shared.models.ITreeNode;

public class Registry {

    /**
     * this array is filled with Entity classes implementing ITreeNode
     * from DIServerCore
     */
    private static Class<? extends ITreeNode>[] trees;

    static void setTrees(Class<? extends ITreeNode>[] trees) {
        Registry.trees = trees;
    }

    public static Class<? extends ITreeNode>[] trees() {
        return trees;
    }

    /**
     * this field is assigned with name of entity extending AbstractUserEntity
     * from DIServerCore
     */
    private static Class<? extends AbstractBaseEntity> userEntityName;

    static void setUserEntityName(Class<? extends AbstractBaseEntity> userEntityName) {
        Registry.userEntityName = userEntityName;
    }

    public static Class<? extends AbstractBaseEntity> userEntityName() {
        return userEntityName;
    }

    /**
     * this array is filled with Entity classes implementing ICombo
     * from DIServerCore
     */
    private static Class<? extends ICombo>[] combos;

    static void setCombos(Class<? extends ICombo>[] combos) {
        Registry.combos = combos;
    }

    public static Class<? extends ICombo>[] combos() {
        return combos;
    }
}

package kz.maks.core.front;

import kz.maks.core.front.services.CoreRemotes;
import kz.maks.core.shared.models.Combo;
import kz.maks.core.shared.models.ICombo;
import kz.maks.core.shared.models.ITreeNode;
import kz.maks.core.shared.remotes.CoreRemote;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cache {

    private static final Map<String, Object> cache = new HashMap<>();

    public static void cache() {
        try {
            cacheTrees();
            cacheCombos();

        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private static void cacheCombos() throws RemoteException {
        Map<String, List<ICombo>> combos = CoreRemotes.CORE_REMOTE().getCombos();

        for (String comboName : combos.keySet()) {
            cache.put(comboName, combos.get(comboName));
        }
    }

    public static void cacheTrees() throws RemoteException {
        Map<String, List<ITreeNode>> treeNodes = CoreRemotes.CORE_REMOTE().getTrees();

        for (String treeName : treeNodes.keySet()) {
            cache.put(treeName, treeNodes.get(treeName));
        }
    }

    public static List<ITreeNode> getTree(String treeName) {
        return (List<ITreeNode>) cache.get(treeName);
    }

    public static List<Combo> getCombo(String comboName) {
        return (List<Combo>) cache.get(comboName);
    }

}

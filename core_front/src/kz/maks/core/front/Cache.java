package kz.maks.core.front;

import kz.maks.core.front.services.CoreRemotes;
import kz.maks.core.shared.models.ITreeNode;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cache {

    private static final Map<String, Object> cache = new HashMap<>();

    public static void cacheTrees() {
        try {
            Map<String, List<ITreeNode>> treeNodes = CoreRemotes.CORE_REMOTE().getTrees();

            for (String treeName : treeNodes.keySet()) {
                cache.put(treeName, treeNodes.get(treeName));
            }

        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<ITreeNode> getTree(String treeName) {
        return (List<ITreeNode>) cache.get(treeName);
    }

}

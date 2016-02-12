package kz.maks.core.front.ui;

import kz.maks.core.front.Cache;
import kz.maks.core.shared.models.Combo;
import kz.maks.core.shared.models.ITreeNode;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tree {
    public final JTree ui;

    private final DefaultMutableTreeNode rootNode;// = new DefaultMutableTreeNode();
    private final Map<Long, DefaultMutableTreeNode> allNodes = new HashMap<>();

    public Tree(String treeName) {
        List<ITreeNode> treeNodes = Cache.getTree(treeName);

//        for (ITreeNode treeNode : treeNodes) {
//            rootNode.add(convertTreeNode(treeNode, allNodes));
//        }

        rootNode = convertTreeNode(treeNodes.get(0), allNodes);

        ui = new JTree(rootNode);
        ui.setRootVisible(false);
        ui.setShowsRootHandles(true);
    }

    private DefaultMutableTreeNode convertTreeNode(ITreeNode treeNode, Map<Long, DefaultMutableTreeNode> allNodes) {
        DefaultMutableTreeNode defaultMutableTreeNode = new DefaultMutableTreeNode();
        allNodes.put(treeNode.getId(), defaultMutableTreeNode);
        Combo userObject = new Combo(treeNode.getId(), treeNode.getName());
        defaultMutableTreeNode.setUserObject(userObject);

        for (Object child : treeNode.getChildren()) {
            defaultMutableTreeNode.add(convertTreeNode((ITreeNode) child, allNodes));
        }

        return defaultMutableTreeNode;
    }

    public Combo getSelected() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) ui.getLastSelectedPathComponent();
        return node != null ? (Combo) node.getUserObject() : null;
    }

    public void setSelected(Long id) {
        ui.clearSelection();
        collapseAll();

        if (id == null) {
            return;
        }

        DefaultMutableTreeNode defaultMutableTreeNode = allNodes.get(id);
        TreePath selectionPath = new TreePath(defaultMutableTreeNode.getPath());
        ui.setSelectionPath(selectionPath);
        ui.scrollPathToVisible(selectionPath);
    }

    private void collapseAll() {
        TreePath rootPath = new TreePath(ui.getModel().getRoot());
        collapse(rootPath, true);
    }

    private void collapse(TreePath treePath, boolean isRoot) {
        TreeNode lastNode = (TreeNode) treePath.getLastPathComponent();

        for (int i = 0; i < ui.getModel().getChildCount(lastNode); i++) {
            Object child = ui.getModel().getChild(lastNode,i);
            TreePath pathToChild = treePath.pathByAddingChild(child);
            collapse(pathToChild, false);
        }

        if (!isRoot) {
            ui.collapsePath(treePath);
        }
    }

    public Combo getById(Long id) {
        DefaultMutableTreeNode defaultMutableTreeNode = allNodes.get(id);
        return (Combo) defaultMutableTreeNode.getUserObject();
    }

}

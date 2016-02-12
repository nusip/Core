package kz.maks.core.back.services.impl;

import kz.maks.core.back.TreeEntities;
import kz.maks.core.back.annotations.Service;
import kz.maks.core.back.services.CoreService;
import kz.maks.core.shared.Utils;
import kz.maks.core.shared.models.ITreeNode;
import kz.maks.core.shared.models.TreeNode;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.reflections.ReflectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hibernate.criterion.Restrictions.eq;
import static org.hibernate.criterion.Restrictions.isNull;
import static org.hibernate.criterion.Restrictions.or;

@Service
public class CoreServiceImpl extends AbstractServiceImpl implements CoreService {

    @Override
    public Map<String, List<ITreeNode>> getTrees() {
        Map<String, List<ITreeNode>> trees = new HashMap<>();

        for (Class treeEntityClass : TreeEntities.get()) {
            List<ITreeNode> tree = getTree(treeEntityClass);
            trees.put(treeEntityClass.getSimpleName(), tree);
        }

        return trees;
    }

    private List<ITreeNode> getTree(Class<? extends ITreeNode> treeEntityClass) {
        Criteria criteria = session().createCriteria(treeEntityClass);
        criteria.add(isNull("parent"));

        if (Utils.hasField(treeEntityClass, "deleted")) {
            criteria.add(eq("deleted", false));
        }

        List<ITreeNode> treeNodes = (List<ITreeNode>) criteria.list();

        List<ITreeNode> copies = new ArrayList<>();

        for (ITreeNode treeNode : treeNodes) {
            // copy entity class implementation of ITreeNode to clean simple one
            ITreeNode copy = getCopy(treeNode);
            copies.add(copy);
        }

        return copies;
    }

    private TreeNode getCopy(ITreeNode treeNode) {
        final List<TreeNode> children = new ArrayList<>();

        for (Object child : treeNode.getChildren()) {
            children.add(getCopy((ITreeNode) child));
        }

        return new TreeNode(treeNode.getId(), treeNode.getName(), children);
    }

}

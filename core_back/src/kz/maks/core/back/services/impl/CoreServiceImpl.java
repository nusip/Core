package kz.maks.core.back.services.impl;

import kz.maks.core.back.Registry;
import kz.maks.core.back.annotations.Service;
import kz.maks.core.back.entities.AbstractUserEntity;
import kz.maks.core.back.services.CoreService;
import kz.maks.core.shared.PasswordUtils;
import kz.maks.core.shared.Utils;
import kz.maks.core.shared.models.Combo;
import kz.maks.core.shared.models.ICombo;
import kz.maks.core.shared.models.ITreeNode;
import kz.maks.core.shared.models.TreeNode;
import org.hibernate.Criteria;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static kz.maks.core.back.entities.AbstractUserEntity.CREDENTIALS_FIELD;
import static kz.maks.core.back.entities.AbstractUserEntity.IDENTIFIER_FIELD;
import static org.hibernate.criterion.Restrictions.eq;
import static org.hibernate.criterion.Restrictions.isNull;

@Service
public class CoreServiceImpl extends AbstractServiceImpl implements CoreService {

    @Override
    public Map<String, List<ITreeNode>> getTrees() {
        Map<String, List<ITreeNode>> trees = new HashMap<>();

        for (Class treeEntityClass : Registry.trees()) {
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

    @Override
    public Map<String, List<ICombo>> getCombos() {
        Map<String, List<ICombo>> comboMap = new HashMap<>();

        for (Class comboEntityClass : Registry.combos()) {
            List<ICombo> comboList = getCombo(comboEntityClass);
            comboMap.put(comboEntityClass.getSimpleName(), comboList);
        }

        return comboMap;
    }

    private List<ICombo> getCombo(Class<? extends ICombo> comboEntityClass) {
        List<ICombo> combos = session().createCriteria(comboEntityClass).list();
        List<ICombo> copies = new ArrayList<>();

        for (ICombo combo : combos) {
            Combo copy = new Combo(combo.getId(), combo.title());
            copies.add(copy);
        }

        return copies;
    }

}

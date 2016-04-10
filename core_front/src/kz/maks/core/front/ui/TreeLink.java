package kz.maks.core.front.ui;

import kz.maks.core.front.annotations.TreeName;
import kz.maks.core.front.validation.AbstractFieldValidator;
import kz.maks.core.shared.Utils;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TreeLink extends AbstractFieldValidator<Long> {
    public final JButton ui;

    private Long value;
    private final LinkButton linkButton;
    private final Tree tree;

    public TreeLink(Frame parent, FormField formField) {
        super(formField);
        String title = "Выберите " + formField.getTitle().toLowerCase();
        linkButton = new LinkButton(title);
        linkButton.ui.setHorizontalAlignment(SwingConstants.LEFT);
        ui = linkButton.ui;

        TreeName treeName = Utils.getField(formField.getClass(), formField.name()).getAnnotation(TreeName.class);
        tree = new Tree(treeName.value());
        final BasicDialog dialog = new BasicDialog(parent, tree.ui, title);
        dialog.ui.setSize(250, dialog.ui.getHeight());

        tree.ui.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                dialog.btnOk.setEnabled(true);
            }
        });

        dialog.btnOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                set(tree.getSelected().getId());
                dialog.ui.setVisible(false);
            }
        });

        linkButton.ui.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tree.setSelected(get());
                dialog.btnOk.setEnabled(get() != null);
                dialog.ui.setVisible(true);
            }
        });

    }

    @Override
    public void set(Long val) {
        this.value = val;
        linkButton.setText(val != null ? tree.getById(val).getTitle() : null);
    }

    @Override
    public Long get() {
        return value;
    }

}

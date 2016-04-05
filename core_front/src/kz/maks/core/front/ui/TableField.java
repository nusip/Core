package kz.maks.core.front.ui;

import kz.maks.core.front.FrontUtils;
import kz.maks.core.front.validation.AbstractFieldValidator;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class TableField<T> extends AbstractFieldValidator<List<T>> {
    public final Box ui = Box.createHorizontalBox();
    public final Table<T> table;
    private final BtnCRUDPanel btnCRUDPanel = new BtnCRUDPanel();

    public TableField(FormField formField, IColumn<T>[] columns) {
        super(formField);
        this.table = new Table<>(columns, true, false);
        JScrollPane scrollPane = new JScrollPane(table.ui);
        FrontUtils.setPreferredHeight(scrollPane, 100);
        FrontUtils.setPreferredWidth(scrollPane, 100);
        ui.add(scrollPane);
        {
            btnCRUDPanel.btnEdit.setVisible(false);
            btnCRUDPanel.btnDelete.setEnabled(false);
            ui.add(btnCRUDPanel.ui);

            btnCRUDPanel.btnAdd.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    table.addEmptyRow();
                }
            });

            btnCRUDPanel.btnDelete.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    table.removeSelected();
                }
            });

            table.ui.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    resetEditDeleteButtons();
                }
            });
        }
    }

    private void resetEditDeleteButtons() {
        boolean isSelected = table.getSelected() != null;
        btnCRUDPanel.btnEdit.setEnabled(isSelected);
        btnCRUDPanel.btnDelete.setEnabled(isSelected);
    }

    @Override
    public List<T> get() {
        return table.get();
    }

    @Override
    public void set(List<T> val) {
        table.set(val);
    }
}

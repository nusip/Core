package kz.maks.core.front.ui;

import com.google.common.base.Joiner;
import kz.maks.core.shared.Utils;
import kz.maks.core.shared.models.Accessor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.util.ArrayList;
import java.util.List;

public class Table<T> implements Accessor<List<T>> {

    public final JTable ui;

    private final DefaultTableModel model = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final IColumn<T>[] columns;
    private final Class<T> clazz;
    private final List<T> rows = new ArrayList<>();

    public Table(IColumn<T>[] columns) {
        this.columns = columns;
        clazz = columns[0].tableClass();

        ui = new JTable();
        ui.setFillsViewportHeight(true);
        ui.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        model.setColumnIdentifiers(columns);

        ui.setModel(model);
    }

    public T getSelected() {
        int selectedRowIndex = ui.getSelectedRow();

        if (selectedRowIndex > -1)
            return rows.get(selectedRowIndex);

        return null;
    }

    public void removeSelected() {
        int selectedRowIndex = ui.getSelectedRow();
        rows.remove(selectedRowIndex);
        ui.getSelectionModel().clearSelection();
        model.removeRow(selectedRowIndex);
    }

    @Override
    public List<T> get() {
        return rows;
    }

    @Override
    public void set(List<T> list) {
        rows.clear();
        rows.addAll(list);
        ui.getSelectionModel().clearSelection();

        model.setRowCount(0);

        for (T t : list) {
            addRow(t);
        }
    }

    private void addRow(T t) {
        List<String> cells = new ArrayList<>();

        for (IColumn<T> column : columns) {
            Object value = Utils.invokeMethod(t, clazz, Utils.getterName(column.name()));
            String strValue = getStringValue(value);
            cells.add(strValue);
        }

        model.addRow(cells.toArray());
    }

    private String getStringValue(Object value) {
        if (value == null) {
            return null;

        } else if (value instanceof Boolean) {
            return value == true ? "Да" : "Нет";

        } else if (value instanceof List) {
            List<String> strList = new ArrayList<>();
            List objList = (List) value;

            for (Object obj : objList) {
                strList.add(getStringValue(obj));
            }

            return Joiner.on("\n").join(strList);
        }

        return value.toString();
    }


}

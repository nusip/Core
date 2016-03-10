package kz.maks.core.front.ui;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
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

    private static final String ROW_NUMBER_COL_NAME = "rowNumber";

    public Table(IColumn<T>[] columns) {
        this.columns = columns;
        clazz = columns[0].tableClass();

        ui = new JTable();
        ui.setFillsViewportHeight(true);
        ui.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        model.setColumnIdentifiers(getColsWithRowNumbering(columns));

        ui.setModel(model);
    }

    private Object[] getColsWithRowNumbering(IColumn<T>[] columns) {
        ArrayList<IColumn<T>> cols = Lists.newArrayList(columns);
        cols.add(0, new IColumn<T>() {
            @Override
            public Class<T> tableClass() {
                return null;
            }

            @Override
            public String name() {
                return ROW_NUMBER_COL_NAME;
            }

            @Override
            public String title() {
                return "№";
            }

            @Override
            public String toString() {
                return title();
            }
        });
        return cols.toArray();
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

        int rowNumber = 1;

        for (T t : list) {
            addRow(t, rowNumber++);
        }
    }

    private void addRow(T t, int rowNumber) {
        List<String> cells = new ArrayList<>();

        cells.add(rowNumber + "");

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

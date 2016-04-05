package kz.maks.core.front.ui;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import kz.maks.core.shared.Utils;
import kz.maks.core.shared.models.Accessor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class Table<T> implements Accessor<List<T>> {

    public final JTable ui;

    private final DefaultTableModel model;
    private final IColumn<T>[] columns;
    private final boolean isCellEditable;
    private final boolean withNumberColumn;
    private final Class<T> clazz;
    private final List<T> rows = new ArrayList<>();
    private int nextLastRowNumber = 1;

    private static final String ROW_NUMBER_COL_NAME = "rowNumber";

    public Table(IColumn<T>[] columns) {
        this(columns, false, true);
    }

    public Table(IColumn<T>[] columns, final boolean isCellEditable, boolean withNumberColumn) {
        this.columns = columns;
        this.isCellEditable = isCellEditable;
        this.withNumberColumn = withNumberColumn;
        clazz = columns[0].tableClass();

        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return isCellEditable;
            }
        };

        ui = new JTable();
        ui.setFillsViewportHeight(true);
        ui.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        model.setColumnIdentifiers(withNumberColumn ? getColsWithRowNumbering(columns) : columns);

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
            return getRow(selectedRowIndex);

        return null;
    }

    private T getRow(int rowIndex) {
        if (!isCellEditable) return rows.get(rowIndex);

        T obj = Utils.newInstance(clazz);

        for (int colIndex = withNumberColumn ? 1 : 0; colIndex < columns.length; colIndex++) {
            Object value = ui.getValueAt(rowIndex, colIndex);
            Utils.invokeMethod(obj, clazz, Utils.setterName(columns[colIndex].name()), value);
        }

        return obj;
    }

    public void removeSelected() {
        int selectedRowIndex = ui.getSelectedRow();
        rows.remove(selectedRowIndex);
        ui.getSelectionModel().clearSelection();
        model.removeRow(selectedRowIndex);
    }

    @Override
    public List<T> get() {
        List<T> data = new ArrayList<>();

        for (int i = 0; i < ui.getRowCount(); i++) {
            T row = getRow(i);

            if (row == null) continue;

            data.add(row);
        }

        return data;
    }

    @Override
    public void set(List<T> list) {
        rows.clear();
        list = list == null ? new ArrayList<T>() : list;
        ui.getSelectionModel().clearSelection();

        model.setRowCount(0);

        nextLastRowNumber = 1;

        for (T t : list) {
            addRow(t);
        }
    }

    public void addEmptyRow() {
        addRow(Utils.newInstance(clazz));
    }

    private void addRow(T t) {
        rows.add(t);

        List<String> cells = new ArrayList<>();

        if (withNumberColumn) {
            cells.add(nextLastRowNumber + "");
        }

        nextLastRowNumber++;

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

        } else if (value instanceof Date) {
            return new SimpleDateFormat(Utils.DATE_FORMAT_FULL).format(value);

        } else if (value instanceof Double) {
            return new BigDecimal((Double) value).toString();

        } else if (value instanceof Boolean) {
            return Boolean.TRUE.equals(value) ? "Да" : "Нет";

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

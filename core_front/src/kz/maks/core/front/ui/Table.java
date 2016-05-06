package kz.maks.core.front.ui;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import kz.maks.core.front.annotations.ComboName;
import kz.maks.core.front.annotations.Hidden;
import kz.maks.core.shared.Utils;
import kz.maks.core.shared.models.Accessor;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import java.awt.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static kz.maks.core.shared.Utils.getField;

public class Table<T> implements Accessor<List<T>> {
    public final JTable ui;
    public final DefaultTableModel tableModel;

    private final DefaultTableColumnModel tableColumnModel;
    private final List<IColumn<T>> columns;
    private final boolean withNumberColumn;
    private final Class<T> clazz;
    private int nextLastRowNumber = 1;

    public Table(IColumn<T>[] columns) {
        this(columns, true);
    }

    public Table(final IColumn<T>[] cols, final boolean withNumberColumn) {
        this.columns = Lists.newArrayList(cols);
        this.withNumberColumn = withNumberColumn;
        clazz = cols[0].tableClass();

        tableColumnModel = new DefaultTableColumnModel();

        ui = new JTable();
        ui.setFillsViewportHeight(true);
        ui.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        int modelIndex = 0;

        if (withNumberColumn) {
            Column rowNumberColumn = new Column(null, "rowNumber", "№", false, 40);
            columns.add(0, rowNumberColumn);
        }

        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return columns.get(column).isEditable();
            }
        };

        tableModel.setColumnIdentifiers(columns.toArray());

        for (; modelIndex < columns.size(); modelIndex++) {
            IColumn column = columns.get(modelIndex);

            if (!(column.getClass().isEnum() &&
                    getField(column.getClass(), column.name()).isAnnotationPresent(Hidden.class))) {
                addTableColumn(column, modelIndex);
            }
        }

        ui.setModel(tableModel);
        ui.setColumnModel(tableColumnModel);
    }

    private TableColumn addTableColumn(IColumn column, int modelIndex) {
        TableColumn tableColumn = new TableColumn();
        tableColumn.setModelIndex(modelIndex);
        tableColumn.setIdentifier(column.name());
        tableColumn.setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                String sValue = getDisplayValue(value);
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, sValue, isSelected, hasFocus, row, column);
                if (sValue != null) {
                    String sWidth = sValue.length() > 50 ? " width='250px'" : "";
                    label.setToolTipText("<HTML><p" + sWidth + ">" + sValue + "<p><HTML/>");
                }
                return label;
            }
        });
//        tableColumn.setCellEditor(new DefaultCellEditor(new JComboBox(new String[] {"ASD", "QWE"})));
        tableColumn.setHeaderValue(column.getTitle());

        if (column.width() > -1) {
            tableColumn.setPreferredWidth(column.width());
        }

        tableColumnModel.addColumn(tableColumn);
        return tableColumn;
    }

    public T getSelected() {
        int selectedRowIndex = ui.getSelectedRow();

        if (selectedRowIndex > -1)
            return getRow(selectedRowIndex);

        return null;
    }

    private T getRow(int rowIndex) {
        T obj = Utils.newInstance(clazz);

        for (int colIndex = withNumberColumn ? 1 : 0; colIndex < columns.size(); colIndex++) {
            Object value = tableModel.getValueAt(rowIndex, colIndex);
            Utils.invokeMethod(obj, clazz, Utils.setterName(columns.get(colIndex).name()), value);
        }

        return obj;
    }

    public void removeSelected() {
        int selectedRowIndex = ui.getSelectedRow();
        ui.getSelectionModel().clearSelection();
        tableModel.removeRow(selectedRowIndex);
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
        list = list == null ? new ArrayList<T>() : list;
        ui.getSelectionModel().clearSelection();

        tableModel.setRowCount(0);

        nextLastRowNumber = 1;

        for (T t : list) {
            addRow(t);
        }
    }

    public void clean() {
        set(new ArrayList<T>());
    }

    public void addEmptyRow() {
        addRow(Utils.newInstance(clazz));
    }

    private void addRow(T t) {
        List<Object> cells = new ArrayList<>();

        if (withNumberColumn) {
            cells.add(nextLastRowNumber + "");
        }

        nextLastRowNumber++;

        for (int i = withNumberColumn ? 1 : 0; i < columns.size(); i++) {
            Object value = Utils.invokeMethod(t, clazz, Utils.getterName(columns.get(i).name()));
            cells.add(value);
        }

        tableModel.addRow(cells.toArray());
    }

    private String getDisplayValue(Object value) {
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
                strList.add(getDisplayValue(obj));
            }

            return Joiner.on(", ").join(strList);
        }

        return value.toString();
    }


}

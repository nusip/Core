package kz.maks.core.front.ui;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import kz.maks.core.front.validation.AbstractFieldValidator;
import kz.maks.core.shared.models.ICombo;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.util.List;

import static com.google.common.collect.Lists.transform;

public class ComboTableField extends AbstractFieldValidator<List<Long>> {
    public final SimpleTableField<ICombo> simpleTableField;
    private final ComboBox comboBox;

    public ComboTableField(FormField formField, ICombo[] items) {
        super(formField);
        simpleTableField = new SimpleTableField<>(formField);
        comboBox = new ComboBox(null, items);
        int columnIndex = simpleTableField.tableField.table.ui.getColumnModel().getColumnIndex(SimpleRecord.FIELD_NAME);
        TableColumn tableColumn = simpleTableField.tableField.table.ui.getColumnModel().getColumn(columnIndex);
        tableColumn.setCellEditor(new DefaultCellEditor(comboBox.ui));
    }

    @Override
    public List<Long> get() {
        List<Long> ids = transform(simpleTableField.get(), new Function<ICombo, Long>() {
            @Override
            public Long apply(ICombo input) {
                return input.getId();
            }
        });
        return Lists.newArrayList(ids);
    }

    @Override
    public void set(List<Long> val) {
        List<ICombo> combos = val != null ? transform(val, new Function<Long, ICombo>() {
            @Override
            public ICombo apply(Long input) {
                return comboBox.idToCombo.get(input);
            }
        }) : null;
        simpleTableField.set(combos);
    }
}

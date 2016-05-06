package kz.maks.core.front.ui;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import kz.maks.core.front.annotations.Required;
import kz.maks.core.front.validation.AbstractFieldValidator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Collection;
import java.util.List;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.Lists.transform;
import static kz.maks.core.shared.Utils.getField;

public class SimpleTableField<T> extends AbstractFieldValidator<List<T>> {

    public final TableField<SimpleRecord<T>> tableField;

    public SimpleTableField(FormField formField) {
        super(formField);
        boolean isRequired = getField(formField.getClass(), formField.name()).isAnnotationPresent(Required.class);
        String title = isRequired ? "<HTML>" + formField.getTitle() + "<b color=\"red\">*</b></HTML>" : formField.getTitle();
        tableField = new TableField<>(
                formField,
                new IColumn[] {
                        new Column<>(
                                SimpleRecord.class,
                                SimpleRecord.FIELD_NAME,
                                title,
                                true,
                                IColumn.DEFAULT_WIDTH
                        )
                }
        );
        tableField.table.ui.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                tableField.table.ui.setBorder(BorderFactory.createDashedBorder(Color.BLACK));
            }

            @Override
            public void focusLost(FocusEvent e) {
                tableField.table.ui.setBorder(null);
            }
        });
    }

    @Override
    public List<T> get() {
        List<T> values = transform(tableField.get(), new Function<SimpleRecord<T>, T>() {
            @Override
            public T apply(SimpleRecord<T> simpleRecord) {
                return simpleRecord.getValue();
            }
        });
//        Collection<String> filteredValues = filter(values, new Predicate<String>() {
//            @Override
//            public boolean apply(String s) {
//                return !isNullOrEmpty(s);
//            }
//        });
        return Lists.newArrayList(values);
    }

    @Override
    public void set(List<T> val) {
        List<SimpleRecord<T>> simpleRecords = val != null ? transform(val, new Function<T, SimpleRecord<T>>() {
            @Override
            public SimpleRecord<T> apply(T s) {
                return new SimpleRecord(s);
            }
        }) : null;
        tableField.set(simpleRecords);
    }
}

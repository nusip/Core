package kz.maks.core.front.ui;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import kz.maks.core.front.validation.AbstractFieldValidator;

import java.util.Collection;
import java.util.List;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.Lists.transform;

public class SimpleTableField extends AbstractFieldValidator<List<String>> {

    public final TableField<SimpleRecord> tableField;

    public SimpleTableField(FormField formField, Column<SimpleRecord> column) {
        super(formField);
        tableField = new TableField<>(formField, new IColumn[] {column});
    }

    @Override
    public List<String> get() {
        List<String> values = transform(tableField.get(), new Function<SimpleRecord, String>() {
            @Override
            public String apply(SimpleRecord simpleRecord) {
                return simpleRecord.getValue();
            }
        });
        Collection<String> filteredValues = filter(values, new Predicate<String>() {
            @Override
            public boolean apply(String s) {
                return !isNullOrEmpty(s);
            }
        });
        return Lists.newArrayList(filteredValues);
    }

    @Override
    public void set(List<String> val) {
        List<SimpleRecord> simpleRecords = val != null ? transform(val, new Function<String, SimpleRecord>() {
            @Override
            public SimpleRecord apply(String s) {
                return new SimpleRecord(s);
            }
        }) : null;
        tableField.set(simpleRecords);
    }
}

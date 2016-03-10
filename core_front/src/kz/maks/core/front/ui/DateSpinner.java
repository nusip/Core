package kz.maks.core.front.ui;

import kz.maks.core.front.validation.AbstractFieldValidator;

import javax.swing.*;
import java.util.Date;

public class DateSpinner extends AbstractFieldValidator<Date> {

    public final JSpinner ui = new JSpinner(new SpinnerDateModel());

    private static final String DEFAULT_FORMAT = "yyyy-MM-dd";

    public DateSpinner(FormField formField) {
        this(formField, DEFAULT_FORMAT);
    }

    public DateSpinner(FormField formField, String dateFormat) {
        super(formField);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(ui, dateFormat);
        ui.setEditor(editor);
    }

    @Override
    public Date get() {
        return (Date) ui.getValue();
    }

    @Override
    public void set(Date val) {
        if (val != null) ui.setValue(val);
    }

}

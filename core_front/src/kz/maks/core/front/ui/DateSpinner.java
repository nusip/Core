package kz.maks.core.front.ui;

import kz.maks.core.front.validation.AbstractFieldValidator;
import kz.maks.core.shared.Utils;

import javax.swing.*;
import java.util.Date;

import static kz.maks.core.shared.Utils.DATE_FORMAT_DEFAULT;

public class DateSpinner extends AbstractFieldValidator<Date> {

    public final JSpinner ui = new JSpinner(new SpinnerDateModel());

    public DateSpinner(FormField formField) {
        this(formField, DATE_FORMAT_DEFAULT);
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

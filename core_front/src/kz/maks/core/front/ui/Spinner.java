package kz.maks.core.front.ui;

import kz.maks.core.front.validation.AbstractFieldValidator;

import javax.swing.*;
import java.math.BigDecimal;

public class Spinner extends AbstractFieldValidator<Number> {

    public final JSpinner ui = new JSpinner();

    private final boolean decimal;

    public Spinner(FormField formField) {
        this(formField, false);
    }

    public Spinner(FormField formField, boolean decimal) {
        super(formField);
        this.decimal = decimal;

        if (decimal) {
            JSpinner.NumberEditor editor = new JSpinner.NumberEditor(ui, "0.00");
            ui.setEditor(editor);
        }
    }

    @Override
    public Number get() {
        String sValue = String.valueOf(ui.getValue());
        if (decimal) {
            double val = Double.parseDouble(sValue);
            return val;
        } else {
            int val = Integer.parseInt(sValue);
            return val;
        }
    }

    @Override
    public void set(Number val) {
        ui.setValue(val == null ? 0 : val);
    }

}

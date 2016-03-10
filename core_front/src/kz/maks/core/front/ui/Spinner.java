package kz.maks.core.front.ui;

import kz.maks.core.front.validation.AbstractFieldValidator;

import javax.swing.*;
import java.math.BigDecimal;

public class Spinner extends AbstractFieldValidator<Number> {

    public static final int INT_MODE = 0;
    public static final int DECIMAL_MODE = 1;

    public final JSpinner ui = new JSpinner();

    private final int mode;

    public Spinner(FormField formField) {
        this(formField, INT_MODE);
    }

    public Spinner(FormField formField, int mode) {
        super(formField);
        this.mode = mode;

        if (mode == DECIMAL_MODE) {
            JSpinner.NumberEditor editor = new JSpinner.NumberEditor(ui, "0.00");
            ui.setEditor(editor);
        }
    }

    @Override
    public Number get() {
        String sValue = String.valueOf(ui.getValue());

        if (mode == DECIMAL_MODE) {
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

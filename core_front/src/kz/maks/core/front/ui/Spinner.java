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
            ui.setModel(new SpinnerNumberModel(Double.valueOf(0), null, null, Double.valueOf(1)));
            JSpinner.NumberEditor editor = new JSpinner.NumberEditor(ui, "0.00");
            ui.setEditor(editor);
        }
    }

    @Override
    public Number get() {
        try {
            if (mode == DECIMAL_MODE) {
                double val = (double) ui.getValue();
                return val;
            } else {
                int val = (int) ui.getValue();
                return val;
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void set(Number val) {
        if (val == null) {
            if (mode == DECIMAL_MODE) {
                val = Double.valueOf(0);
            } else {
                val = Integer.valueOf(0);
            }
        }
        ui.setValue(val);
    }

}

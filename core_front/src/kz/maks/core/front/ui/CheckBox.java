package kz.maks.core.front.ui;

import kz.maks.core.front.validation.AbstractFieldValidator;

import javax.swing.*;

public class CheckBox extends AbstractFieldValidator<Boolean> {

    public final JCheckBox ui = new JCheckBox();

    public CheckBox(FormField formField) {
        super(formField);
    }

    @Override
    public Boolean get() {
        return ui.isSelected();
    }

    @Override
    public void set(Boolean val) {
        ui.setSelected(Boolean.TRUE.equals(val));
    }

}

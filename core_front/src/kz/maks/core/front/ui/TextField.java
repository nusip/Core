package kz.maks.core.front.ui;

import kz.maks.core.front.validation.AbstractFieldValidator;

import javax.swing.*;

public class TextField extends AbstractFieldValidator<String> {

    public final JTextField ui = new JTextField();

    public TextField(FormField formField) {
        super(formField);
    }

    @Override
    public String get() {
        return ui.getText();
    }

    @Override
    public void set(String val) {
        ui.setText(val);
    }

}

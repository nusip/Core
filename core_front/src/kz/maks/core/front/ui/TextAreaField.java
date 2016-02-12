package kz.maks.core.front.ui;

import kz.maks.core.front.validation.AbstractFieldValidator;

import javax.swing.*;
import java.awt.*;

public class TextAreaField extends AbstractFieldValidator<String> {

    public final JTextArea ui = new JTextArea();

    public TextAreaField(FormField formField) {
        super(formField);
        ui.setFont(Font.getFont(Font.DIALOG_INPUT));
        ui.setWrapStyleWord(true);
        ui.setLineWrap(true);
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

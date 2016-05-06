package kz.maks.core.front.ui;

import com.google.common.base.Strings;
import kz.maks.core.front.validation.AbstractFieldValidator;

import javax.swing.*;

import static com.google.common.base.Strings.isNullOrEmpty;

public class PasswordField extends AbstractFieldValidator<String> {

    public final JPasswordField ui = new JPasswordField();

    public PasswordField(FormField formField) {
        super(formField);
    }

    @Override
    public String get() {
        return String.valueOf(ui.getPassword());
    }

    @Override
    public void set(String val) {
        ui.setText(val);
    }

}

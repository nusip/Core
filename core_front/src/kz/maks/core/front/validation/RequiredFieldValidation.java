package kz.maks.core.front.validation;

import kz.maks.core.front.ui.FormField;

import java.util.List;

public class RequiredFieldValidation implements FieldValidation {

    @Override
    public String validate(FormField formField, Object value) {
        String error = "Поле \"" + formField.title() + "\" обязательно для заполнения.";

        if (value == null)
            return error;

        if (value instanceof Integer) {
            if (value == 0)
                return error;
        }

        if (value instanceof Double) {
            if (Double.valueOf(0).compareTo((Double) value) == 0)
                return error;
        }

        if (value instanceof List) {
            if (((List) value).isEmpty())
                return error;
        }

        return null;
    }

}

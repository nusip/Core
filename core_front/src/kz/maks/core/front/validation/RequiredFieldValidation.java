package kz.maks.core.front.validation;

import kz.maks.core.front.ui.FormField;

import java.util.List;

public class RequiredFieldValidation implements FieldValidation {

    @Override
    public String validate(FormField formField, Object value) {
        String error = "Поле \"" + formField.getTitle() + "\" обязательно для заполнения.";

        if (value == null)
            return error;

        if (value instanceof String) {
            if (((String)value).isEmpty())
                return error;
        }

        if (value instanceof Integer) {
            if ((Integer)value == 0)
                return error;
        }

        if (value instanceof Double) {
            if (Double.valueOf(0).compareTo((Double) value) == 0)
                return error;
        }

        if (value instanceof List) {
            List list = (List) value;

            for (Object element : list) {
                if (element == null || element.toString().trim().isEmpty())
                    return error;
            }

            if (list.isEmpty()) return error;
        }

        return null;
    }

}

package kz.maks.core.front.validation;

import kz.maks.core.front.ui.FormField;

public interface FieldValidation {

    /**
     * validate,
     * return error message if validation fails,
     * return null if validation succeeds
     */
    String validate(FormField formField, Object value);

}

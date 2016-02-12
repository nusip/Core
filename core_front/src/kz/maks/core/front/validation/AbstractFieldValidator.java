package kz.maks.core.front.validation;

import kz.maks.core.front.ui.FormField;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFieldValidator<T> implements ValidatableFieldAccessor<T> {

    private final List<FieldValidation> validations = new ArrayList<>();
    private final List<String> errorMessages = new ArrayList<>();
    private final FormField formField;

    public AbstractFieldValidator(FormField formField) {
        this.formField = formField;
    }

    @Override
    public void addFieldValidation(FieldValidation validation) {
        validations.add(validation);
    }

    private void validate() {
        errorMessages.clear();

        for (FieldValidation validation : validations) {
            String error = validation.validate(formField, get());

            if (error != null) {
                errorMessages.add(error);
            }
        }
    }

    @Override
    public List<String> errorMessages() {
        return errorMessages;
    }

    @Override
    public boolean isValid() {
        validate();
        return errorMessages.isEmpty();
    }

}

package kz.maks.core.front.validation;

public interface ValidatableField extends Validatable {

    void addFieldValidation(FieldValidation validation);

}

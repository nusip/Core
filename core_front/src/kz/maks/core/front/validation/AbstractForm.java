package kz.maks.core.front.validation;

import kz.maks.core.front.ui.FormField;
import kz.maks.core.shared.Utils;
import kz.maks.core.shared.models.Accessor;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractForm<T> implements Accessor<T>, Validatable {
    public final JPanel ui = new JPanel();

    protected final FormField<T>[] formFields;
    protected final Class<T> clazz;
    protected final Map<FormField, ValidatableFieldAccessor> fieldValues = new HashMap<>();
    protected final List<String> errorMessages = new ArrayList<>();

    protected AbstractForm(FormField<T>[] formFields) {
        this.formFields = formFields;
        clazz = formFields[0].formClass();
    }

    @Override
    public T get() {
        T obj = Utils.newInstance(clazz);

        for (FormField<T> formField : formFields) {
            Accessor fieldValue = fieldValues.get(formField);
            Object val = fieldValue.get();
            Utils.invokeMethod(obj, clazz, Utils.setterName(formField.name()), val);
        }

        return obj;
    }

    @Override
    public void set(T obj) {
        for (FormField<T> formField : formFields) {
            Object val = obj != null ? Utils.invokeMethod(obj, clazz, Utils.getterName(formField.name())) : null;
            Accessor fieldValue = fieldValues.get(formField);
            fieldValue.set(val);
        }
    }

    @Override
    public List<String> errorMessages() {
        return errorMessages;
    }

    @Override
    public boolean isValid() {
        errorMessages.clear();

        for (FormField<T> formField : formFields) {
            ValidatableFieldAccessor fieldValue = fieldValues.get(formField);

            if (!fieldValue.isValid()) {
                errorMessages.addAll(fieldValue.errorMessages());
            }
        }

        return errorMessages.isEmpty();
    }

}

package kz.maks.core.front.ui;

import kz.maks.core.front.validation.AbstractFieldValidator;
import kz.maks.core.front.validation.ValidatableFieldAccessor;
import kz.maks.core.shared.models.Value;

public class HiddenField<T> extends AbstractFieldValidator<T> {

    private T t;

    public HiddenField(FormField formField) {
        super(formField);
    }

    @Override
    public T get() {
        return t;
    }

    @Override
    public void set(T val) {
        t = val;
    }

    @Override
    public boolean isValid() {
        return true;
    }

}

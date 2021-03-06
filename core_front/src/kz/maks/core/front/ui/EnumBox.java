package kz.maks.core.front.ui;

import kz.maks.core.front.validation.AbstractFieldValidator;

import javax.swing.*;

public class EnumBox<T> extends AbstractFieldValidator<T> {

    public final JComboBox ui;

    public EnumBox(FormField formField, T[] items) {
        super(formField);
        ui = new JComboBox(items);
    }

    @Override
    public T get() {
        return (T) ui.getSelectedItem();
    }

    @Override
    public void set(T val) {
        ui.setSelectedItem(val);
    }

}

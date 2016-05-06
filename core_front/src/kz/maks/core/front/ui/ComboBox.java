package kz.maks.core.front.ui;

import kz.maks.core.front.validation.AbstractFieldValidator;
import kz.maks.core.shared.models.ICombo;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class ComboBox extends AbstractFieldValidator<Long> {

    public final JComboBox ui;

    final Map<Long, ICombo> idToCombo = new HashMap<>();

    public ComboBox(FormField formField, ICombo[] items) {
        super(formField);
        ui = new JComboBox(items);

        for (ICombo combo : items) {
            idToCombo.put(combo.getId(), combo);
        }
    }

    @Override
    public Long get() {
        ICombo combo = (ICombo) ui.getSelectedItem();
        return combo != null ? combo.getId() : null;
    }

    @Override
    public void set(Long val) {
        ui.setSelectedItem(idToCombo.get(val));
    }

}

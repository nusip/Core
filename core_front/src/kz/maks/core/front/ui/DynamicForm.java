package kz.maks.core.front.ui;

import kz.maks.core.front.FrontUtils;
import kz.maks.core.front.annotations.*;
import kz.maks.core.front.annotations.TextArea;
import kz.maks.core.front.validation.RequiredFieldValidation;
import kz.maks.core.front.validation.AbstractForm;
import kz.maks.core.front.validation.ValidatableFieldAccessor;
import kz.maks.core.shared.Utils;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.List;

import static kz.maks.core.shared.Utils.isDecimalType;

public class DynamicForm<T> extends AbstractForm<T> {
    private int cols;

    public DynamicForm(Frame parent, FormField<T>[] formFields) {
        this(parent, formFields, 1);
    }

    public DynamicForm(Frame parent, FormField<T>[] formFields, int cols) {
        super(parent, formFields);
        this.cols = cols;
        build();
        processAnnotations();
    }

    protected void build() {
        FrontUtils.addMargins(ui);
        ui.setLayout(new GridBagLayout());

        int fieldIndex = 0;

        for (FormField formField : formFields) {
            JComponent fieldComponent = addField(formField);

            JLabel label = getLabel(formField);
            label.setHorizontalAlignment(SwingConstants.RIGHT);
            JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            labelPanel.add(label);
            addComponentInner(labelPanel, fieldIndex++);

            if (fieldComponent != null) {
                addComponentInner(fieldComponent, fieldIndex++);
            }
        }
    }

    private void addComponentInner(JComponent component, int index) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(3, 3, 3, 3);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.gridx = index % (cols * 2);

        ui.add(component, constraints);
    }

    public void insertComponent(JComponent component, int gridx, int gridy) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(3, 3, 3, 3);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.gridx = gridx;
        constraints.gridy = gridy;

        ui.add(component, constraints);
    }

}

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
    private final Frame parent;
    private int cols;

    public DynamicForm(Frame parent, FormField<T>[] formFields) {
        this(parent, formFields, 1);
    }

    public DynamicForm(Frame parent, FormField<T>[] formFields, int cols) {
        super(formFields);
        this.parent = parent;
        this.cols = cols;

        try {
            build();

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private void build() throws NoSuchFieldException {
        FrontUtils.addMargins(ui);
        ui.setLayout(new GridBagLayout());

        Map<String, Field> fieldMap = getAllFields(clazz);

        int fieldIndex = 0;

        for (FormField formField : formFields) {
            Field field = fieldMap.get(formField.name());

            JComponent fieldComponent = null;

            if (formField.getClass().getDeclaredField(formField.name()).isAnnotationPresent(Hidden.class)) {
                fieldValues.put(formField, new HiddenField(formField));
                continue;
            } else if (formField.getClass().getDeclaredField(formField.name()).isAnnotationPresent(TreeName.class)) {
                fieldComponent = getTreeLink(formField);
            } else if (String.class.isAssignableFrom(field.getType())) {
                if (formField.getClass().getDeclaredField(formField.name()).isAnnotationPresent(TextArea.class)) {
                    fieldComponent = getTextArea(formField);
                } else {
                    fieldComponent = getTextField(formField);
                }
            } else if (Boolean.class.isAssignableFrom(field.getType())) {
                fieldComponent = getCheckBox(formField);
            } else if (Number.class.isAssignableFrom(field.getType())) {
                fieldComponent = getSpinner(formField, isDecimalType(field.getType()) ? Spinner.DECIMAL_MODE : Spinner.INT_MODE);
            } else if (field.getType().isEnum()) {
                fieldComponent = getCombo(formField, field.getType());
            } else if (List.class.isAssignableFrom(field.getType())) {
                ParameterizedType fieldType = (ParameterizedType) field.getGenericType();
                Class<?> componentType = (Class<?>) fieldType.getActualTypeArguments()[0];

                if (componentType == String.class) {
                    fieldComponent = getTextFieldList(formField);
                } else {
                    continue;
                }
            }

            addValidations(formField);

            JLabel label = getLabel(formField);
            label.setHorizontalAlignment(SwingConstants.RIGHT);
            JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            labelPanel.add(label);
            addComponentInner(labelPanel, fieldIndex++);

            addComponentInner(fieldComponent, fieldIndex++);
        }
    }

    private void addValidations(FormField formField) {
        Field field = Utils.getField(formField.getClass(), formField.name());
        ValidatableFieldAccessor validatableFieldAccessor = fieldValues.get(formField);

        if (field.isAnnotationPresent(Required.class)) {
            validatableFieldAccessor.addFieldValidation(new RequiredFieldValidation());
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

    private static Map<String, Field> getAllFields(Class<?> clazz) {
        Map<String, Field> fieldMap = new HashMap<>();
        List<Field> fieldList = new ArrayList<>();

        addAllFields(fieldList, clazz);

        for (Field field : fieldList) {
            fieldMap.put(field.getName(), field);
        }

        return fieldMap;
    }

    private static void addAllFields(List<Field> fieldList, Class<?> clazz) {
        fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));

        if (clazz.getSuperclass() != null) {
            addAllFields(fieldList, clazz.getSuperclass());
        }
    }

    private JLabel getLabel(FormField formField) {
        JLabel label = new JLabel();
        boolean isRequired = Utils.getField(formField.getClass(), formField.name()).isAnnotationPresent(Required.class);

        if (isRequired) {
            label.setText("<HTML>" + formField.title() + "<b color=\"red\">*</b></HTML>");
        } else {
            label.setText(formField.title());
        }

        return label;
    }

    private JButton getTreeLink(final FormField formField) {
        TreeLink treeLink = new TreeLink(parent, formField);
        fieldValues.put(formField, treeLink);
        return treeLink.ui;
    }

    private JComboBox getCombo(FormField formField, Class<?> type) {
        EnumBox comboBox = new EnumBox(formField, type.getEnumConstants());
        fieldValues.put(formField, comboBox);
        return comboBox.ui;
    }

    private JSpinner getSpinner(FormField formField, int mode) {
        Spinner spinner = new Spinner(formField, mode);
        fieldValues.put(formField, spinner);
        return spinner.ui;
    }

    private JCheckBox getCheckBox(FormField formField) {
        final CheckBox checkBox = new CheckBox(formField);
        fieldValues.put(formField, checkBox);
        return checkBox.ui;
    }

    private JScrollPane getTextArea(FormField formField) {
        TextAreaField textAreaField = new TextAreaField(formField);
        JScrollPane scrollPane = new JScrollPane(textAreaField.ui);
        FrontUtils.setPreferredHeight(scrollPane, 100);
        FrontUtils.setMinHeight(scrollPane, 100);
        fieldValues.put(formField, textAreaField);
        return scrollPane;
    }

    private Box getTextFieldList(FormField formField) {
        TextFieldList textFieldList = new TextFieldList(formField);
        fieldValues.put(formField, textFieldList);
        return textFieldList.ui;
    }

    private JTextField getTextField(FormField formField) {
        TextField textField = new TextField(formField);
        fieldValues.put(formField, textField);
        return textField.ui;
    }

}
package kz.maks.core.front.validation;

import kz.maks.core.front.Cache;
import kz.maks.core.front.FrontUtils;
import kz.maks.core.front.annotations.*;
import kz.maks.core.front.ui.*;
import kz.maks.core.shared.Utils;
import kz.maks.core.shared.models.Accessor;
import kz.maks.core.shared.models.ICombo;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.List;

import static kz.maks.core.shared.Utils.isDecimalType;

public abstract class AbstractForm<T> implements Accessor<T>, Validatable {
    public final JPanel ui = new JPanel();

    protected final FormField<T>[] formFields;
    protected final Class<T> clazz;
    protected final Map<FormField, ValidatableFieldAccessor> fieldValues = new HashMap<>();
    protected final Map<FormField, JComponent> fieldComponents = new HashMap<>();
    protected final List<String> errorMessages = new ArrayList<>();
    protected final Frame parent;
    protected final Map<String, Field> fieldMap;

    protected AbstractForm(Frame parent, FormField<T>[] formFields) {
        this.formFields = formFields;
        this.parent = parent;
        clazz = formFields[0].formClass();
        fieldMap = getAllFields(clazz);
    }

    protected JComponent addField(FormField formField) {
        Field field = fieldMap.get(formField.name());

        JComponent fieldComponent = null;

        if (Utils.getField(formField.getClass(), formField.name()).isAnnotationPresent(Hidden.class)) {
            fieldValues.put(formField, new HiddenField(formField));
            return fieldComponent;
        } else if (Utils.getField(formField.getClass(), formField.name()).isAnnotationPresent(TreeName.class)) {
            fieldComponent = getTreeLink(formField);
        } else if (Utils.getField(formField.getClass(), formField.name()).isAnnotationPresent(ComboName.class)) {
            fieldComponent = getComboBox(formField);
        } else if (String.class.isAssignableFrom(field.getType())) {
            if (Utils.getField(formField.getClass(), formField.name()).isAnnotationPresent(kz.maks.core.front.annotations.TextArea.class)) {
                fieldComponent = getTextArea(formField);
            } else {
                fieldComponent = getTextField(formField);
            }
        } else if (Boolean.class.isAssignableFrom(field.getType()) || boolean.class.isAssignableFrom(field.getType())) {
            fieldComponent = getCheckBox(formField);
        } else if (Number.class.isAssignableFrom(field.getType())) {
            fieldComponent = getSpinner(formField, isDecimalType(field.getType()) ? Spinner.DECIMAL_MODE : Spinner.INT_MODE);
        } else if (Date.class.isAssignableFrom(field.getType())) {
            fieldComponent = getDateSpinner(formField);
        } else if (field.getType().isEnum()) {
            fieldComponent = getEnumBox(formField, field.getType());
        } else if (List.class.isAssignableFrom(field.getType())) {
            ParameterizedType fieldType = (ParameterizedType) field.getGenericType();
            Class<?> componentType = (Class<?>) fieldType.getActualTypeArguments()[0];

            if (componentType == String.class) {
                fieldComponent = getStringTableField(formField);
            } else {
                return fieldComponent;
            }
        }

        fieldComponents.put(formField, fieldComponent);

        return fieldComponent;
    }

    protected void processAnnotations() {
        for (FormField formField : formFields) {
            Field field = Utils.getField(formField.getClass(), formField.name());
            ValidatableFieldAccessor validatableFieldAccessor = fieldValues.get(formField);

            if (field.isAnnotationPresent(Required.class)) {
                validatableFieldAccessor.addFieldValidation(new RequiredFieldValidation());
            }

            if (field.isAnnotationPresent(Disabled.class)) {
                fieldComponents.get(formField).setEnabled(false);
            }
        }
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

    protected JLabel getLabel(FormField formField) {
        JLabel label = new JLabel();
        boolean isRequired = Utils.getField(formField.getClass(), formField.name()).isAnnotationPresent(Required.class);

        if (isRequired) {
            label.setText("<HTML>" + formField.getTitle() + "<b color=\"red\">*</b></HTML>");
        } else {
            label.setText(formField.getTitle());
        }

        return label;
    }

    protected JButton getTreeLink(final FormField formField) {
        TreeLink treeLink = new TreeLink(parent, formField);
        fieldValues.put(formField, treeLink);
        return treeLink.ui;
    }

    private JComponent getComboBox(FormField formField) {
        ComboName comboName = Utils.getField(formField.getClass(), formField.name()).getAnnotation(ComboName.class);
        ComboBox comboBox = new ComboBox(formField, Cache.getCombo(comboName.value()).toArray(new ICombo[] {}));
        fieldValues.put(formField, comboBox);
        return comboBox.ui;
    }

    protected JComboBox getEnumBox(FormField formField, Class<?> type) {
        EnumBox enumBox = new EnumBox(formField, type.getEnumConstants());
        fieldValues.put(formField, enumBox);
        return enumBox.ui;
    }

    protected JSpinner getSpinner(FormField formField, int mode) {
        Spinner spinner = new Spinner(formField, mode);
        fieldValues.put(formField, spinner);
        return spinner.ui;
    }

    protected JSpinner getDateSpinner(FormField formField) {
        DateSpinner dateSpinner = new DateSpinner(formField);
        fieldValues.put(formField, dateSpinner);
        return dateSpinner.ui;
    }

    protected JCheckBox getCheckBox(FormField formField) {
        final CheckBox checkBox = new CheckBox(formField);
        fieldValues.put(formField, checkBox);
        return checkBox.ui;
    }

    protected JScrollPane getTextArea(FormField formField) {
        TextAreaField textAreaField = new TextAreaField(formField);
        JScrollPane scrollPane = new JScrollPane(textAreaField.ui);
        FrontUtils.setPreferredHeight(scrollPane, 100);
        FrontUtils.setMinHeight(scrollPane, 100);
        fieldValues.put(formField, textAreaField);
        return scrollPane;
    }

    protected Box getStringTableField(FormField formField) {
        boolean isRequired = Utils.getField(formField.getClass(), formField.name()).isAnnotationPresent(Required.class);
        String title = isRequired ? "<HTML>" + formField.getTitle() + "<b color=\"red\">*</b></HTML>" : formField.getTitle();
        final SimpleTableField simpleTableField = new SimpleTableField(
                formField, new Column<>(SimpleRecord.class, SimpleRecord.FIELD_NAME, title, true, IColumn.DEFAULT_WIDTH));
        fieldValues.put(formField, simpleTableField);
        return simpleTableField.tableField.ui;
    }

    protected Box getTextFieldList(FormField formField) {
        TextFieldList textFieldList = new TextFieldList(formField);
        fieldValues.put(formField, textFieldList);
        return textFieldList.ui;
    }

    protected JTextField getTextField(FormField formField) {
        kz.maks.core.front.ui.TextField textField = new kz.maks.core.front.ui.TextField(formField);
        fieldValues.put(formField, textField);
        return textField.ui;
    }

}

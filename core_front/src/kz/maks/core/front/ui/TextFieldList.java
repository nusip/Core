package kz.maks.core.front.ui;

import kz.maks.core.front.FrontUtils;
import kz.maks.core.front.validation.AbstractFieldValidator;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Strings.isNullOrEmpty;

public class TextFieldList extends AbstractFieldValidator<List<String>> {

    public final Box ui = Box.createVerticalBox();

    private final List<JTextField> textFields = new ArrayList<>();

    private final Box linkBox = Box.createHorizontalBox();

    public TextFieldList(FormField formField) {
        super(formField);
        LinkButton linkButton = new LinkButton("Добавить " + formField.getTitle().toLowerCase());
        linkButton.ui.setHorizontalAlignment(SwingConstants.LEFT);
        linkBox.add(linkButton.ui);
        ui.add(linkBox);

        linkButton.ui.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTextField(null);
            }
        });
    }

    private void addTextField(String val) {
        final Box fieldBox = Box.createHorizontalBox();
        FrontUtils.addMarginsBottom(fieldBox);
        final JTextField textField = new JTextField();
        textField.setText(val);
        fieldBox.add(textField);
        fieldBox.add(FrontUtils.hGap());
        JButton btnRemove = new JButton("-");
        btnRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textFields.remove(textField);
                ui.remove(fieldBox);
                ui.revalidate();
            }
        });
        fieldBox.add(btnRemove);
        ui.add(fieldBox, textFields.size());
        ui.revalidate();
        textFields.add(textField);
    }


    @Override
    public List<String> get() {
        List<String> vals = new ArrayList<>();

        for (JTextField textField : textFields) {
            if (!isNullOrEmpty(textField.getText().trim())) {
                vals.add(textField.getText());
            }
        }

        return vals;
    }

    @Override
    public void set(List<String> vals) {
        textFields.clear();
        ui.removeAll();
        ui.add(linkBox);

        if (vals == null) {
            return;
        }

        for (String val : vals) {
            addTextField(val);
        }
    }

}

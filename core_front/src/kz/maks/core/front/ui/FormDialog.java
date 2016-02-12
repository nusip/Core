package kz.maks.core.front.ui;

import com.google.common.base.Joiner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormDialog<T> extends BasicDialog {

    public final Form<T> form;

    public FormDialog(Frame parent, Form<T> form) {
        super(parent, form.ui, null);
        this.form = form;
    }

    public void runOnValid(final Runnable runnable) {
        btnOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (form.isValid()) {
                    runnable.run();
                } else {
                    String errors = Joiner.on("\n").join(form.errorMessages());
                    JOptionPane.showMessageDialog(null, errors, "Ошибка валидации", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

}

package kz.maks.core.front.ui;

import kz.maks.core.front.FrontUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static kz.maks.core.front.FrontUtils.DEFAULT_GAP_SIZE;
import static kz.maks.core.front.FrontUtils.hGap;

public class BasicDialog {
    public final JDialog ui;
    public final JButton btnOk;

    public BasicDialog(Frame parent, final Component component, String title) {
        ui = new JDialog(parent, true) {
            @Override
            public Dimension getPreferredSize() {
                return FrontUtils.fitWindowSize(super.getPreferredSize());
            }
        };

        ui.setLayout(new BoxLayout(ui.getContentPane(), BoxLayout.Y_AXIS));
        ui.setTitle(title);
        {
            Box verticalBox = Box.createVerticalBox();
            ui.add(verticalBox);
            {
                JScrollPane scrollPane = new JScrollPane(component);
                FrontUtils.setPreferredWidth(scrollPane, component.getPreferredSize().width + DEFAULT_GAP_SIZE * 2);
                verticalBox.add(scrollPane);
            }
            {
                Box btnsBox = Box.createHorizontalBox();
                FrontUtils.addMargins(btnsBox);
                verticalBox.add(btnsBox);
                {
                    btnsBox.add(Box.createHorizontalGlue());
                }
                {
                    btnOk = new JButton("OK");
                    btnsBox.add(btnOk);
                }
                {
                    btnsBox.add(hGap());
                }
                {
                    JButton btnCancel = new JButton("Отмена");
                    btnCancel.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            ui.setVisible(false);
                        }
                    });
                    btnsBox.add(btnCancel);
                }
            }
        }
        ui.pack();
        ui.setLocationRelativeTo(parent);
        ui.setResizable(false);
        ui.setVisible(false);

        ui.getRootPane().registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ui.setVisible(false);
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }
}

package kz.maks.core.front.ui;

import kz.maks.core.front.FrontUtils;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class BtnCRUDPanel {
    public final Box ui;
    public final JButton btnAdd;
    public final JButton btnEdit;
    public final JButton btnDelete;

    public BtnCRUDPanel() {
        ui = Box.createVerticalBox();
        FrontUtils.addMargins(ui);
        ui.setPreferredSize(new Dimension(40, 40));
        {
            {
                URL resource = this.getClass().getResource("add-icon.png");
                ImageIcon icon = new ImageIcon(resource);
                btnAdd = new JButton(icon);
                ui.add(btnAdd);
            }
            ui.add(FrontUtils.vGap());
            {
                URL resource = this.getClass().getResource("edit-icon.png");
                ImageIcon icon = new ImageIcon(resource);
                btnEdit = new JButton(icon);
                ui.add(btnEdit);
            }
            ui.add(FrontUtils.vGap());
            {
                URL resource = this.getClass().getResource("delete-icon.png");
                ImageIcon icon = new ImageIcon(resource);
                btnDelete = new JButton(icon);
                ui.add(btnDelete);
            }
            ui.add(Box.createVerticalGlue());
        }
    }
}

package kz.maks.core.front.ui;

import com.google.common.collect.Lists;
import kz.maks.core.front.FrontUtils;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.List;

import static kz.maks.core.front.ui.BtnCRUDPanel.Button.*;

public class BtnCRUDPanel {
    public final Box ui;
    public final JButton btnAdd;
    public final JButton btnEdit;
    public final JButton btnDelete;
    public final JButton btnSave;
    public final JButton btnRefresh;

    public enum Button {
        ADD, EDIT, DELETE, SAVE, REFRESH
    }

    public BtnCRUDPanel(Button... visibleButtons) {
        ui = Box.createVerticalBox();
        FrontUtils.addMargins(ui);
        ui.setPreferredSize(new Dimension(40, 40));
        List<Button> visibleBtnList = Lists.newArrayList(visibleButtons);
        {
            URL resource = this.getClass().getResource("refresh-icon.png");
            ImageIcon icon = new ImageIcon(resource);
            btnRefresh = new JButton(icon);

            if (visibleBtnList.contains(REFRESH)) {
                ui.add(btnRefresh);
                ui.add(FrontUtils.vGap());
            }
        }
        {
            {
                URL resource = this.getClass().getResource("add-icon.png");
                ImageIcon icon = new ImageIcon(resource);
                btnAdd = new JButton(icon);

                if (visibleBtnList.contains(ADD)) {
                    ui.add(btnAdd);
                    ui.add(FrontUtils.vGap());
                }
            }
            {
                URL resource = this.getClass().getResource("edit-icon.png");
                ImageIcon icon = new ImageIcon(resource);
                btnEdit = new JButton(icon);

                if (visibleBtnList.contains(EDIT)) {
                    ui.add(btnEdit);
                    ui.add(FrontUtils.vGap());
                }
            }
            {
                URL resource = this.getClass().getResource("save-icon.png");
                ImageIcon icon = new ImageIcon(resource);
                btnSave = new JButton(icon);

                if (visibleBtnList.contains(SAVE)) {
                    ui.add(btnSave);
                    ui.add(FrontUtils.vGap());
                }
            }
            {
                URL resource = this.getClass().getResource("delete-icon.png");
                ImageIcon icon = new ImageIcon(resource);
                btnDelete = new JButton(icon);

                if (visibleBtnList.contains(DELETE)) {
                    ui.add(btnDelete);
                    ui.add(FrontUtils.vGap());
                }
            }
            ui.add(Box.createVerticalGlue());
        }
    }

}

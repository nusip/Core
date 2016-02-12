package kz.maks.core.front.ui;

import kz.maks.core.front.FrontUtils;

import javax.swing.*;

public class BtnPgPanel {
    public final Box ui;
    public final JButton btnPrev;
    public final JButton btnNext;

    public BtnPgPanel() {
        ui = Box.createHorizontalBox();
        FrontUtils.addMargins(ui);
        {
            btnPrev = new JButton("Предыдущая");
            ui.add(btnPrev);
        }
        ui.add(FrontUtils.hGap());
        {
            btnNext = new JButton("Следующая");
            ui.add(btnNext);
        }
    }
}

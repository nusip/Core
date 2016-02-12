package kz.maks.core.front.ui;

import javax.swing.*;
import java.awt.*;

public class LinkButton {
    private static final String TEXT_FORMAT = "<HTML><FONT color=\"#000099\"><U>%s</U></FONT></HTML>";

    public final JButton ui;
    private final String defaultValue;

    public LinkButton(String defaultValue) {
        this.defaultValue = defaultValue;
        ui = new JButton();
        ui.setContentAreaFilled(false);
        ui.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setText(defaultValue);
    }

    public void setText(String val) {
        if (val == null) {
            val = defaultValue;
        }
        ui.setText(String.format(TEXT_FORMAT, val));
    }
}

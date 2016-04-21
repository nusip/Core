package kz.maks.core.front.ui;

import kz.maks.core.front.services.Callback;

import javax.swing.*;
import java.awt.*;

public class ProgressDialogCallback<RESULT> extends Callback<RESULT> {
    private final ProgressGlassPane progressGlassPane;
    private final Component parent;

    /**
     * expects parent to have ProgressGlassPane instance set as glass pane
     */
    public ProgressDialogCallback(JFrame parent) {
        this.parent = parent;
        progressGlassPane = new ProgressGlassPane();
        parent.setGlassPane(progressGlassPane);
    }

    public ProgressDialogCallback(JDialog parent) {
        this.parent = parent;
        progressGlassPane = new ProgressGlassPane();
        parent.setGlassPane(progressGlassPane);
    }

    @Override
    public void beforeCall() {
        parent.setEnabled(false);
        progressGlassPane.showPane();
    }

    @Override
    public void atTheEnd() {
        progressGlassPane.hidePane();
        parent.setEnabled(true);
    }
}

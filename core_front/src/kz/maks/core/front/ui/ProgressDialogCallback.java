package kz.maks.core.front.ui;

import kz.maks.core.front.services.Callback;

import javax.swing.*;

public class ProgressDialogCallback<RESULT> extends Callback<RESULT> {
    private final JFrame parent;

    /**
     * expects parent to have ProgressGlassPane instance set as glass pane
     */
    public ProgressDialogCallback(JFrame parent) {
        this.parent = parent;
    }

    @Override
    public void beforeCall() {
        parent.setEnabled(false);
        ProgressGlassPane glassPane = (ProgressGlassPane) parent.getGlassPane();
        glassPane.showPane();
    }

    @Override
    public void atTheEnd() {
        ProgressGlassPane glassPane = (ProgressGlassPane) parent.getGlassPane();
        glassPane.hidePane();
        parent.setEnabled(true);
    }
}

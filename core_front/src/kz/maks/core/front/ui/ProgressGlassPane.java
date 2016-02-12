package kz.maks.core.front.ui;

import kz.maks.core.front.FrontUtils;

import javax.swing.*;
import java.awt.*;

public class ProgressGlassPane extends JComponent {

    private JProgressBar progressBar;

    public ProgressGlassPane() {
        setLayout(new GridBagLayout());
        {
            progressBar = new JProgressBar();
            FrontUtils.setPreferredHeight(progressBar, 25);
            add(progressBar);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // gets the current clipping area
        Rectangle clip = g.getClipBounds();

        // sets a 65% translucent composite
        AlphaComposite alpha = AlphaComposite.SrcOver.derive(0.65f);
        Composite composite = g2.getComposite();
        g2.setComposite(alpha);

        // fills the background
        g2.setColor(getBackground());
        g2.fillRect(clip.x, clip.y, clip.width, clip.height);

        g2.setComposite(composite);
    }

    public void showPane() {
        progressBar.setIndeterminate(true);
        setVisible(true);
    }

    public void hidePane() {
        progressBar.setIndeterminate(false);
        setVisible(false);
    }

}

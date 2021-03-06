package kz.maks.core.front;

import javax.swing.*;
import java.awt.*;

public class FrontUtils {

    public static final int DEFAULT_GAP_SIZE = 5;

    private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();

    public static Dimension fitWindowSize(Dimension preferredSize) {
        Dimension maxWindowSize = FrontUtils.getMaxWindowSize();

        if (preferredSize.getWidth() > maxWindowSize.width) {
            preferredSize.width = maxWindowSize.width;
        }

        if (preferredSize.getHeight() > maxWindowSize.height) {
            preferredSize.height = maxWindowSize.height;
        }

        return preferredSize;
    }

    public static Dimension getMaxWindowSize() {
        double screenHeight = SCREEN_SIZE.getHeight();
        int maxWindowHeight = (int) (screenHeight - screenHeight * 0.1);

        double screenWidth = SCREEN_SIZE.getWidth();
        int maxWindowWidth = (int) (screenWidth - screenWidth * 0.1);

        return new Dimension(maxWindowWidth, maxWindowHeight);
    }

    public static void addMargins(JComponent component) {
        addMargins(component, DEFAULT_GAP_SIZE);
    }

    public static void addMarginsBottom(JComponent component) {
        component.setBorder(BorderFactory.createEmptyBorder(0, 0, DEFAULT_GAP_SIZE, 0));
    }

    public static void addMargins(JComponent component, int gapSize) {
        component.setBorder(BorderFactory.createEmptyBorder(gapSize, gapSize, gapSize, gapSize));
    }

    public static void addBorder(JComponent component) {
        component.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    public static Component vGap() {
        return vGap(DEFAULT_GAP_SIZE);
    }

    public static Component vGap(int gapSize) {
        Component strut = Box.createVerticalStrut(gapSize);
        strut.setMaximumSize(new Dimension(gapSize, gapSize));
        return strut;
    }

    public static Component hGap() {
        return hGap(DEFAULT_GAP_SIZE);
    }

    public static Component hGap(int gapSize) {
        Component strut = Box.createHorizontalStrut(gapSize);
        strut.setMaximumSize(new Dimension(gapSize, gapSize));
        return strut;
    }

    public static void setMaxWidth(Component component, int width) {
        Dimension dimension = component.getMaximumSize();
        dimension.setSize(width, dimension.getHeight());
        component.setMaximumSize(dimension);
    }

    public static void setMaxHeight(Component component, int height) {
        Dimension dimension = component.getMaximumSize();
        dimension.setSize(dimension.getWidth(), height);
        component.setMaximumSize(dimension);
    }

    public static void setPreferredWidth(Component component, int width) {
        Dimension dimension = component.getPreferredSize();
        dimension.setSize(width, dimension.getHeight());
        component.setPreferredSize(dimension);
    }

    public static void setForcedWidth(Component component, int width) {
        setPreferredWidth(component, width);
        setMinWidth(component, width);
        setMaxWidth(component, width);
        setWidth(component, width);
    }

    public static void setForcedHeight(Component component, int height) {
        setPreferredHeight(component, height);
        setMinHeight(component, height);
        setMaxHeight(component, height);
        setHeight(component, height);
    }

    public static void setPreferredHeight(Component component, int height) {
        Dimension dimension = component.getPreferredSize();
        dimension.setSize(dimension.getWidth(), height);
        component.setPreferredSize(dimension);
    }

    public static void setMinWidth(Component component, int width) {
        Dimension dimension = component.getMinimumSize();
        dimension.setSize(width, dimension.getHeight());
        component.setMinimumSize(dimension);
    }

    public static void setMinHeight(Component component, int height) {
        Dimension dimension = component.getMinimumSize();
        dimension.setSize(dimension.getWidth(), height);
        component.setMinimumSize(dimension);
    }

    public static void setHeight(Component component, int height) {
        Dimension dimension = component.getSize();
        dimension.setSize(dimension.getWidth(), height);
        component.setSize(dimension);
    }

    public static void setWidth(Component component, int width) {
        Dimension dimension = component.getSize();
        dimension.setSize(width, dimension.getHeight());
        component.setSize(dimension);
    }
}

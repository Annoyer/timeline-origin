package org.jcy.timeline.swing.ui;

import java.awt.*;

public class Resources {

    public static final Color WHITE = new Color(255, 255, 255);

    public static void changeFontSize(Component component, int increment) {
        Font baseFont = component.getFont();
        Font font = createFrom(baseFont, increment);
        component.setFont(font);
    }

    static Font createFrom(Font baseFont, int increment) {
        return new Font(baseFont.getName(), baseFont.getStyle(), baseFont.getSize() + increment);
    }
}
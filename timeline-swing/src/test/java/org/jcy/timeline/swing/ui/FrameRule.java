package org.jcy.timeline.swing.ui;

import org.junit.rules.ExternalResource;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;

import static javax.swing.JFrame.EXIT_ON_CLOSE;
import static javax.swing.SwingUtilities.invokeAndWait;
import static javax.swing.SwingUtilities.invokeLater;
import static org.mockito.Mockito.mock;

class FrameRule extends ExternalResource {

    private final HashSet<JFrame> frames;

    FrameRule() {
        frames = new HashSet<>();
    }

    Container showInFrame(Container container) throws Exception {
        JFrame frame = new JFrame("Test Window");
        frames.add(frame);
        invokeLater(() -> {
            frame.add(container);
            frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
            frame.setBounds(100, 100, 350, 700);
            frame.setVisible(true);
        });
        invokeAndWait(mock(Runnable.class));
        invokeAndWait(mock(Runnable.class));
        return container;
    }

    @Override
    protected void after() {
        frames.forEach(frame -> frame.dispose());
    }
}
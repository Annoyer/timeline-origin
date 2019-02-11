package org.jcy.timeline.swing.ui;

import org.jcy.timeline.core.model.Item;
import org.jcy.timeline.core.ui.AutoUpdate;
import org.jcy.timeline.core.ui.ItemViewer;
import org.jcy.timeline.util.Messages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static org.jcy.timeline.util.Assertion.checkArgument;

class SwingAutoUpdate<T extends Item> implements AutoUpdate<T, Container> {

    private final ActionListener itemViewerNotifier;
    private final ActionListener headerNofifier;
    private final Timer timer;

    SwingAutoUpdate(Header<T> header, ItemViewer<T, Container> itemViewer, int delay) {
        checkArgument(header != null, Messages.get("HEADER_MUST_NOT_BE_NULL"));
        checkArgument(itemViewer != null, Messages.get("ITEM_VIEWER_MUST_NOT_BE_NULL"));
        checkArgument(delay >= 0, Messages.get("DELAY_MUST_NOT_BE_NEGATIVE"));

        this.timer = new Timer(delay, null);
        this.itemViewerNotifier = event -> itemViewer.update();
        this.headerNofifier = event -> header.update();
    }

    @Override
    public void start() {
        timer.addActionListener(itemViewerNotifier);
        timer.addActionListener(headerNofifier);
        timer.start();
    }

    @Override
    public void stop() {
        timer.stop();
        timer.removeActionListener(headerNofifier);
        timer.removeActionListener(itemViewerNotifier);
    }
}
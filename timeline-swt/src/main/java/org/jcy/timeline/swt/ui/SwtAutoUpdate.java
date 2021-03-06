package org.jcy.timeline.swt.ui;

import org.jcy.timeline.core.model.Item;
import org.jcy.timeline.core.ui.AutoUpdate;
import org.jcy.timeline.core.ui.ItemViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.jcy.timeline.util.Messages;

import static org.jcy.timeline.util.Assertion.checkArgument;

class SwtAutoUpdate<T extends Item> implements AutoUpdate<T, Composite> {

    private final ItemViewer<T, Composite> itemViewer;
    private final Header<T> header;
    private final Display display;
    private final int delay;

    private volatile boolean started;

    SwtAutoUpdate(Header<T> header, ItemViewer<T, Composite> itemViewer, int delay) {
        checkArgument(header != null, Messages.get("HEADER_MUST_NOT_BE_NULL"));
        checkArgument(itemViewer != null, Messages.get("ITEM_VIEWER_MUST_NOT_BE_NULL"));
        checkArgument(delay >= 0, Messages.get("DELAY_MUST_NOT_BE_NEGATIVE"));

        this.display = Display.getCurrent();
        this.itemViewer = itemViewer;
        this.header = header;
        this.delay = delay;
    }

    @Override
    public void start() {
        started = true;
        display.timerExec(delay, () -> triggerUpdate());
    }

    @Override
    public void stop() {
        started = false;
    }

    private void triggerUpdate() {
        if (started) {
            itemViewer.update();
            header.update();
            start();
        }
    }
}
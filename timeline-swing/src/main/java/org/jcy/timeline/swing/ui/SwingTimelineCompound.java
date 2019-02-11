package org.jcy.timeline.swing.ui;

import org.jcy.timeline.core.model.Item;
import org.jcy.timeline.core.model.ItemProvider;
import org.jcy.timeline.core.model.SessionStorage;
import org.jcy.timeline.core.model.Timeline;
import org.jcy.timeline.core.ui.AutoUpdate;
import org.jcy.timeline.core.ui.ItemUiFactory;
import org.jcy.timeline.core.ui.ItemViewer;
import org.jcy.timeline.util.BackgroundProcessor;
import org.jcy.timeline.util.Messages;

import java.awt.*;

import static org.jcy.timeline.util.Assertion.checkArgument;

class SwingTimelineCompound<T extends Item> {

    private final ItemViewer<T, Container> itemViewer;
    private final AutoUpdate<T, Container> autoUpdate;
    private final Header<T> header;

    static BackgroundProcessor createBackgroundProcessor() {
        return new BackgroundProcessor(new SwingUiThreadDispatcher());
    }

    SwingTimelineCompound(
            ItemProvider<T> itemProvider, ItemUiFactory<T, Container> itemUiFactory, SessionStorage<T> sessionStorage) {
        checkArgument(itemProvider != null, Messages.get("ITEM_PROVIDER_MUST_NOT_BE_NULL"));
        checkArgument(itemUiFactory != null, Messages.get("ITEM_UI_FACTORY_MUST_NOT_BE_NULL"));
        checkArgument(sessionStorage != null, Messages.get("SESSION_STORAGE_MUST_NOT_BE_NULL"));

        Timeline<T> timeline = new Timeline<>(itemProvider, sessionStorage);
        itemViewer = new ItemViewer<>(new SwingItemViewerCompound<>(timeline, itemUiFactory));
        header = new Header<T>(timeline);
        autoUpdate = new SwingAutoUpdate<>(header, itemViewer, 5_000);
    }

    ItemViewer<T, Container> getItemViewer() {
        return itemViewer;
    }

    Header<T> getHeader() {
        return header;
    }

    AutoUpdate<T, Container> getAutoUpdate() {
        return autoUpdate;
    }
}
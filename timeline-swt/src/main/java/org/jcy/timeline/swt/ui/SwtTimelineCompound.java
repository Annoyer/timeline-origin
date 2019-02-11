package org.jcy.timeline.swt.ui;

import org.jcy.timeline.core.model.Item;
import org.jcy.timeline.core.model.ItemProvider;
import org.jcy.timeline.core.model.SessionStorage;
import org.jcy.timeline.core.model.Timeline;
import org.jcy.timeline.core.ui.AutoUpdate;
import org.jcy.timeline.core.ui.ItemUiFactory;
import org.jcy.timeline.core.ui.ItemViewer;
import org.jcy.timeline.util.BackgroundProcessor;
import org.eclipse.swt.widgets.Composite;
import org.jcy.timeline.util.Messages;

import static org.jcy.timeline.util.Assertion.checkArgument;

class SwtTimelineCompound<T extends Item> {

    private final ItemViewer<T, Composite> itemViewer;
    private final AutoUpdate<T, Composite> autoUpdate;
    private final Header<T> header;

    static BackgroundProcessor createBackgroundProcessor() {
        return new BackgroundProcessor(new SwtUiThreadDispatcher());
    }

    SwtTimelineCompound(
            ItemProvider<T> itemProvider, ItemUiFactory<T, Composite> itemUiFactory, SessionStorage<T> sessionStorage) {
        checkArgument(itemProvider != null, Messages.get("ITEM_PROVIDER_MUST_NOT_BE_NULL"));
        checkArgument(itemUiFactory != null, Messages.get("ITEM_UI_FACTORY_MUST_NOT_BE_NULL"));
        checkArgument(sessionStorage != null, Messages.get("SESSION_STORAGE_MUST_NOT_BE_NULL"));

        Timeline<T> timeline = new Timeline<>(itemProvider, sessionStorage);
        itemViewer = new ItemViewer<>(new SwtItemViewerCompound<>(timeline, itemUiFactory));
        header = new Header<T>(timeline);
        autoUpdate = new SwtAutoUpdate<>(header, itemViewer, 5_000);
    }

    ItemViewer<T, Composite> getItemViewer() {
        return itemViewer;
    }

    Header<T> getHeader() {
        return header;
    }

    AutoUpdate<T, Composite> getAutoUpdate() {
        return autoUpdate;
    }
}
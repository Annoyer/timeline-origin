package org.jcy.timeline.swt.ui;

import org.jcy.timeline.core.model.Item;
import org.jcy.timeline.core.model.Timeline;
import org.jcy.timeline.core.ui.*;
import org.eclipse.swt.widgets.Composite;
import org.jcy.timeline.util.Messages;

import static org.jcy.timeline.util.Assertion.checkArgument;

class SwtItemViewerCompound<T extends Item> implements ItemViewerCompound<T, Composite> {

    private final SwtTopItemUpdater<T> topItemUpdater;
    private final SwtTopItemScroller<T> scroller;
    private final SwtItemUiList<T> itemUiList;

    SwtItemViewerCompound(Timeline<T> timeline, ItemUiFactory<T, Composite> itemUiFactory) {
        checkArgument(timeline != null, Messages.get("TIMELINE_MUST_NOT_BE_NULL"));
        checkArgument(itemUiFactory != null, Messages.get("ITEM_UI_FACTORY_MUST_NOT_BE_NULL"));

        ItemUiMap<T, Composite> itemUiMap = new ItemUiMap<>(timeline, itemUiFactory);
        itemUiList = new SwtItemUiList<T>(itemUiMap);
        scroller = new SwtTopItemScroller<>(timeline, itemUiMap, itemUiList);
        topItemUpdater = new SwtTopItemUpdater<>(timeline, itemUiMap, itemUiList);
    }

    @Override
    public TopItemUpdater<T, Composite> getTopItemUpdater() {
        return topItemUpdater;
    }

    @Override
    public TopItemScroller<T> getScroller() {
        return scroller;
    }

    @Override
    public ItemUiList<T, Composite> getItemUiList() {
        return itemUiList;
    }
}
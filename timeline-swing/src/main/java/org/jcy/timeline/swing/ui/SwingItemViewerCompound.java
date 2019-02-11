package org.jcy.timeline.swing.ui;

import org.jcy.timeline.core.model.Item;
import org.jcy.timeline.core.model.Timeline;
import org.jcy.timeline.core.ui.*;
import org.jcy.timeline.util.Messages;

import java.awt.*;

import static org.jcy.timeline.util.Assertion.checkArgument;

class SwingItemViewerCompound<T extends Item> implements ItemViewerCompound<T, Container> {

    private final SwingTopItemUpdater<T> topItemUpdater;
    private final SwingTopItemScroller<T> scroller;
    private final SwingItemUiList<T> itemUiList;

    SwingItemViewerCompound(Timeline<T> timeline, ItemUiFactory<T, Container> itemUiFactory) {
        checkArgument(timeline != null, Messages.get("TIMELINE_MUST_NOT_BE_NULL"));
        checkArgument(itemUiFactory != null, Messages.get("ITEM_UI_FACTORY_MUST_NOT_BE_NULL"));

        ItemUiMap<T, Container> itemUiMap = new ItemUiMap<>(timeline, itemUiFactory);
        itemUiList = new SwingItemUiList<T>(itemUiMap);
        scroller = new SwingTopItemScroller<>(timeline, itemUiMap, itemUiList);
        topItemUpdater = new SwingTopItemUpdater<>(timeline, itemUiMap, itemUiList);
    }

    @Override
    public TopItemUpdater<T, Container> getTopItemUpdater() {
        return topItemUpdater;
    }

    @Override
    public TopItemScroller<T> getScroller() {
        return scroller;
    }

    @Override
    public ItemUiList<T, Container> getItemUiList() {
        return itemUiList;
    }
}
package org.jcy.timeline.core.ui;

import org.jcy.timeline.core.model.Item;

public interface ItemViewerCompound<T extends Item, U> {
    ItemUiList<T, U> getItemUiList();

    TopItemScroller<T> getScroller();

    TopItemUpdater<T, U> getTopItemUpdater();
}
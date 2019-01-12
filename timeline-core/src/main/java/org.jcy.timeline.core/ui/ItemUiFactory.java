package org.jcy.timeline.core.ui;

import org.jcy.timeline.core.model.Item;

public interface ItemUiFactory<T extends Item, U> {
    ItemUi<T> create(U uiContext, T item, int index);
}
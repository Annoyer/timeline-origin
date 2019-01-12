package org.jcy.timeline.core.ui;

import org.jcy.timeline.core.model.Item;

public interface TopItemScroller<T extends Item> {
    void scrollIntoView();
}

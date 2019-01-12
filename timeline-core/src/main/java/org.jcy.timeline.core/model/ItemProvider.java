package org.jcy.timeline.core.model;

import java.util.List;

public interface ItemProvider<T extends Item> {
    List<T> fetchItems(T ancestor, int fetchCount);

    int getNewCount(T predecessor);

    List<T> fetchNew(T predecessor);
}
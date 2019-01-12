package org.jcy.timeline.core.ui;

import org.jcy.timeline.core.model.Item;

public interface AutoUpdate<T extends Item, U> {
    void start();

    void stop();
}
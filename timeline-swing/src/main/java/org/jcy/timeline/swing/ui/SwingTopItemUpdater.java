package org.jcy.timeline.swing.ui;

import org.jcy.timeline.core.model.Item;
import org.jcy.timeline.core.model.Timeline;
import org.jcy.timeline.core.ui.ItemUi;
import org.jcy.timeline.core.ui.ItemUiMap;
import org.jcy.timeline.core.ui.TopItemUpdater;
import org.jcy.timeline.util.UiThreadDispatcher;

import javax.swing.*;
import java.awt.*;

class SwingTopItemUpdater<T extends Item> extends TopItemUpdater<T, Container> {

    private final UiThreadDispatcher uiThreadDispatcher;
    private final SwingItemUiList<T> itemUiList;

    SwingTopItemUpdater(Timeline<T> timeline, ItemUiMap<T, Container> itemUiMap, SwingItemUiList<T> itemUiList) {
        this(timeline, itemUiMap, itemUiList, new SwingUiThreadDispatcher());
    }

    SwingTopItemUpdater(
            Timeline<T> timeline, ItemUiMap<T, Container> map, SwingItemUiList<T> list, UiThreadDispatcher dispatcher) {
        super(timeline, map);
        this.uiThreadDispatcher = dispatcher;
        this.itemUiList = list;
    }

    @Override
    public void register() {
        JScrollPane jScrollPane = (JScrollPane) itemUiList.getUiRoot();
        BoundedRangeModel model = jScrollPane.getVerticalScrollBar().getModel();
        uiThreadDispatcher.dispatch(() -> model.addChangeListener(evt -> update()));
    }

    @Override
    protected boolean isBelowTop(ItemUi<T> itemUi) {
        Component component = ((SwingItemUi<T>) itemUi).getComponent();
        if (component.isShowing()) {
            return isBelowTop(component);
        }
        return false;
    }

    private boolean isBelowTop(Component component) {
        double y1 = itemUiList.getUiRoot().getLocationOnScreen().getY();
        double y2 = component.getLocationOnScreen().getY();
        return y2 - y1 >= 0;
    }
}
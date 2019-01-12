package org.jcy.timeline.swing.ui;

import org.jcy.timeline.core.model.Item;
import org.jcy.timeline.core.model.Timeline;
import org.jcy.timeline.core.ui.ItemUiMap;
import org.jcy.timeline.core.ui.TopItemScroller;
import org.jcy.timeline.util.UiThreadDispatcher;

import javax.swing.*;
import java.awt.*;

class SwingTopItemScroller<T extends Item> implements TopItemScroller<T> {

    static final int TOP_POSITION = 5;

    private final UiThreadDispatcher uiThreadDispatcher;
    private final ItemUiMap<T, Container> itemUiMap;
    private final SwingItemUiList<T> itemUiList;
    private final Timeline<T> timeline;

    SwingTopItemScroller(Timeline<T> timeline, ItemUiMap<T, Container> itemUiMap, SwingItemUiList<T> itemUiList) {
        this(timeline, itemUiMap, itemUiList, new SwingUiThreadDispatcher());
    }

    SwingTopItemScroller(Timeline<T> timeline,
                         ItemUiMap<T, Container> itemUiMap,
                         SwingItemUiList<T> itemUiList,
                         UiThreadDispatcher dispatcher) {
        this.uiThreadDispatcher = dispatcher;
        this.itemUiList = itemUiList;
        this.itemUiMap = itemUiMap;
        this.timeline = timeline;
    }

    public void scrollIntoView() {
        uiThreadDispatcher.dispatch(() -> doScrollIntoView());
    }

    public void doScrollIntoView() {
        if (timeline.getTopItem().isPresent()) {
            SwingItemUi<T> itemUi = getItemUi();
            if (itemUi != null) {
                Component component = itemUi.getComponent();
                if (component.isShowing()) {
                    updateVerticalScrollbarSelection(component);
                }
            }
        }
    }

    void setScrollbarSelection(int newValue) {
        getContentPane().getVerticalScrollBar().getModel().setValue(newValue);
    }

    private SwingItemUi<T> getItemUi() {
        T item = timeline.getTopItem().get();
        return (SwingItemUi<T>) itemUiMap.findByItemId(item.getId());
    }

    private void updateVerticalScrollbarSelection(Component component) {
        double verticalLocation = computeVerticalLocation(component);
        if (verticalLocation < TOP_POSITION) {
            updateVerticalScrollbarSelection(verticalLocation);
        }
    }

    private void updateVerticalScrollbarSelection(double verticalLocation) {
        int oldValue = getContentPane().getVerticalScrollBar().getModel().getValue();
        setScrollbarSelection(oldValue - 15 + (int) verticalLocation);
    }

    private double computeVerticalLocation(Component component) {
        double y1 = itemUiList.getUiRoot().getLocationOnScreen().getY();
        double y2 = component.getLocationOnScreen().getY();
        return y2 - y1;
    }

    private JScrollPane getContentPane() {
        return (JScrollPane) itemUiList.getUiRoot();
    }
}
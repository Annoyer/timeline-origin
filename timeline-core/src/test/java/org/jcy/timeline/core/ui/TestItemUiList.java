package org.jcy.timeline.core.ui;

import org.jcy.timeline.core.model.Item;
import org.jcy.timeline.util.BackgroundProcessor;

class TestItemUiList extends ItemUiList<Item, Object> {

    private final Object content;

    TestItemUiList(ItemUiMap<Item, Object> itemUiMap, BackgroundProcessor backgroundProcessor) {
        super(itemUiMap, backgroundProcessor);
        content = new Object();
    }

    @Override
    protected void createUi(Object parent) {
    }

    @Override
    protected void beforeContentUpdate() {
    }

    @Override
    protected void afterContentUpdate() {
    }

    @Override
    protected Object getContent() {
        return content;
    }

    @Override
    protected Object getUiRoot() {
        return null;
    }
}
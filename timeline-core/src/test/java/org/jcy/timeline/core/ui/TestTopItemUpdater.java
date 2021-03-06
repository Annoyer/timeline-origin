package org.jcy.timeline.core.ui;

import org.jcy.timeline.core.model.FakeItem;
import org.jcy.timeline.core.model.Timeline;

import java.util.ArrayList;
import java.util.List;

class TestTopItemUpdater extends TopItemUpdater<FakeItem, Object> {

    private final List<ItemUi<FakeItem>> itemsBelowTop;

    TestTopItemUpdater(Timeline<FakeItem> timeline, ItemUiMap<FakeItem, Object> itemUiMap) {
        super(timeline, itemUiMap);
        itemsBelowTop = new ArrayList<>();
    }

    public void setBelowTop(FakeItem... itemsBelowTop) {
        this.itemsBelowTop.clear();
        for (FakeItem fakeItem : itemsBelowTop) {
            this.itemsBelowTop.add(itemUiMap.findByItemId(fakeItem.getId()));
        }
    }

    @Override
    protected void register() {
    }

    @Override
    protected boolean isBelowTop(ItemUi<FakeItem> itemUi) {
        return itemsBelowTop.contains(itemUi);
    }
}
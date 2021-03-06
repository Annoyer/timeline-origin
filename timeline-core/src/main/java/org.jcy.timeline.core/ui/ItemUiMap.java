package org.jcy.timeline.core.ui;

import org.jcy.timeline.core.model.Item;
import org.jcy.timeline.core.model.Timeline;
import org.jcy.timeline.util.Messages;

import java.util.HashMap;
import java.util.Map;

import static org.jcy.timeline.util.Assertion.checkArgument;

public class ItemUiMap<T extends Item, U> {

    protected final ItemUiFactory<T, U> itemUiFactory;
    protected final Map<String, ItemUi<T>> itemUis;
    protected final Timeline<T> timeline;

    public ItemUiMap(Timeline<T> timeline, ItemUiFactory<T, U> itemUiFactory) {
        checkArgument(timeline != null, Messages.get("TIMELINE_MUST_NOT_BE_NULL"));
        checkArgument(itemUiFactory != null, Messages.get("ITEM_UI_FACTORY_MUST_NOT_BE_NULL"));

        this.itemUiFactory = itemUiFactory;
        this.timeline = timeline;
        this.itemUis = new HashMap<>();
    }

    public boolean containsItemUi(String id) {
        checkArgument(id != null, Messages.get("ID_MUST_NOT_BE_NULL"));

        return itemUis.containsKey(id);
    }

    public ItemUi<T> findByItemId(String id) {
        checkArgument(id != null, Messages.get("ID_MUST_NOT_BE_NULL"));
        checkArgument(containsItemUi(id), Messages.get("UNKNOWN_ITEM_UI_ENTRY"), id);

        return itemUis.get(id);
    }

    public boolean isTimelineEmpty() {
        return timeline.getItems().isEmpty();
    }

    public void fetch(FetchOperation operation) {
        checkArgument(operation != null, Messages.get("OPERATION_MUST_NOT_BE_NULL"));

        operation.fetch(timeline);
    }

    public void update(U uiContext) {
        checkArgument(uiContext != null, Messages.get("UI_CONTEXT_MUST_NOT_BE_NULL"));

        timeline.getItems().forEach(item -> doUpdate(uiContext, item));
    }

    private void doUpdate(U uiContext, T item) {
        if (containsItemUi(item.getId())) {
            findByItemId(item.getId()).update();
        } else {
            createItem(uiContext, item);
        }
    }

    private void createItem(U uiContext, T item) {
        int index = timeline.getItems().indexOf(item);
        ItemUi<T> itemUi = itemUiFactory.create(uiContext, item, index);
        itemUis.put(item.getId(), itemUi);
    }
}
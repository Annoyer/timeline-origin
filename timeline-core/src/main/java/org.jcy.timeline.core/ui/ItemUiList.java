package org.jcy.timeline.core.ui;

import org.jcy.timeline.core.model.Item;
import org.jcy.timeline.util.BackgroundProcessor;

import static org.jcy.timeline.util.Assertion.checkArgument;

public abstract class ItemUiList<T extends Item, U> {

    static final String OPERATION_MUST_NOT_BE_NULL = "Argument 'operation' must not be null.";

    protected final BackgroundProcessor backgroundProcessor;
    protected final ItemUiMap<T, U> itemUiMap;

    protected ItemUiList(ItemUiMap<T, U> itemUiMap, BackgroundProcessor backgroundProcessor) {
        this.backgroundProcessor = backgroundProcessor;
        this.itemUiMap = itemUiMap;
    }

    public boolean isTimelineEmpty() {
        return itemUiMap.isTimelineEmpty();
    }

    public void fetchInBackground(FetchOperation operation) {
        checkArgument(operation != null, OPERATION_MUST_NOT_BE_NULL);

        backgroundProcessor.process(() -> fetch(operation));
    }

    public void fetch(FetchOperation operation) {
        checkArgument(operation != null, OPERATION_MUST_NOT_BE_NULL);

        itemUiMap.fetch(operation);
        backgroundProcessor.dispatchToUiThread(() -> update());
    }

    public void update() {
        beforeContentUpdate();
        itemUiMap.update(getContent());
        afterContentUpdate();
    }

    protected abstract void beforeContentUpdate();

    protected abstract void afterContentUpdate();

    protected abstract void createUi(U parent);

    protected abstract U getContent();

    protected abstract U getUiRoot();
}
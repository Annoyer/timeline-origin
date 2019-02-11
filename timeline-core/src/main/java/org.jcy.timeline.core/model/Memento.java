package org.jcy.timeline.core.model;

import org.jcy.timeline.util.Messages;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.jcy.timeline.util.Assertion.checkArgument;

public class Memento<T extends Item> {

    private static final Memento<?> EMPTY_MEMENTO = new Memento<>(new HashSet<>(), Optional.empty());

    private final Optional<T> topItem;
    private final Set<T> items;

    @SuppressWarnings("unchecked")
    public static <T extends Item> Memento<T> empty() {
        return (Memento<T>) EMPTY_MEMENTO;
    }

    public Memento(Set<T> items, Optional<T> topItem) {
        checkArgument(items != null, Messages.get("ARGUMENT_ITEMS_MUST_NOT_BE_NULL"));
        checkArgument(topItem != null, Messages.get("ARGUMENT_TOP_ITEM_MUST_NOT_BE_NULL"));
        checkArgument(topItemExistsIfItemsNotEmpty(items, topItem), Messages.get("TOP_ITEM_IS_MISSING"));
        checkArgument(topItemIsElementOfItems(items, topItem), Messages.get("TOP_ITEM_IS_UNRELATED"));

        this.items = new HashSet<>(items);
        this.topItem = topItem;
    }

    public Set<T> getItems() {
        return new HashSet<>(items);
    }

    public Optional<T> getTopItem() {
        return topItem;
    }

    private static <T> boolean topItemExistsIfItemsNotEmpty(Set<T> items, Optional<T> topItem) {
        return !topItem.isPresent() && items.isEmpty()
                || topItem.isPresent() && !items.isEmpty()
                || items.isEmpty();
    }

    private static <T> boolean topItemIsElementOfItems(Set<T> items, Optional<T> topItem) {
        return items.isEmpty() && !topItem.isPresent()
                || !items.isEmpty() && items.contains(topItem.get());
    }
}
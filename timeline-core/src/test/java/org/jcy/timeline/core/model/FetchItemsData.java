package org.jcy.timeline.core.model;

import java.util.HashSet;
import java.util.Optional;

import static org.jcy.timeline.core.model.FakeItems.*;
import static org.jcy.timeline.core.model.Timeline.*;
import static java.util.Arrays.asList;

enum FetchItemsData {

    ON_DEFAULT_FETCH_COUNT_BOUND {
        @Override
        void init() {
            FakeItem[] itemPool = createItems(DEFAULT_FETCH_COUNT * 3);
            setInput(itemPool);
            FakeItem[] fetchedItems = subArray(reverse(itemPool), 0, DEFAULT_FETCH_COUNT * 2);
            setOutput(fetchedItems);
            setMemento(createMemento(fetchedItems[0], fetchedItems));
        }
    },

    ON_LOWER_FETCH_COUNT_BOUND {
        @Override
        void init() {
            setInput(FIRST_ITEM, SECOND_ITEM, THIRD_ITEM);
            setFetchCount(FETCH_COUNT_LOWER_BOUND);
            setOutput(THIRD_ITEM, SECOND_ITEM);
            setMemento(createMemento(THIRD_ITEM, THIRD_ITEM, SECOND_ITEM));
        }
    },

    ON_UPPER_FETCH_COUNT_BOUND {
        @Override
        void init() {
            FakeItem[] itemPool = createItems(FETCH_COUNT_UPPER_BOUND * 3);
            setInput(itemPool);
            setFetchCount(FETCH_COUNT_UPPER_BOUND);
            FakeItem[] fetchedItems = subArray(reverse(itemPool), 0, FETCH_COUNT_UPPER_BOUND * 2);
            setOutput(fetchedItems);
            setMemento(createMemento(fetchedItems[0], fetchedItems));
        }
    },

    WITH_NO_ITEMS_AVAILABLE {
        @Override
        void init() {
        }
    },

    WITH_ITEMS_OF_SAME_DATE {
        @Override
        void init() {
            FakeItem first = new FakeItem("1", 10);
            FakeItem second = new FakeItem("2", 10);
            setInput(first, second);
            setFetchCount(1);
            setOutput(second, first);
            setMemento(createMemento(second, first, second));
        }
    },

    WITH_FETCH_COUNT_EXCEEDS_AVAILABLE_ITEMS {
        @Override
        void init() {
            setInput(FIRST_ITEM, SECOND_ITEM, THIRD_ITEM);
            setFetchCount(2);
            setOutput(THIRD_ITEM, SECOND_ITEM, FIRST_ITEM);
            setMemento(createMemento(THIRD_ITEM, THIRD_ITEM, SECOND_ITEM, FIRST_ITEM));
        }
    };

    private int fetchCount;
    private FakeItem[] input;
    private FakeItem[] output;
    private Memento<FakeItem> memento;

    FetchItemsData() {
        fetchCount = Timeline.DEFAULT_FETCH_COUNT;
        input = new FakeItem[0];
        output = new FakeItem[0];
        memento = Memento.empty();
        init();
    }

    abstract void init();

    void setInput(FakeItem... input) {
        this.input = input;
    }

    void setFetchCount(int fetchCount) {
        this.fetchCount = fetchCount;
    }

    void setOutput(FakeItem... output) {
        this.output = output;
    }

    void setMemento(Memento<FakeItem> memento) {
        this.memento = memento;
    }

    int getPageSize() {
        return fetchCount;
    }

    FakeItem[] getInput() {
        return input;
    }

    FakeItem[] getOutput() {
        return output;
    }

    public Memento<FakeItem> getMemento() {
        return memento;
    }

    static Memento<FakeItem> createMemento(FakeItem topItem, FakeItem... items) {
        return new Memento<>(new HashSet<>(asList(items)), Optional.of(topItem));
    }
}
package org.jcy.timeline.core.model;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.jcy.timeline.core.model.FakeItems.*;
import static org.jcy.timeline.core.model.Memento.*;
import static org.jcy.timeline.test.util.ThrowableCaptor.thrownBy;
import static org.assertj.core.api.Assertions.assertThat;

public class MementoTest {

    private static final Optional<FakeItem> UNRELATED_ITEM = Optional.of(new FakeItem("id", 2));

    private Memento<FakeItem> memento;
    private HashSet<FakeItem> givenItems;

    @Before
    public void setup() {
        givenItems = new HashSet<>(ALL_ITEMS);
        memento = new Memento<>(givenItems, Optional.of(FIRST_ITEM));
    }

    @Test
    public void getItems() {
        Set<FakeItem> actual = memento.getItems();

        assertThat(actual).isEqualTo(ALL_ITEMS);
    }

    @Test
    public void getTopItem() {
        Optional<FakeItem> actual = memento.getTopItem();

        assertThat(actual).contains(FIRST_ITEM);
    }

    @Test
    public void getItemsIfGivenItemSetChanges() {
        givenItems.remove(SECOND_ITEM);

        Set<FakeItem> actual = memento.getItems();

        assertThat(actual).isEqualTo(ALL_ITEMS);
    }

    @Test
    public void getItemsIfPreviouslyReturnedItemSetChanges() {
        Set<FakeItem> resultSet = memento.getItems();

        resultSet.remove(SECOND_ITEM);
        Set<FakeItem> actual = memento.getItems();

        assertThat(actual).isEqualTo(ALL_ITEMS);
    }

    @Test
    public void constructWithEmptyItemsAndWithoutTopItem() {
        Memento<Item> actual = Memento.empty();

        assertThat(actual.getItems()).isEmpty();
        assertThat(actual.getTopItem()).isEmpty();
    }

    @Test
    public void constructWithEmptyItemsButWitTopItem() {
        Throwable actual = thrownBy(() -> new Memento<>(new HashSet<>(), UNRELATED_ITEM));

        assertThat(actual)
                .hasMessage(TOP_ITEM_IS_UNRELATED)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void constructWithItemsButWithoutTopItem() {
        Throwable actual = thrownBy(() -> new Memento<>(ALL_ITEMS, Optional.empty()));

        assertThat(actual)
                .hasMessage(TOP_ITEM_IS_MISSING)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void constructWithItemsAndUnrelatedTopItem() {
        Throwable actual = thrownBy(() -> new Memento<>(ALL_ITEMS, UNRELATED_ITEM));

        assertThat(actual)
                .hasMessage(TOP_ITEM_IS_UNRELATED)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void constructWithNullAsItems() {
        Throwable actual = thrownBy(() -> new Memento<>(null, Optional.empty()));

        assertThat(actual)
                .hasMessage(ARGUMENT_ITEMS_MUST_NOT_BE_NULL)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void constructWithNullAsTopItem() {
        Throwable actual = thrownBy(() -> new Memento<>(new HashSet<>(), null));

        assertThat(actual)
                .hasMessage(ARGUMENT_TOP_ITEM_MUST_NOT_BE_NULL)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void empty() {
        Memento<Item> expected = new Memento<>(new HashSet<>(), Optional.empty());

        Memento<Item> actual = Memento.empty();

        MementoAssert.assertThat(actual).isEqualTo(expected);
    }
}
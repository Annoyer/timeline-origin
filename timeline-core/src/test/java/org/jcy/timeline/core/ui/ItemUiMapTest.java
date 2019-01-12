package org.jcy.timeline.core.ui;

import org.jcy.timeline.core.model.FakeItem;
import org.jcy.timeline.core.model.Timeline;
import org.junit.Before;
import org.junit.Test;

import static org.jcy.timeline.core.model.FakeItems.FIRST_ITEM;
import static org.jcy.timeline.test.util.ThrowableCaptor.thrownBy;
import static org.jcy.timeline.core.ui.ItemUiMap.*;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class ItemUiMapTest {

    private static final int ITEM_INDEX = 0;

    private ItemUiMap<FakeItem, Object> itemUiMap;
    private Timeline<FakeItem> timeline;
    private ItemUi<FakeItem> itemUi;
    private Object uiContext;
    private FakeItem item;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() {
        uiContext = new Object();
        item = FIRST_ITEM;
        timeline = stubTimeline(item);
        itemUi = mock(ItemUi.class);
        itemUiMap = new ItemUiMap<>(timeline, stubItemUiFactory(itemUi, uiContext, item, ITEM_INDEX));
    }

    @Test
    public void updateOfItemsWithoutUi() {
        itemUiMap.update(uiContext);

        itemUiMap.update(uiContext);
        ItemUi<FakeItem> actual = itemUiMap.findByItemId(item.getId());


        assertThat(actual).isEqualTo(itemUi);
    }

    @Test
    public void updateOfItemsWithUi() {
        itemUiMap.update(uiContext);

        itemUiMap.update(uiContext);
        ItemUi<FakeItem> actual = itemUiMap.findByItemId(item.getId());

        verify(actual).update();
    }

    @Test
    public void updateWithNullAsUiContext() {
        Throwable actual = thrownBy(() -> itemUiMap.update(null));

        assertThat(actual)
                .hasMessageContaining(UI_CONTEXT_MUST_NOT_BE_NULL)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void findByItemId() {
        itemUiMap.update(uiContext);

        ItemUi<FakeItem> actual = itemUiMap.findByItemId(item.getId());

        assertThat(actual).isSameAs(itemUi);
    }

    @Test
    public void findByItemIdForUnknownItem() {
        Throwable actual = thrownBy(() -> itemUiMap.findByItemId(item.getId()));

        assertThat(actual)
                .hasMessageContaining(item.getId())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void findByItemIdWithNullAsId() {
        Throwable actual = thrownBy(() -> itemUiMap.findByItemId(null));

        assertThat(actual)
                .hasMessageContaining(ID_MUST_NOT_BE_NULL)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void containsItemUi() {
        itemUiMap.update(uiContext);

        boolean actual = itemUiMap.containsItemUi(item.getId());

        assertThat(actual).isTrue();
    }

    @Test
    public void containsItemUiForUnknownItem() {
        boolean actual = itemUiMap.containsItemUi(item.getId());

        assertThat(actual).isFalse();
    }

    @Test
    public void containsItemUiWithNullAsId() {
        Throwable actual = thrownBy(() -> itemUiMap.containsItemUi(null));

        assertThat(actual)
                .hasMessageContaining(ID_MUST_NOT_BE_NULL)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void isTimelineEmptyIfTimelineContainsNoItems() {
        equipTimeline(timeline);

        boolean actual = itemUiMap.isTimelineEmpty();

        assertThat(actual).isTrue();
    }

    @Test
    public void isTimelineEmptyIfTimelineContainsItems() {
        equipTimeline(timeline, item);

        boolean actual = itemUiMap.isTimelineEmpty();

        assertThat(actual).isFalse();
    }

    @Test
    public void fetch() {
        FetchOperation operation = mock(FetchOperation.class);

        itemUiMap.fetch(operation);

        verify(operation).fetch(timeline);
    }

    @Test
    public void fetchWithNullAsOperation() {
        Throwable actual = thrownBy(() -> itemUiMap.fetch(null));

        assertThat(actual)
                .hasMessage(OPERATION_MUST_NOT_BE_NULL)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void constructWithNullAsTimeline() {
        Throwable actual = thrownBy(() -> new ItemUiMap<>(null, stubItemUiFactory(null, null, null, 0)));

        assertThat(actual)
                .hasMessage(TIMELINE_MUST_NOT_BE_NULL)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void constructWithNullAsItemUiFactory() {
        Throwable actual = thrownBy(() -> new ItemUiMap<>(timeline, null));

        assertThat(actual)
                .hasMessage(ITEM_UI_FACTORY_MUST_NOT_BE_NULL)
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static ItemUiFactory<FakeItem, Object> stubItemUiFactory(
            ItemUi<FakeItem> expected, Object uiContext, FakeItem item, int itemIndex) {
        @SuppressWarnings("unchecked")
        ItemUiFactory<FakeItem, Object> result = mock(ItemUiFactory.class);
        when(result.create(uiContext, item, itemIndex)).thenReturn(expected);
        return result;
    }

    private static Timeline<FakeItem> stubTimeline(FakeItem... items) {
        @SuppressWarnings("unchecked")
        Timeline<FakeItem> result = mock(Timeline.class);
        equipTimeline(result, items);
        return result;
    }

    private static void equipTimeline(Timeline<FakeItem> timeline, FakeItem... items) {
        when(timeline.getItems()).thenReturn(asList(items));
    }
}
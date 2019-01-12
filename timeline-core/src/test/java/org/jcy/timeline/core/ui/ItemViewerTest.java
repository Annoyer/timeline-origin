package org.jcy.timeline.core.ui;

import org.jcy.timeline.core.model.Item;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import static org.jcy.timeline.core.ui.FetchOperation.MORE;
import static org.jcy.timeline.core.ui.FetchOperation.NEW;
import static org.jcy.timeline.core.ui.ItemViewerCompoundHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class ItemViewerTest {

    private TopItemUpdater<Item, Object> topItemUpdater;
    private TopItemScroller<Item> scroller;
    private ItemUiList<Item, Object> itemUiList;
    private ItemViewer<Item, Object> itemViewer;

    @Before
    public void setUp() {
        itemUiList = stubItemList();
        scroller = stubScroller();
        topItemUpdater = stubTopItemUpdater();
        itemViewer = new ItemViewer<>(stubItemViewerCompound(itemUiList, scroller, topItemUpdater));
    }

    @Test
    public void createUi() {
        Object parent = new Object();

        itemViewer.createUi(parent);

        verify(itemUiList).createUi(parent);
    }

    @Test
    public void getUiRoot() {
        Object expected = new Object();
        when(itemUiList.getUiRoot()).thenReturn(expected);

        Object actual = itemViewer.getUiRoot();

        assertThat(actual).isSameAs(expected);
    }

    @Test
    public void initializeWithEmptyTimeline() {
        when(itemUiList.isTimelineEmpty()).thenReturn(true);

        itemViewer.initialize();

        InOrder order = inOrder(itemUiList, scroller, topItemUpdater);
        order.verify(itemUiList).isTimelineEmpty();
        order.verify(itemUiList).fetch(MORE);
        order.verify(itemUiList).update();
        order.verify(scroller).scrollIntoView();
        order.verify(topItemUpdater).register();
        order.verifyNoMoreInteractions();
    }

    @Test
    public void initializeWithNonEmptyTimeline() {
        when(itemUiList.isTimelineEmpty()).thenReturn(false);

        itemViewer.initialize();

        InOrder order = inOrder(itemUiList, scroller, topItemUpdater);
        order.verify(itemUiList).isTimelineEmpty();
        order.verify(itemUiList).update();
        order.verify(scroller).scrollIntoView();
        order.verify(topItemUpdater).register();
        order.verifyNoMoreInteractions();
    }

    @Test
    public void fetchNew() {
        itemViewer.fetchNew();

        verify(itemUiList).fetch(NEW);
    }

    @Test
    public void update() {
        itemViewer.update();

        verify(itemUiList).update();
    }
}

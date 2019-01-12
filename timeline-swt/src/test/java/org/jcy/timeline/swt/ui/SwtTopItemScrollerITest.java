package org.jcy.timeline.swt.ui;

import org.jcy.timeline.core.model.Item;
import org.jcy.timeline.core.model.Timeline;
import org.jcy.timeline.test.util.swt.DisplayHelper;
import org.jcy.timeline.core.ui.ItemUiMap;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.jcy.timeline.swt.BackgroundThreadHelper.directUiThreadDispatcher;
import static org.jcy.timeline.swt.ui.SwtTopItemScroller.TOP_POSITION;
import static org.jcy.timeline.swt.ui.TopItemTestHelper.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

public class SwtTopItemScrollerITest {

    @Rule
    public final DisplayHelper displayHelper = new DisplayHelper();

    private ItemUiMap<Item, Composite> itemUiMap;
    private SwtTopItemScroller<Item> scroller;
    private SwtItemUiList<Item> itemUiList;
    private Timeline<Item> timeline;
    private Shell shell;

    @Before
    public void setUp() {
        shell = displayHelper.createShell();
        timeline = stubTimeline();
        itemUiMap = stubUiItemMap();
        itemUiList = stubUiItemList(shell);
        scroller = spy(new SwtTopItemScroller<>(timeline, itemUiMap, itemUiList, directUiThreadDispatcher()));
    }

    @Test
    public void scrollIntoViewIfBelowTop() throws Exception {
        SwtItemUi<Item> itemUi = equipItemListWithItem();
        itemUi.getControl().setLocation(0, TOP_POSITION + 1);

        scroller.scrollIntoView();

        verify(scroller).setScrollbarSelection(anyInt());
    }

    @Test
    public void scrollIntoViewIfAboveTop() throws Exception {
        SwtItemUi<Item> itemUi = equipItemListWithItem();
        itemUi.getControl().setLocation(0, TOP_POSITION - 1);

        scroller.scrollIntoView();

        verify(scroller, never()).setScrollbarSelection(anyInt());
    }

    @Test
    public void scrollIntoViewWithoutTopItem() {
        scroller.scrollIntoView();

        verify(scroller, never()).setScrollbarSelection(anyInt());
    }

    @Test
    public void scrollIntoViewWithoutItemUi() {
        equipWithTopItem(timeline, new Item("id", 20L) {
        });

        scroller.scrollIntoView();

        verify(scroller, never()).setScrollbarSelection(anyInt());
    }

    @Test
    public void scrollIntoViewIfItemUiComponentIsNotShowing() {
        Item item = equipWithTopItem(timeline, new Item("id", 20L) {
        });
        SwtItemUi<Item> itemUi = stubItemUi(displayHelper.createShell());
        map(itemUiMap, item, itemUi);

        scroller.scrollIntoView();

        verify(scroller, never()).setScrollbarSelection(anyInt());
    }

    private SwtItemUi<Item> equipItemListWithItem() throws Exception {
        Item item = equipWithTopItem(timeline, new Item("id", 20L) {
        });
        ShellHelper.showShell(shell);
        SwtItemUi<Item> result = stubItemUi(newChildComposite(itemUiList.getUiRoot()));
        map(itemUiMap, item, result);
        return result;
    }
}
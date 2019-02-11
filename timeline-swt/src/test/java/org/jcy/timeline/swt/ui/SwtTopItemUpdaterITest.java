package org.jcy.timeline.swt.ui;

import org.jcy.timeline.core.model.Item;
import org.jcy.timeline.core.model.Timeline;
import org.jcy.timeline.core.ui.ItemUiMap;
import org.jcy.timeline.test.util.swt.DisplayHelper;
import org.jcy.timeline.test.util.swt.SwtEventHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.jcy.timeline.swt.BackgroundThreadHelper.directUiThreadDispatcher;
import static org.jcy.timeline.swt.ui.ShellHelper.showShell;
import static org.jcy.timeline.swt.ui.TopItemTestHelper.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class SwtTopItemUpdaterITest {

    @Rule
    public final DisplayHelper displayHelper = new DisplayHelper();

    private ItemUiMap<Item, Composite> itemUiMap;
    private SwtTopItemUpdater<Item> updater;
    private SwtItemUiList<Item> itemUiList;
    private Timeline<Item> timeline;
    private Shell shell;

    @Before
    public void setUp() {
        shell = displayHelper.createShell();
        timeline = stubTimeline();
        itemUiMap = stubUiItemMap();
        itemUiList = stubUiItemList(shell);
        updater = spy(new SwtTopItemUpdater<>(timeline, itemUiMap, itemUiList, directUiThreadDispatcher()));
    }

    @Test
    public void updateIfBelowTop() throws Exception {
        Item item = equipItemListWithItem();
        getItemUiControl(item).setLocation(0, fromTopOffset(1));

        updater.update();

        verify(timeline).setTopItem(item);
    }

    @Test
    public void updateIfBelowTopWithDifferentTopItem() throws Exception {
        Item item = equipItemListWithItem();
        equipWithTopItem(timeline, new Item("other", 30L) {
        });
        getItemUiControl(item).setLocation(0, fromTopOffset(1));

        updater.update();

        verify(timeline).setTopItem(item);
    }

    @Test
    public void updateIfBelowTopWithEqualTopItem() throws Exception {
        Item item = equipItemListWithItem();
        equipWithTopItem(timeline, item);
        getItemUiControl(item).setLocation(0, fromTopOffset(1));

        updater.update();

        verify(timeline, never()).setTopItem(item);
    }

    @Test
    public void updateIfAboveTop() throws Exception {
        Item item = equipItemListWithItem();
        getItemUiControl(item).setLocation(0, fromTopOffset(-1));

        updater.update();

        verify(timeline, never()).setTopItem(item);
    }

    @Test
    public void updateWithoutItems() throws Exception {
        equipWithItems(timeline, new Item("id", 20L) {
        });

        updater.update();

        verify(timeline, never()).setTopItem(any(Item.class));
    }

    @Test
    public void updateWithoutItemUi() throws Exception {
        updater.update();

        verify(timeline, never()).setTopItem(any(Item.class));
    }

    @Test
    public void updateIfItemUiControlIsNotShowing() throws Exception {
        Item item = equipWithItems(timeline, new Item("id", 20L) {
        });
        SwtItemUi<Item> itemUi = stubItemUi(displayHelper.createShell());
        map(itemUiMap, item, itemUi);
        getItemUiControl(item).setLocation(0, fromTopOffset(1));

        updater.update();

        verify(timeline, never()).setTopItem(item);
    }

    @Test
    public void register() {
        updater.register();

        triggerScrollbarSelectionChange(10);

        verify(updater).update();
    }

    private Item equipItemListWithItem() throws Exception {
        Item result = equipWithItems(timeline, new Item("id", 20L) {
        });
        showShell(shell);
        SwtItemUi<Item> itemUi = stubItemUi(newChildComposite(itemUiList.getUiRoot()));
        map(itemUiMap, result, itemUi);
        return result;
    }

    private Control getItemUiControl(Item item) {
        return ((SwtItemUi<Item>) itemUiMap.findByItemId(item.getId())).getControl();
    }

    private void triggerScrollbarSelectionChange(int newValue) {
        ScrollBar verticalBar = ((ScrolledComposite) itemUiList.getUiRoot()).getVerticalBar();
        verticalBar.setSelection(newValue);
        SwtEventHelper.trigger(SWT.Selection).withDetail(newValue).on(verticalBar);
    }

    private static int fromTopOffset(int i) {
        return i - SwtTopItemUpdater.TOP_OFF_SET;
    }
}
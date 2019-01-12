package org.jcy.timeline.swing.ui;

import org.jcy.timeline.core.model.Item;
import org.jcy.timeline.core.ui.ItemUiMap;
import org.jcy.timeline.util.BackgroundProcessor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import java.awt.*;

import static org.jcy.timeline.core.ui.FetchOperation.MORE;
import static org.jcy.timeline.swing.BackgroundThreadHelper.directBackgroundProcessor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

public class SwingItemUiListITest {

    private BackgroundProcessor backgroundProcessor;
    private ItemUiMap<Item, Container> itemUiMap;
    private SwingItemUiList<Item> itemUiList;

    @Before
    public void setUp() {
        itemUiMap = stubItemUiMap();
        backgroundProcessor = directBackgroundProcessor();
        itemUiList = new SwingItemUiList<>(itemUiMap, backgroundProcessor);
    }

    @Test
    public void createUi() {
        itemUiList.createUi(null);

        assertThat(itemUiList.getUiRoot()).isNotNull();
        assertThat(itemUiList.getContent()).isNotNull();
    }

    @Test
    public void fetchMoreOnClick() {
        itemUiList.createUi(null);

        itemUiList.fetchMore.doClick();

        InOrder order = inOrder(itemUiMap, backgroundProcessor);
        order.verify(itemUiMap).fetch(MORE);
        order.verify(itemUiMap).update(itemUiList.content);
    }

    @SuppressWarnings("unchecked")
    private ItemUiMap<Item, Container> stubItemUiMap() {
        return mock(ItemUiMap.class);
    }
}
package org.jcy.timeline.swing.ui;

import org.jcy.timeline.core.model.Item;
import org.jcy.timeline.core.model.Timeline;
import org.jcy.timeline.core.ui.ItemUiFactory;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.jcy.timeline.swing.ui.SwingItemViewerCompound.ITEM_UI_FACTORY_MUST_NOT_BE_NULL;
import static org.jcy.timeline.swing.ui.SwingItemViewerCompound.TIMELINE_MUST_NOT_BE_NULL;
import static org.jcy.timeline.test.util.ThrowableCaptor.thrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class SwingItemViewerCompoundTest {

    private ItemUiFactory<Item, Container> itemUiFactory;
    private SwingItemViewerCompound<Item> compound;
    private Timeline<Item> timeline;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() {
        timeline = mock(Timeline.class);
        itemUiFactory = mock(ItemUiFactory.class);
        compound = new SwingItemViewerCompound<>(timeline, itemUiFactory);
    }

    @Test
    public void getTopItemUpdater() {
        assertThat(compound.getTopItemUpdater()).isNotNull();
    }

    @Test
    public void getScroller() {
        assertThat(compound.getScroller()).isNotNull();
    }

    @Test
    public void getItemUiList() {
        assertThat(compound.getItemUiList()).isNotNull();
    }

    @Test
    public void constructWithNullAsTimeline() {
        Throwable actual = thrownBy(() -> new SwingItemViewerCompound<>(null, itemUiFactory));

        assertThat(actual)
                .hasMessage(TIMELINE_MUST_NOT_BE_NULL)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void constructWithNullAsItemUiFactory() {
        Throwable actual = thrownBy(() -> new SwingItemViewerCompound<>(timeline, null));

        assertThat(actual)
                .hasMessage(ITEM_UI_FACTORY_MUST_NOT_BE_NULL)
                .isInstanceOf(IllegalArgumentException.class);
    }
}
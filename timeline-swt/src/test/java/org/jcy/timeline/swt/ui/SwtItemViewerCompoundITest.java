package org.jcy.timeline.swt.ui;

import org.jcy.timeline.core.model.Item;
import org.jcy.timeline.core.model.Timeline;
import org.jcy.timeline.core.ui.ItemUiFactory;
import org.jcy.timeline.test.util.swt.DisplayHelper;
import org.eclipse.swt.widgets.Composite;
import org.jcy.timeline.util.Messages;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.jcy.timeline.test.util.ThrowableCaptor.thrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class SwtItemViewerCompoundITest {

    @Rule
    public final DisplayHelper displayHelper = new DisplayHelper();

    private ItemUiFactory<Item, Composite> itemUiFactory;
    private SwtItemViewerCompound<Item> compound;
    private Timeline<Item> timeline;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() {
        displayHelper.ensureDisplay();
        timeline = mock(Timeline.class);
        itemUiFactory = mock(ItemUiFactory.class);
        compound = new SwtItemViewerCompound<>(timeline, itemUiFactory);
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
        Throwable actual = thrownBy(() -> new SwtItemViewerCompound<>(null, itemUiFactory));

        assertThat(actual)
                .hasMessage(Messages.get("TIMELINE_MUST_NOT_BE_NULL"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void constructWithNullAsItemUiFactory() {
        Throwable actual = thrownBy(() -> new SwtItemViewerCompound<>(timeline, null));

        assertThat(actual)
                .hasMessage(Messages.get("ITEM_UI_FACTORY_MUST_NOT_BE_NULL"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
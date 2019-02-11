package org.jcy.timeline.swing.ui;

import org.jcy.timeline.core.model.Item;
import org.jcy.timeline.core.ui.AutoUpdate;
import org.jcy.timeline.core.ui.ItemViewer;
import org.jcy.timeline.util.Messages;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.jcy.timeline.swing.ui.SwingAutoUpdate.*;
import static org.jcy.timeline.swing.ui.SwingTimelineCompoundHelper.stubHeader;
import static org.jcy.timeline.swing.ui.SwingTimelineCompoundHelper.stubItemViewer;
import static org.jcy.timeline.test.util.ThreadHelper.sleep;
import static org.jcy.timeline.test.util.ThrowableCaptor.thrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class SwingAutoUpdateITest {

    private static final int DELAY = 0;

    private AutoUpdate<Item, Container> autoUpdate;
    private ItemViewer<Item, Container> itemViewer;
    private Header<Item> header;

    @Before
    public void setUp() {
        header = stubHeader();
        itemViewer = stubItemViewer();
        autoUpdate = new SwingAutoUpdate<>(header, itemViewer, DELAY);
    }

    @After
    public void tearDown() {
        autoUpdate.stop();
    }

    @Test
    public void start() {
        autoUpdate.start();

        sleep(DELAY);

        verify(header, atLeastOnce()).update();
        verify(itemViewer, atLeastOnce()).update();
    }

    @Test
    public void stop() {
        autoUpdate.start();
        sleep(DELAY);

        autoUpdate.stop();
        reset(header, itemViewer);
        sleep(DELAY);

        verify(header, never()).update();
        verify(itemViewer, never()).update();
    }

    @Test
    public void constructWithNullAsHeader() {
        Throwable actual = thrownBy(() -> new SwingAutoUpdate<>(null, itemViewer, DELAY));

        assertThat(actual)
                .hasMessage(Messages.get("HEADER_MUST_NOT_BE_NULL"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void constructWithNullAsItemViewer() {
        Throwable actual = thrownBy(() -> new SwingAutoUpdate<>(header, null, DELAY));

        assertThat(actual)
                .hasMessage(Messages.get("ITEM_VIEWER_MUST_NOT_BE_NULL"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void constructWithNegativeDelay() {
        Throwable actual = thrownBy(() -> new SwingAutoUpdate<>(header, itemViewer, -1));

        assertThat(actual)
                .hasMessage(Messages.get("DELAY_MUST_NOT_BE_NEGATIVE"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
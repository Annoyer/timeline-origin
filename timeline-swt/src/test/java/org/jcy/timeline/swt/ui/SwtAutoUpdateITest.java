package org.jcy.timeline.swt.ui;

import org.jcy.timeline.core.model.Item;
import org.jcy.timeline.core.ui.ItemViewer;
import org.jcy.timeline.test.util.swt.DisplayHelper;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.jcy.timeline.util.Messages;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.jcy.timeline.swt.ui.SwtTimelineCompoundHelper.stubHeader;
import static org.jcy.timeline.swt.ui.SwtTimelineCompoundHelper.stubItemViewer;
import static org.jcy.timeline.test.util.swt.DisplayHelper.flushPendingEvents;
import static org.jcy.timeline.test.util.ThreadHelper.sleep;
import static org.jcy.timeline.test.util.ThrowableCaptor.thrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class SwtAutoUpdateITest {

    private static final int DELAY = 10;

    @Rule
    public final DisplayHelper displayHelper = new DisplayHelper();

    private SwtAutoUpdate<Item> autoUpdate;
    private ItemViewer<Item, Composite> itemViewer;
    private Header<Item> header;

    @Before
    public void setUp() {
        Shell parent = displayHelper.createShell();
        header = stubHeader(parent);
        itemViewer = stubItemViewer(parent);
        autoUpdate = new SwtAutoUpdate<>(header, itemViewer, DELAY);
    }

    @After
    public void tearDown() {
        autoUpdate.stop();
    }

    @Test
    public void start() {
        autoUpdate.start();

        waitForDelay();

        verify(header, atLeastOnce()).update();
        verify(itemViewer, atLeastOnce()).update();
    }

    @Test
    public void stop() {
        autoUpdate.start();
        waitForDelay();

        autoUpdate.stop();
        reset(header, itemViewer);
        waitForDelay();

        verify(header, never()).update();
        verify(itemViewer, never()).update();
    }

    @Test
    public void constructWithNullAsHeader() {
        Throwable actual = thrownBy(() -> new SwtAutoUpdate<>(null, itemViewer, DELAY));

        assertThat(actual)
                .hasMessage(Messages.get("HEADER_MUST_NOT_BE_NULL"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void constructWithNullAsItemViewer() {
        Throwable actual = thrownBy(() -> new SwtAutoUpdate<>(header, null, DELAY));

        assertThat(actual)
                .hasMessage(Messages.get("ITEM_VIEWER_MUST_NOT_BE_NULL"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void constructWithNegativeDelay() {
        Throwable actual = thrownBy(() -> new SwtAutoUpdate<>(header, itemViewer, -1));

        assertThat(actual)
                .hasMessage(Messages.get("DELAY_MUST_NOT_BE_NEGATIVE"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private void waitForDelay() {
        sleep(DELAY);
        flushPendingEvents();
    }
}
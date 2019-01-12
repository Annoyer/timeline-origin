package org.jcy.timeline.swt.ui;

import org.jcy.timeline.core.model.Item;
import org.jcy.timeline.test.util.swt.DisplayHelper;
import org.jcy.timeline.core.ui.ItemViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jcy.timeline.swt.ui.SwtTimelineCompoundHelper.*;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;

public class SwtTimelineITest {

    @Rule
    public final DisplayHelper displayHelper = new DisplayHelper();

    private ItemViewer<Item, Composite> itemViewer;
    private SwtAutoUpdate<Item> autoUpdate;
    private SwtTimeline<Item> timeline;
    private Header<Item> header;
    private Shell parent;

    @Before
    public void setUp() {
        parent = displayHelper.createShell();
        autoUpdate = stubAutoUpdate();
        itemViewer = stubItemViewer(parent);
        header = stubHeader(parent);
        timeline = new SwtTimeline<>(parent, stubCompound(header, itemViewer, autoUpdate));
    }

    @Test
    public void initialization() {
        InOrder order = inOrder(header, itemViewer);
        order.verify(header).createUi(any(Composite.class));
        order.verify(itemViewer).createUi(any(Composite.class));
        order.verify(itemViewer).initialize();
        order.verify(header).onFetchNew(any(Listener.class));
    }

    @Test
    public void fetchNewDelegation() {
        ArgumentCaptor<Listener> captor = forClass(Listener.class);
        verify(header).onFetchNew(captor.capture());

        captor.getValue().handleEvent(null);

        verify(itemViewer).fetchNew();
    }

    @Test
    public void getComponent() {
        assertThat(timeline.getControl()).isNotNull();
    }

    @Test
    public void startAutoRefresh() {
        timeline.startAutoRefresh();

        verify(autoUpdate).start();
    }

    @Test
    public void stopAutoRefresh() {
        timeline.stopAutoRefresh();

        verify(autoUpdate).stop();
    }

    @Test
    public void setTitle() {
        String expected = "title";

        timeline.setTitle(expected);

        verify(header).setTitle(expected);
    }
}
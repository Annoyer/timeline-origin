package org.jcy.timeline.swing.ui;

import org.jcy.timeline.core.model.Item;
import org.jcy.timeline.core.ui.ItemViewer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import java.awt.*;
import java.awt.event.ActionListener;

import static org.jcy.timeline.swing.ui.SwingTimelineCompoundHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;

public class SwingTimelineTest {

    private SwingTimeline<Item> timeline;
    private SwingAutoUpdate<Item> autoUpdate;
    private ItemViewer<Item, Container> itemViewer;
    private Header<Item> header;

    @Before
    public void setUp() {
        autoUpdate = stubAutoUpdate();
        itemViewer = stubItemViewer();
        header = stubHeader();
        timeline = new SwingTimeline<>(stubCompound(header, itemViewer, autoUpdate));
    }

    @Test
    public void initialization() {
        InOrder order = inOrder(header, itemViewer);
        order.verify(header).createUi();
        order.verify(itemViewer).createUi(null);
        order.verify(itemViewer).initialize();
        order.verify(header).onFetchNew(any(ActionListener.class));
    }

    @Test
    public void fetchNewDelegation() {
        ArgumentCaptor<ActionListener> captor = forClass(ActionListener.class);
        verify(header).onFetchNew(captor.capture());

        captor.getValue().actionPerformed(null);

        verify(itemViewer).fetchNew();
    }

    @Test
    public void getComponent() {
        assertThat(timeline.getComponent()).isNotNull();
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
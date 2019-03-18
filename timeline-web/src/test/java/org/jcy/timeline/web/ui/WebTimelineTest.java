package org.jcy.timeline.web.ui;

import org.assertj.core.util.Lists;
import org.jcy.timeline.core.model.Timeline;
import org.jcy.timeline.core.provider.git.GitItem;
import org.jcy.timeline.core.ui.FetchOperation;
import org.jcy.timeline.web.ItemFactory;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.mockito.Mockito.*;

public class WebTimelineTest {

    private static WebTimeline webTimeline;

    private static Timeline<GitItem> timeline;

    private static WebAutoUpdate autoUpdate;

    private static List<GitItem> items;

    @BeforeClass
    public static void setUp() {
        items = Lists.newArrayList(ItemFactory.createNewItems(1, 10));
        timeline = mock(Timeline.class);
        when(timeline.getItems()).thenReturn(items);

        autoUpdate = mock(WebAutoUpdate.class);
        webTimeline = new WebTimeline("1", timeline, autoUpdate);
    }

    @Test
    public void startAutoFresh() {
        webTimeline.startAutoFresh();
        verify(autoUpdate).start();
    }

    @Test
    public void stopAutoFresh() {
        webTimeline.stopAutoFresh();
        verify(autoUpdate).stop();
    }

    @Test
    public void fetch() {
        webTimeline.fetch(FetchOperation.MORE);
        verify(timeline, atLeastOnce()).fetchItems();
        verify(timeline).getItems();
    }

    @Test
    public void getItems() {
        List<GitItem> currentItems = webTimeline.getItems();
        Assert.assertEquals(items.size(), currentItems.size());
    }
}
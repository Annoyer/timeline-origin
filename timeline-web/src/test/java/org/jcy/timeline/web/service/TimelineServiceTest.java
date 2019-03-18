package org.jcy.timeline.web.service;

import com.google.common.collect.Lists;
import org.jcy.timeline.core.model.Memento;
import org.jcy.timeline.core.model.Timeline;
import org.jcy.timeline.core.provider.git.GitItem;
import org.jcy.timeline.core.provider.git.GitItemProvider;
import org.jcy.timeline.core.util.FileSessionStorage;
import org.jcy.timeline.web.ItemFactory;
import org.jcy.timeline.web.model.FetchResponse;
import org.jcy.timeline.web.model.RegisterResponse;
import org.jcy.timeline.web.ui.WebAutoUpdate;
import org.jcy.timeline.web.ui.WebTimeline;
import org.jcy.timeline.web.ui.WebTimelineFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TimelineServiceTest {

    private static final String URI = "https://github.com/Annoyer/jenkins-web-test.git";
    private static final String PROJECT_NAME = "jenkins-web-test";

    @Mock
    private WebTimelineFactory timelineFactory;
    @InjectMocks
    private TimelineService timelineService;

    private WebTimeline timeline;

    private WebTimeline timelineEmpty;

    private List<GitItem> newItems;

    private List<GitItem> moreItems;

    private GitItemProvider itemProvider;

    private GitItemProvider itemProviderEmpty;

    private FileSessionStorage<GitItem> storage;

    private String sessionId = "a";

    @Before
    public void stubTimeline() {
        newItems = Lists.newArrayList(ItemFactory.createNewItems(100, 6));
        moreItems = Lists.newArrayList(ItemFactory.createNewItems(1000, 10));

        itemProvider = mock(GitItemProvider.class);
        when(itemProvider.fetchNew(any())).thenReturn(newItems);
        when(itemProvider.fetchItems(anyObject(), anyInt())).thenReturn(moreItems);

        storage = mock(FileSessionStorage.class);
        when(storage.read()).thenReturn(Memento.empty());
        Timeline<GitItem> t = new Timeline<>(itemProvider, storage);
        WebAutoUpdate autoUpdate = new WebAutoUpdate(sessionId, t);
        timeline = spy(new WebTimeline(sessionId, t, autoUpdate));
        when(timelineFactory.create(sessionId, URI, PROJECT_NAME)).thenReturn(timeline);

        itemProviderEmpty = mock(GitItemProvider.class);
        when(itemProvider.fetchNew(any())).thenReturn(Collections.EMPTY_LIST);
        when(itemProvider.fetchItems(anyObject(), anyInt())).thenReturn(Collections.EMPTY_LIST);

        Timeline<GitItem> tE = new Timeline<>(itemProviderEmpty, storage);
        WebAutoUpdate autoUpdateE = new WebAutoUpdate(sessionId, tE);
        timelineEmpty = spy(new WebTimeline(sessionId, tE, autoUpdateE));
        when(timelineFactory.create(sessionId, null, null)).thenReturn(timelineEmpty);
    }

    @Test
    public void register() {
        RegisterResponse response = timelineService.register(sessionId, URI, PROJECT_NAME);
        Assert.assertTrue(response.isSuccess());
        Assert.assertEquals(response.getId(), sessionId);
        Assert.assertEquals(response.getItems().size(), moreItems.size());
        Assert.assertTrue(TimelineService.isValid(sessionId));
        verify(storage, atLeastOnce()).read();
    }

    @Test
    public void fetchMore() {
        timelineService.register(sessionId, URI, PROJECT_NAME);

        FetchResponse response = timelineService.fetchMore(sessionId);
        Assert.assertTrue(!response.getItems().isEmpty());

        verify(itemProvider, times(2)).fetchItems(anyObject(), anyInt());
    }

    @Test
    public void fetchMoreWithEmptyResult() {
        timelineService.register(sessionId, null, null);
        FetchResponse withUnknownSession = timelineService.fetchMore(sessionId);
        Assert.assertTrue(withUnknownSession.getItems().isEmpty());
        Assert.assertTrue(withUnknownSession.isSuccess());
        Assert.assertNull(withUnknownSession.getCause());
        verify(itemProvider).fetchItems(anyObject(), anyInt());
        timelineService.unregister(sessionId);
    }

    @Test
    public void fetchMoreWithUnknownSession() {
        FetchResponse withUnknownSession = timelineService.fetchMore("???");
        Assert.assertFalse(withUnknownSession.isSuccess());
        Assert.assertEquals("The sessionId [???] is not registered!", withUnknownSession.getCause());
        Assert.assertNull(withUnknownSession.getItems());
    }

    @Test
    public void fetchNew() {
        timelineService.register(sessionId, URI, PROJECT_NAME);

        FetchResponse response = timelineService.fetchNew(sessionId);
        Assert.assertTrue(!response.getItems().isEmpty());

        verify(itemProvider).fetchNew(anyObject());
    }

    @Test
    public void fetchNewWithUnknownSession() {
        FetchResponse withUnknownSession = timelineService.fetchNew("???");
        Assert.assertFalse(withUnknownSession.isSuccess());
        Assert.assertEquals("The sessionId [???] is not registered!", withUnknownSession.getCause());
        Assert.assertNull(withUnknownSession.getItems());
    }

    @Test
    public void unregister() {
        timelineService.register(sessionId, URI, PROJECT_NAME);
        boolean result = timelineService.unregister(sessionId);
        Assert.assertTrue(result);
        verify(timeline).stopAutoFresh();
    }

    @Test
    public void unregisterWithUnknownSession() {
        boolean unknownSession = timelineService.unregister("???");
        Assert.assertFalse(unknownSession);
    }
}
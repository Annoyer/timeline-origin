package org.jcy.timeline.web.ui;

import org.jcy.timeline.core.model.Timeline;
import org.jcy.timeline.core.provider.git.GitItem;
import org.jcy.timeline.core.provider.git.GitItemProvider;
import org.jcy.timeline.core.util.FileSessionStorage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;

public class WebAutoUpdateTest {

    private static final String sessionId = "AAA";
    private WebAutoUpdate autoUpdate;

    @Before
    public void setUp() {
        Timeline<GitItem> t = mock(Timeline.class);
        autoUpdate = new WebAutoUpdate(sessionId, t);
    }

    @Test
    public void start() {
        autoUpdate.start();
        Assert.assertTrue(autoUpdate.isScheduled);
    }

    @Test
    public void stop() {
        autoUpdate.stop();

        autoUpdate.stop();
        Assert.assertTrue(!autoUpdate.isScheduled);
    }
}
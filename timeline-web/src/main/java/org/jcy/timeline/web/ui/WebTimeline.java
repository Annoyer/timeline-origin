package org.jcy.timeline.web.ui;

import org.jcy.timeline.core.model.Timeline;
import org.jcy.timeline.core.provider.git.GitItem;
import org.jcy.timeline.core.ui.FetchOperation;

import java.util.List;

public class WebTimeline {

    private final String sessionId;

    private final Timeline<GitItem> timeline;

    private WebAutoUpdate autoUpdate;

    public WebTimeline(String sessionId, Timeline<GitItem> timeline, WebAutoUpdate autoUpdate) {
        this.sessionId = sessionId;
        this.timeline = timeline;
        this.timeline.fetchItems();
        this.autoUpdate = autoUpdate;
    }

    public void startAutoFresh() {
        autoUpdate.start();
    }

    public void stopAutoFresh() {
        autoUpdate.stop();
    }

    public List<GitItem> fetch(FetchOperation fetchOperation) {
       fetchOperation.fetch(timeline);
       return timeline.getItems();
    }

    public List<GitItem> getItems() {
        return timeline.getItems();
    }
}

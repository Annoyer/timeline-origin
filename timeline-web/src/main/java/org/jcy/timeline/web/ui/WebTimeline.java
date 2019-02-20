package org.jcy.timeline.web.ui;

import org.jcy.timeline.core.model.ItemProvider;
import org.jcy.timeline.core.model.Timeline;
import org.jcy.timeline.core.provider.git.GitItem;
import org.jcy.timeline.core.ui.FetchOperation;
import org.jcy.timeline.core.util.FileSessionStorage;

import java.util.List;

public class WebTimeline {

    private final String sessionId;

    private final Timeline<GitItem> timeline;

    private WebAutoUpdate autoUpdate;

    public WebTimeline(String sessionId, ItemProvider<GitItem> itemItemProvider, FileSessionStorage<GitItem> sessionStorage) {
        this.sessionId = sessionId;
        this.timeline = new Timeline<>(itemItemProvider, sessionStorage);
        this.timeline.fetchItems();
    }

    public void startAutoFresh() {
        synchronized (this) {
            if (autoUpdate == null) {
                autoUpdate = new WebAutoUpdate(sessionId, timeline);
            }
        }

        autoUpdate.start();
    }

    public void stopAutoFresh() {
        if (autoUpdate != null) {
            autoUpdate.stop();
        }
    }

    public List<GitItem> fetch(FetchOperation fetchOperation) {
       fetchOperation.fetch(timeline);
       return timeline.getItems();
    }

    public List<GitItem> getItems() {
        return timeline.getItems();
    }
}

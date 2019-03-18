package org.jcy.timeline.web.ui;

import org.jcy.timeline.core.model.Timeline;
import org.jcy.timeline.core.provider.git.GitItem;
import org.jcy.timeline.core.provider.git.GitItemProvider;
import org.jcy.timeline.core.provider.git.GitItemSerialization;
import org.jcy.timeline.core.util.FileSessionStorage;
import org.jcy.timeline.core.util.FileStorageStructure;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class WebTimelineFactory {

    private static final String BASE_DIRECTORY = System.getProperty("user.home");

    public WebTimeline create(String sessionId, String uri, String name) {
        File base = new File(BASE_DIRECTORY + "/" + sessionId);
        if (!base.exists() || !base.isDirectory()) {
            base.mkdirs();
        }
        FileStorageStructure storageStructure = new FileStorageStructure(base);
        File timelineDirectory = storageStructure.getTimelineDirectory();
        GitItemProvider itemProvider = new GitItemProvider(uri, timelineDirectory, name);
        FileSessionStorage<GitItem> storage = new FileSessionStorage<>(storageStructure.getStorageFile(), new GitItemSerialization());
        Timeline<GitItem> timeline = this.createTimeline(itemProvider, storage);
        WebAutoUpdate autoUpdate = this.createAutoUpdate(sessionId, timeline);
        return new WebTimeline(sessionId, timeline, autoUpdate);
    }

    Timeline<GitItem> createTimeline(GitItemProvider itemProvider, FileSessionStorage<GitItem> storage) {
        return new Timeline<>(itemProvider, storage);
    }

    WebAutoUpdate createAutoUpdate(String sessionId, Timeline<GitItem> timeline) {
        return new WebAutoUpdate(sessionId, timeline);
    }
}

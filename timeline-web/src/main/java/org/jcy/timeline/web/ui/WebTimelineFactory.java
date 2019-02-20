package org.jcy.timeline.web.ui;

import org.jcy.timeline.core.provider.git.GitItem;
import org.jcy.timeline.core.provider.git.GitItemProvider;
import org.jcy.timeline.core.provider.git.GitItemSerialization;
import org.jcy.timeline.core.util.FileSessionStorage;
import org.jcy.timeline.core.util.FileStorageStructure;

import java.io.File;

public class WebTimelineFactory {

    private static final String BASE_DIRECTORY = System.getProperty("user.home");

    public static WebTimeline create(String sessionId, String uri, String name) {
        File base = new File(BASE_DIRECTORY + "/" + sessionId);
        if (!base.exists() || !base.isDirectory()) {
            base.mkdirs();
        }
        if (base.exists() && base.isDirectory()) {
            FileStorageStructure storageStructure = new FileStorageStructure(base);
            File timelineDirectory = storageStructure.getTimelineDirectory();
            GitItemProvider itemProvider = new GitItemProvider(uri, timelineDirectory, name);
            FileSessionStorage<GitItem> storage = new FileSessionStorage<>(storageStructure.getStorageFile(), new GitItemSerialization());
            return new WebTimeline(sessionId, itemProvider, storage);
        }
        return null;
    }
}

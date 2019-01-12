package org.jcy.timeline.swing.git;

import org.jcy.timeline.core.provider.git.GitItem;
import org.jcy.timeline.core.provider.git.GitItemProvider;
import org.jcy.timeline.core.provider.git.GitItemSerialization;
import org.jcy.timeline.swing.ui.SwingTimeline;
import org.jcy.timeline.core.util.FileSessionStorage;
import org.jcy.timeline.core.util.FileStorageStructure;

import java.io.File;

public class GitTimelineFactory {

    private final FileStorageStructure storageStructure;

    public GitTimelineFactory(FileStorageStructure storageStructure) {
        this.storageStructure = storageStructure;
    }

    public SwingTimeline<GitItem> create(String uri, String name) {
        // git remote repository, 读取commit缓存至本地文件
        File timelineDirectory = storageStructure.getTimelineDirectory();
        GitItemProvider itemProvider = new GitItemProvider(uri, timelineDirectory, name);

        // swing UI factory
        GitItemUiFactory itemUiFactory = new GitItemUiFactory();

        // 获取本地缓存的git commit记录（C:\Users\joy12\.timeline\session.storage)
        File storageFile = storageStructure.getStorageFile();
        FileSessionStorage<GitItem> storage = new FileSessionStorage<>(storageFile, new GitItemSerialization());
        return new SwingTimeline<GitItem>(itemProvider, itemUiFactory, storage);
    }
}
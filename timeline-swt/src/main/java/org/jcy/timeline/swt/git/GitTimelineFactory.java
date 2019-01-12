package org.jcy.timeline.swt.git;

import org.jcy.timeline.core.provider.git.GitItem;
import org.jcy.timeline.core.provider.git.GitItemProvider;
import org.jcy.timeline.core.provider.git.GitItemSerialization;
import org.jcy.timeline.swt.ui.SwtTimeline;
import org.jcy.timeline.core.util.FileSessionStorage;
import org.jcy.timeline.core.util.FileStorageStructure;
import org.eclipse.swt.widgets.Composite;

import java.io.File;

public class GitTimelineFactory {

    private final FileStorageStructure storageStructure;

    public GitTimelineFactory(FileStorageStructure storageStructure) {
        this.storageStructure = storageStructure;
    }

    public SwtTimeline<GitItem> create(Composite parent, String uri, String name) {
        File timelineDirectory = storageStructure.getTimelineDirectory();
        GitItemProvider itemProvider = new GitItemProvider(uri, timelineDirectory, name);
        GitItemUiFactory itemUiFactory = new GitItemUiFactory();
        File storageFile = storageStructure.getStorageFile();
        FileSessionStorage<GitItem> storage = new FileSessionStorage<>(storageFile, new GitItemSerialization());
        return new SwtTimeline<>(parent, itemProvider, itemUiFactory, storage);
    }
}
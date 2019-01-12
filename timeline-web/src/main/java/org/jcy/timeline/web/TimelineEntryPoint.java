package org.jcy.timeline.web;

import org.jcy.timeline.swt.ui.SwtTimeline;
import org.jcy.timeline.swt.git.GitTimelineFactory;
import org.jcy.timeline.core.util.FileStorageStructure;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.application.EntryPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import java.io.File;
import java.util.Locale;

public class TimelineEntryPoint implements EntryPoint {

    private static final File BASE_DIRECTORY = new File(System.getProperty("user.home"));
    private static final String URI = "https://github.com/Annoyer/jenkins-web-test.git";
    private static final String REPOSITORY_NAME = "jenkins-web-test";

    private final StorageDirectoryProvider storageDirectoryProvider;

    public TimelineEntryPoint() {
        storageDirectoryProvider = new StorageDirectoryProvider(BASE_DIRECTORY);
    }

    @Override
    public int createUI() {
        Locale.setDefault(Locale.ENGLISH);
        Shell shell = new Shell(new Display(), SWT.NO_TRIM);
        shell.setMaximized(true);
        shell.setLayout(new FillLayout());
        SwtTimeline<?> timeline = createTimelineFactory().create(shell, URI, REPOSITORY_NAME);
        timeline.setTitle("JUnit");
        timeline.startAutoRefresh();
        RWT.getUISession().addUISessionListener(evt -> timeline.stopAutoRefresh());
        shell.open();
        return 0;
    }

    private GitTimelineFactory createTimelineFactory() {
        File directory = storageDirectoryProvider.getDirectory(RWT.getRequest(), RWT.getResponse());
        return new GitTimelineFactory(new FileStorageStructure(directory));
    }
}
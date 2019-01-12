package org.jcy.timeline.swt;

import org.jcy.timeline.swt.ui.SwtTimeline;
import org.jcy.timeline.swt.git.GitTimelineFactory;
import org.jcy.timeline.core.util.FileStorageStructure;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import java.io.File;
import java.util.Locale;

public class Application {

    private static final File BASE_DIRECTORY = new File(System.getProperty("user.home"));
    private static final String URI = "https://github.com/Annoyer/jenkins-web-test.git";
    private static final String REPOSITORY_NAME = "jenkins-web-test";

    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        Display display = Display.getDefault();
        Shell shell = createShell(display);
        SwtTimeline<?> timeline = createTimelineFactory().create(shell, URI, REPOSITORY_NAME);
        timeline.setTitle("JUnit");
        timeline.startAutoRefresh();
        spinUiLoop(display, shell);
    }

    private static Shell createShell(Display display) {
        Shell result = new Shell(display, SWT.SHELL_TRIM);
        result.setLayout(new FillLayout());
        result.setBounds(100, 100, 350, 700);
        return result;
    }

    private static void spinUiLoop(Display display, Shell shell) {
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    private static GitTimelineFactory createTimelineFactory() {
        return new GitTimelineFactory(new FileStorageStructure(BASE_DIRECTORY));
    }
}
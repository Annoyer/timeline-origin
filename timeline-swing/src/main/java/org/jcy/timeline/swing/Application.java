package org.jcy.timeline.swing;

import org.jcy.timeline.swing.ui.SwingTimeline;
import org.jcy.timeline.swing.git.GitTimelineFactory;
import org.jcy.timeline.core.util.FileStorageStructure;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Locale;

import static javax.swing.JFrame.EXIT_ON_CLOSE;
import static javax.swing.SwingUtilities.invokeLater;

public class Application {

    private static final File BASE_DIRECTORY = new File(System.getProperty("user.home"));
    private static final String URI = "https://github.com/Annoyer/jenkins-web-test.git";
    private static final String REPOSITORY_NAME = "jenkins-web-test";

    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        invokeLater(() -> createAndShowUi());
    }

    private static void createAndShowUi() {
        JFrame frame = createFrame();
        SwingTimeline<?> timeline = createTimelineFactory().create(URI, REPOSITORY_NAME);
        timeline.setTitle("JUnit");
        layout(frame, timeline.getComponent());
        // 开启自动更新 每隔五秒，一个后台线程回去fetch一下commit记录
        timeline.startAutoRefresh();
    }

    private static JFrame createFrame() {
        JFrame result = new JFrame("Timeline");
        result.setDefaultCloseOperation(EXIT_ON_CLOSE);
        return result;
    }

    private static GitTimelineFactory createTimelineFactory() {
        return new GitTimelineFactory(new FileStorageStructure(BASE_DIRECTORY));
    }

    private static void layout(JFrame frame, Component timeline) {
        frame.add(timeline);
        frame.setBounds(100, 100, 350, 700);
        frame.setVisible(true);
    }
}
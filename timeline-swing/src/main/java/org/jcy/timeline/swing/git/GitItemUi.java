package org.jcy.timeline.swing.git;

import org.jcy.timeline.core.provider.git.GitItem;
import org.jcy.timeline.swing.ui.SwingItemUi;
import org.jcy.timeline.util.NiceTime;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

import static org.jcy.timeline.swing.ui.Resources.WHITE;

class GitItemUi implements SwingItemUi<GitItem> {

    private final NiceTime niceTime;
    private final JTextArea content;
    private final JPanel component;
    private final JLabel author;
    private final GitItem item;
    private final JLabel time;

    GitItemUi(GitItem item) {
        this(item, new NiceTime());
    }

    GitItemUi(GitItem item, NiceTime niceTime) {
        this.niceTime = niceTime;
        this.item = item;
        this.component = createComponent();
        this.author = createAuthor(item);
        this.time = createTime();
        this.content = createContent(item);
        layout();
    }

    @Override
    public Component getComponent() {
        return component;
    }

    @Override
    public void update() {
        time.setText(getPrettyTime());
    }

    String getTime() {
        return time.getText();
    }

    private JPanel createComponent() {
        JPanel result = new JPanel();
        result.setBackground(WHITE);
        return result;
    }

    private JLabel createAuthor(GitItem item) {
        JLabel result = new JLabel(item.getAuthor());
        result.setOpaque(true);
        result.setBackground(WHITE);
        return result;
    }

    private JLabel createTime() {
        JLabel result = new JLabel(getPrettyTime());
        result.setOpaque(true);
        result.setBackground(WHITE);
        return result;
    }

    private JTextArea createContent(GitItem item) {
        JTextArea result = new JTextArea(item.getContent());
        result.setWrapStyleWord(true);
        result.setLineWrap(true);
        result.setEditable(false);
        return result;
    }

    private void layout() {
        component.setLayout(new BorderLayout(5, 5));
        component.add(author, BorderLayout.WEST);
        component.add(time, BorderLayout.EAST);
        component.add(content, BorderLayout.SOUTH);
    }

    private String getPrettyTime() {
        return niceTime.format(new Date(item.getTimeStamp()));
    }
}
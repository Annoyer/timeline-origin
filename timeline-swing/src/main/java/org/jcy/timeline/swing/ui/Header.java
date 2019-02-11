package org.jcy.timeline.swing.ui;

import org.jcy.timeline.core.model.Item;
import org.jcy.timeline.core.model.Timeline;
import org.jcy.timeline.util.BackgroundProcessor;
import org.jcy.timeline.util.Messages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.jcy.timeline.swing.ui.Resources.changeFontSize;
import static org.jcy.timeline.swing.ui.SwingTimelineCompound.createBackgroundProcessor;
import static org.jcy.timeline.util.Assertion.checkArgument;

class Header<T extends Item> {

    static final String TITLE = "Timeline";

    private final BackgroundProcessor backgroundProcessor;
    private final Timeline<T> timeline;

    JPanel component;
    JButton fetchNew;
    JLabel title;

    Header(Timeline<T> timeline) {
        this(timeline, createBackgroundProcessor());
    }

    Header(Timeline<T> timeline, BackgroundProcessor backgroundProcessor) {
        this.backgroundProcessor = backgroundProcessor;
        this.timeline = timeline;
    }

    void createUi() {
        createComponent();
        createTitle();
        createFetchNew();
        layout();
    }

    Component getComponent() {
        return component;
    }

    void update() {
        backgroundProcessor.process(() -> {
            int count = timeline.getNewCount();
            backgroundProcessor.dispatchToUiThread(() -> update(count));
        });
    }

    void onFetchNew(ActionListener listener) {
        fetchNew.addActionListener(evt -> notifyAboutFetchRequest(listener, evt));
    }

    void setTitle(String title) {
        checkArgument(title != null, Messages.get("TITLE_MUST_NOT_BE_NULL"));

        this.title.setText(title);
    }

    String getTitle() {
        return title.getText();
    }

    private void update(int count) {
        fetchNew.setText(count + " new");
        fetchNew.setVisible(count > 0);
    }

    private void notifyAboutFetchRequest(ActionListener listener, ActionEvent evt) {
        listener.actionPerformed(new ActionEvent(component, evt.getID(), evt.getActionCommand()));
        fetchNew.setVisible(false);
    }

    private void createComponent() {
        component = new JPanel();
    }

    private void createTitle() {
        title = new JLabel(TITLE);
        changeFontSize(title, 10);
    }

    private void createFetchNew() {
        fetchNew = new JButton();
        fetchNew.setVisible(false);
    }

    private void layout() {
        component.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 10));
        component.add(title);
        component.add(fetchNew);
    }
}
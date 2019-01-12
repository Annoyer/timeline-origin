package org.jcy.timeline.swing.ui;

import org.jcy.timeline.core.model.Item;
import org.jcy.timeline.core.model.ItemProvider;
import org.jcy.timeline.core.model.SessionStorage;
import org.jcy.timeline.core.ui.AutoUpdate;
import org.jcy.timeline.core.ui.ItemUiFactory;
import org.jcy.timeline.core.ui.ItemViewer;

import javax.swing.*;
import java.awt.*;

public class SwingTimeline<T extends Item> {

    private final ItemViewer<T, Container> itemViewer;
    private final AutoUpdate<T, Container> autoUpdate;
    private final Header<T> header;
    private final JPanel component;

    public SwingTimeline(
            ItemProvider<T> itemProvider, ItemUiFactory<T, Container> itemUiFactory, SessionStorage<T> sessionStorage) {
        this(new SwingTimelineCompound<>(itemProvider, itemUiFactory, sessionStorage));
    }

    SwingTimeline(SwingTimelineCompound<T> compound) {
        itemViewer = compound.getItemViewer();
        header = compound.getHeader();
        autoUpdate = compound.getAutoUpdate();
        component = initialize();
    }

    public Component getComponent() {
        return component;
    }

    public void startAutoRefresh() {
        autoUpdate.start();
    }

    public void stopAutoRefresh() {
        autoUpdate.stop();
    }

    public void setTitle(String title) {
        header.setTitle(title);
    }

    private JPanel initialize() {
        header.createUi();
        itemViewer.createUi(null);
        JPanel result = createComponent();
        itemViewer.initialize();
        header.onFetchNew(event -> itemViewer.fetchNew());
        return result;
    }

    private JPanel createComponent() {
        JPanel result = new JPanel();
        result.setLayout(new BorderLayout());
        result.add(header.getComponent(), BorderLayout.NORTH);
        result.add(itemViewer.getUiRoot(), BorderLayout.CENTER);
        return result;
    }
}
package org.jcy.timeline.swing.ui;

import org.jcy.timeline.core.model.Item;
import org.jcy.timeline.core.ui.ItemUiList;
import org.jcy.timeline.core.ui.ItemUiMap;
import org.jcy.timeline.util.BackgroundProcessor;

import javax.swing.*;
import java.awt.*;

import static org.jcy.timeline.swing.ui.Resources.WHITE;
import static org.jcy.timeline.swing.ui.SwingItemUi.createUiItemConstraints;
import static org.jcy.timeline.swing.ui.SwingTimelineCompound.createBackgroundProcessor;
import static org.jcy.timeline.core.ui.FetchOperation.MORE;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;

class SwingItemUiList<T extends Item> extends ItemUiList<T, Container> {

    JScrollPane uiRoot;
    JButton fetchMore;
    JPanel content;

    SwingItemUiList(ItemUiMap<T, Container> itemUiMap) {
        this(itemUiMap, createBackgroundProcessor());
    }

    SwingItemUiList(ItemUiMap<T, Container> itemUiMap, BackgroundProcessor backgroundProcessor) {
        super(itemUiMap, backgroundProcessor);
    }

    @Override
    protected JScrollPane getUiRoot() {
        return uiRoot;
    }

    @Override
    protected Container getContent() {
        return content;
    }

    @Override
    protected void beforeContentUpdate() {
        content.remove(fetchMore);
    }

    @Override
    protected void afterContentUpdate() {
        content.add(fetchMore, createUiItemConstraints());
        content.getParent().doLayout();
    }

    @Override
    protected void createUi(Container parent) {
        createContent();
        createComponent();
        createFetchMore();
    }

    private void createContent() {
        content = new JPanel();
        content.setLayout(new GridBagLayout());
        content.setBackground(WHITE);
    }

    private void createComponent() {
        uiRoot = new JScrollPane(content);
        uiRoot.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
    }

    private void createFetchMore() {
        fetchMore = new JButton("more");
        fetchMore.addActionListener(event -> fetchInBackground(MORE));
        content.add(fetchMore);
    }
}
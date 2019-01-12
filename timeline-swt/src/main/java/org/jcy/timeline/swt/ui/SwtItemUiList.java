package org.jcy.timeline.swt.ui;

import org.jcy.timeline.core.model.Item;
import org.jcy.timeline.core.ui.ItemUiList;
import org.jcy.timeline.core.ui.ItemUiMap;
import org.jcy.timeline.util.BackgroundProcessor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import static java.util.Arrays.asList;
import static org.jcy.timeline.swt.ui.Resources.getColorWhite;
import static org.jcy.timeline.swt.ui.SwtTimelineCompound.createBackgroundProcessor;
import static org.jcy.timeline.core.ui.FetchOperation.MORE;

class SwtItemUiList<T extends Item> extends ItemUiList<T, Composite> {

    ScrolledComposite uiRoot;
    Composite content;
    Button fetchMore;

    SwtItemUiList(ItemUiMap<T, Composite> itemUiMap) {
        this(itemUiMap, createBackgroundProcessor());
    }

    SwtItemUiList(ItemUiMap<T, Composite> itemUiMap, BackgroundProcessor backgroundProcessor) {
        super(itemUiMap, backgroundProcessor);
    }

    @Override
    protected Composite getUiRoot() {
        return uiRoot;
    }

    @Override
    protected Composite getContent() {
        return content;
    }

    @Override
    protected void beforeContentUpdate() {
        fetchMore.dispose();
    }

    @Override
    protected void afterContentUpdate() {
        createFetchMore();
        layoutContent();
    }

    @Override
    public void createUi(Composite parent) {
        createControl(parent);
        createContent();
        createFetchMore();
    }

    private void createControl(Composite parent) {
        uiRoot = new ScrolledComposite(parent, SWT.V_SCROLL);
        uiRoot.setBackground(getColorWhite());
        uiRoot.addListener(SWT.Resize, evt -> layoutContent());
    }

    private void createContent() {
        content = new Composite(uiRoot, SWT.NONE);
        content.setBackground(getColorWhite());
        uiRoot.setContent(content);
    }

    private void createFetchMore() {
        fetchMore = new Button(content, SWT.NONE);
        fetchMore.setText("more");
        fetchMore.addListener(SWT.Selection, event -> fetch(MORE));
    }

    private void layoutContent() {
        GridLayout gridLayout = new GridLayout();
        content.setLayout(gridLayout);
        content.setSize(computePreferredContentSize());
        int itemControlWidth = computePreferredContentSize().x - gridLayout.verticalSpacing * 2;
        asList(content.getChildren()).forEach(itemControl -> setLayoutData(itemControl, itemControlWidth));
        content.setSize(computePreferredContentSize());
        content.layout();
    }

    private Point computePreferredContentSize() {
        return content.computeSize(uiRoot.getClientArea().width, SWT.DEFAULT, true);
    }

    private void setLayoutData(Control itemControl, int width) {
        itemControl.setLayoutData(new GridData(width, SWT.DEFAULT));
    }
}
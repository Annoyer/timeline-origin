package org.jcy.timeline.swt.ui;

import org.jcy.timeline.core.model.Item;
import org.jcy.timeline.core.model.ItemProvider;
import org.jcy.timeline.core.model.SessionStorage;
import org.jcy.timeline.core.ui.AutoUpdate;
import org.jcy.timeline.core.ui.ItemUiFactory;
import org.jcy.timeline.core.ui.ItemViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import static org.jcy.timeline.swt.ui.util.FormDatas.attach;

public class SwtTimeline<T extends Item> {

    private final ItemViewer<T, Composite> itemViewer;
    private final AutoUpdate<T, Composite> autoUpdate;
    private final Header<T> header;
    private final Composite control;

    public SwtTimeline(
            Composite parent, ItemProvider<T> itemProvider, ItemUiFactory<T, Composite> itemUiFactory, SessionStorage<T> sessionStorage) {
        this(parent, new SwtTimelineCompound<>(itemProvider, itemUiFactory, sessionStorage));
    }

    SwtTimeline(Composite parent, SwtTimelineCompound<T> compound) {
        itemViewer = compound.getItemViewer();
        header = compound.getHeader();
        autoUpdate = compound.getAutoUpdate();
        control = initialize(parent);
    }

    public Control getControl() {
        return control;
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

    private Composite initialize(Composite parent) {
        Composite result = new Composite(parent, SWT.NONE);
        header.createUi(result);
        itemViewer.createUi(result);
        layout(result);
        itemViewer.initialize();
        header.onFetchNew(event -> itemViewer.fetchNew());
        return result;
    }

    private void layout(Composite control) {
        control.setLayout(new FormLayout());
        attach(header.getControl()).toLeft().toTop().toRight();
        attach(itemViewer.getUiRoot()).toLeft().atTopTo(header.getControl()).toRight().toBottom();
    }
}
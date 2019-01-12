package org.jcy.timeline.swt.ui;

import org.jcy.timeline.core.model.Item;
import org.jcy.timeline.core.ui.ItemUi;
import org.eclipse.swt.widgets.Control;

public interface SwtItemUi<T extends Item> extends ItemUi<T> {
    Control getControl();
}

package org.jcy.timeline.swing.ui;

import org.jcy.timeline.core.model.Item;
import org.jcy.timeline.core.ui.ItemUi;

import java.awt.*;

import static java.awt.GridBagConstraints.HORIZONTAL;

public interface SwingItemUi<T extends Item> extends ItemUi<T> {

    Component getComponent();

    public static GridBagConstraints createUiItemConstraints() {
        GridBagConstraints result = new GridBagConstraints();
        result.gridx = 0;
        result.fill = HORIZONTAL;
        result.weightx = 1;
        result.insets = new Insets(15, 10, 5, 10);
        return result;
    }
}

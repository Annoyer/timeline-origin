package org.jcy.timeline.swing.git;

import org.jcy.timeline.core.provider.git.GitItem;
import org.jcy.timeline.core.ui.ItemUi;
import org.jcy.timeline.core.ui.ItemUiFactory;
import org.jcy.timeline.util.Messages;

import java.awt.*;

import static org.jcy.timeline.swing.ui.SwingItemUi.createUiItemConstraints;
import static org.jcy.timeline.util.Assertion.checkArgument;

public class GitItemUiFactory implements ItemUiFactory<GitItem, Container> {

    @Override
    public ItemUi<GitItem> create(Container uiContext, GitItem item, int index) {
        checkArgument(uiContext != null, Messages.get("UI_CONTEXT_MUST_NOT_BE_NULL"));
        checkArgument(item != null, Messages.get("ITEM_MUST_NOT_BE_NULL"));
        checkArgument(index >= 0, Messages.get("INDEX_MUST_NOT_BE_NEGATIVE"));

        GitItemUi result = new GitItemUi(item);
        uiContext.add(result.getComponent(), createUiItemConstraints(), index);
        return result;
    }
}
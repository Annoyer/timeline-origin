package org.jcy.timeline.swt.git;

import org.jcy.timeline.core.provider.git.GitItem;
import org.jcy.timeline.core.ui.ItemUi;
import org.jcy.timeline.core.ui.ItemUiFactory;
import org.eclipse.swt.widgets.Composite;
import org.jcy.timeline.util.Messages;

import static org.jcy.timeline.util.Assertion.checkArgument;

public class GitItemUiFactory implements ItemUiFactory<GitItem, Composite> {

    @Override
    public ItemUi<GitItem> create(Composite uiContext, GitItem item, int index) {
        checkArgument(uiContext != null, Messages.get("UI_CONTEXT_MUST_NOT_BE_NULL"));
        checkArgument(item != null, Messages.get("ITEM_MUST_NOT_BE_NULL"));
        checkArgument(index >= 0, Messages.get("INDEX_MUST_NOT_BE_NEGATIVE"));

        return new GitItemUi(uiContext, item, index);
    }
}
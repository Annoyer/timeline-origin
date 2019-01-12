package org.jcy.timeline.swing.git;

import org.jcy.timeline.core.provider.git.GitItem;
import org.jcy.timeline.core.ui.ItemUi;
import org.jcy.timeline.core.ui.ItemUiFactory;

import java.awt.*;

import static org.jcy.timeline.swing.ui.SwingItemUi.createUiItemConstraints;
import static org.jcy.timeline.util.Assertion.checkArgument;

public class GitItemUiFactory implements ItemUiFactory<GitItem, Container> {

    static final String UI_CONTEXT_MUST_NOT_BE_NULL = "Argument 'uiContext' must not be null.";
    static final String ITEM_MUST_NOT_BE_NULL = "Argument 'item' must not be null.";
    static final String INDEX_MUST_NOT_BE_NEGATIVE = "Index must not be negative.";

    @Override
    public ItemUi<GitItem> create(Container uiContext, GitItem item, int index) {
        checkArgument(uiContext != null, UI_CONTEXT_MUST_NOT_BE_NULL);
        checkArgument(item != null, ITEM_MUST_NOT_BE_NULL);
        checkArgument(index >= 0, INDEX_MUST_NOT_BE_NEGATIVE);

        GitItemUi result = new GitItemUi(item);
        uiContext.add(result.getComponent(), createUiItemConstraints(), index);
        return result;
    }
}
package org.jcy.timeline.swt.git;

import org.jcy.timeline.core.provider.git.GitItem;
import org.jcy.timeline.swt.ui.SwtItemUi;
import org.jcy.timeline.test.util.swt.DisplayHelper;
import org.eclipse.swt.widgets.Composite;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.jcy.timeline.swt.git.GitItemUiFactory.*;
import static org.jcy.timeline.test.util.ThrowableCaptor.thrownBy;
import static java.lang.System.currentTimeMillis;
import static org.assertj.core.api.Assertions.assertThat;

public class GitItemUiFactoryITest {

    private static final int INDEX = 0;

    @Rule
    public final DisplayHelper displayHelper = new DisplayHelper();

    private GitItemUiFactory factory;
    private Composite uiContext;
    private GitItem gitItem;

    @Before
    public void setUp() {
        factory = new GitItemUiFactory();
        uiContext = displayHelper.createShell();
        gitItem = new GitItem("id", currentTimeMillis(), "author", "content");
    }

    @Test
    public void create() {
        SwtItemUi<GitItem> actual = (SwtItemUi<GitItem>) factory.create(uiContext, gitItem, INDEX);

        assertThat(actual.getControl()).isSameAs(uiContext.getChildren()[INDEX]);
    }

    @Test
    public void createWithNullAsUiContext() {
        Throwable actual = thrownBy(() -> factory.create(null, gitItem, INDEX));

        assertThat(actual)
                .hasMessage(UI_CONTEXT_MUST_NOT_BE_NULL)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void createWithNullAsItem() {
        Throwable actual = thrownBy(() -> factory.create(uiContext, null, INDEX));

        assertThat(actual)
                .hasMessage(ITEM_MUST_NOT_BE_NULL)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void createWithNegativeIndex() {
        Throwable actual = thrownBy(() -> factory.create(uiContext, gitItem, -1));

        assertThat(actual)
                .hasMessage(INDEX_MUST_NOT_BE_NEGATIVE)
                .isInstanceOf(IllegalArgumentException.class);
    }
}
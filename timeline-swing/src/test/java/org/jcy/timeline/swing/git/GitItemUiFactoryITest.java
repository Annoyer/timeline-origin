package org.jcy.timeline.swing.git;

import org.jcy.timeline.core.provider.git.GitItem;
import org.jcy.timeline.swing.ui.SwingItemUi;
import org.jcy.timeline.util.Messages;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;

import static org.jcy.timeline.swing.git.GitItemUiFactory.*;
import static org.jcy.timeline.test.util.ThrowableCaptor.thrownBy;
import static java.lang.System.currentTimeMillis;
import static org.assertj.core.api.Assertions.assertThat;

public class GitItemUiFactoryITest {

    private static final int INDEX = 0;

    private GitItemUiFactory factory;
    private JPanel uiContext;
    private GitItem gitItem;

    @Before
    public void setUp() {
        factory = new GitItemUiFactory();
        uiContext = new JPanel();
        gitItem = new GitItem("id", currentTimeMillis(), "author", "content");
    }

    @Test
    public void create() {
        SwingItemUi<GitItem> actual = (SwingItemUi<GitItem>) factory.create(uiContext, gitItem, INDEX);

        assertThat(actual.getComponent()).isSameAs(uiContext.getComponent(INDEX));
    }

    @Test
    public void createWithNullAsUiContext() {
        Throwable actual = thrownBy(() -> factory.create(null, gitItem, INDEX));

        assertThat(actual)
                .hasMessage(Messages.get("UI_CONTEXT_MUST_NOT_BE_NULL"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void createWithNullAsItem() {
        Throwable actual = thrownBy(() -> factory.create(uiContext, null, INDEX));

        assertThat(actual)
                .hasMessage(Messages.get("ITEM_MUST_NOT_BE_NULL"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void createWithNegativeIndex() {
        Throwable actual = thrownBy(() -> factory.create(uiContext, gitItem, -1));

        assertThat(actual)
                .hasMessage(Messages.get("INDEX_MUST_NOT_BE_NEGATIVE"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
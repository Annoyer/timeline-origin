package org.jcy.timeline.swing.ui;

import org.jcy.timeline.core.model.Item;
import org.jcy.timeline.core.model.ItemProvider;
import org.jcy.timeline.core.model.Memento;
import org.jcy.timeline.core.model.SessionStorage;
import org.jcy.timeline.core.ui.ItemUiFactory;
import org.jcy.timeline.util.BackgroundProcessor;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.jcy.timeline.swing.ui.SwingTimelineCompound.*;
import static org.jcy.timeline.test.util.ThrowableCaptor.thrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SwingTimelineCompoundTest {

    private ItemUiFactory<Item, Container> itemUiFactory;
    private SwingTimelineCompound<Item> compound;
    private SessionStorage<Item> sessionStorage;
    private ItemProvider<Item> itemProvider;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() {
        itemProvider = mock(ItemProvider.class);
        itemUiFactory = mock(ItemUiFactory.class);
        sessionStorage = stubSessionStorage();
        compound = new SwingTimelineCompound<>(itemProvider, itemUiFactory, sessionStorage);
    }

    @Test
    public void getItemViewer() {
        assertThat(compound.getItemViewer()).isNotNull();
    }

    @Test
    public void getHeader() {
        assertThat(compound.getHeader()).isNotNull();
    }

    @Test
    public void getAutoUpdate() {
        assertThat(compound.getAutoUpdate()).isNotNull();
    }

    @Test
    public void constructWithNullAsItemProvider() {
        Throwable actual = thrownBy(() -> new SwingTimelineCompound<>(null, itemUiFactory, sessionStorage));

        assertThat(actual)
                .hasMessage(ITEM_PROVIDER_MUST_NOT_BE_NULL)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void constructWithNullAsItemUiFactory() {
        Throwable actual = thrownBy(() -> new SwingTimelineCompound<>(itemProvider, null, sessionStorage));

        assertThat(actual)
                .hasMessage(ITEM_UI_FACTORY_MUST_NOT_BE_NULL)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void constructWithNullAsSessionStorage() {
        Throwable actual = thrownBy(() -> new SwingTimelineCompound<>(itemProvider, itemUiFactory, null));

        assertThat(actual)
                .hasMessage(SESSION_STORAGE_MUST_NOT_BE_NULL)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @SuppressWarnings("rawtypes")
    private static SessionStorage stubSessionStorage() {
        SessionStorage result = mock(SessionStorage.class);
        when(result.read()).thenReturn(Memento.empty());
        return result;
    }

    @Test
    public void createBackgroundProcessor() {
        BackgroundProcessor actual = SwingTimelineCompound.createBackgroundProcessor();

        assertThat(actual).isNotNull();
    }
}
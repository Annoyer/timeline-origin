package org.jcy.timeline.swt.ui;

import org.jcy.timeline.core.model.Item;
import org.jcy.timeline.core.model.ItemProvider;
import org.jcy.timeline.core.model.Memento;
import org.jcy.timeline.core.model.SessionStorage;
import org.jcy.timeline.core.ui.ItemUiFactory;
import org.jcy.timeline.test.util.swt.DisplayHelper;
import org.jcy.timeline.util.BackgroundProcessor;
import org.eclipse.swt.widgets.Composite;
import org.jcy.timeline.util.Messages;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.jcy.timeline.swt.ui.SwtTimelineCompound.*;
import static org.jcy.timeline.test.util.ThrowableCaptor.thrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SwtTimelineCompoundITest {

    @Rule
    public final DisplayHelper displayHelper = new DisplayHelper();

    private ItemUiFactory<Item, Composite> itemUiFactory;
    private SwtTimelineCompound<Item> compound;
    private SessionStorage<Item> sessionStorage;
    private ItemProvider<Item> itemProvider;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() {
        displayHelper.ensureDisplay();
        itemProvider = mock(ItemProvider.class);
        itemUiFactory = mock(ItemUiFactory.class);
        sessionStorage = stubSessionStorage();
        compound = new SwtTimelineCompound<>(itemProvider, itemUiFactory, sessionStorage);
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
        Throwable actual = thrownBy(() -> new SwtTimelineCompound<>(null, itemUiFactory, sessionStorage));

        assertThat(actual)
                .hasMessage(Messages.get("ITEM_PROVIDER_MUST_NOT_BE_NULL"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void constructWithNullAsItemUiFactory() {
        Throwable actual = thrownBy(() -> new SwtTimelineCompound<>(itemProvider, null, sessionStorage));

        assertThat(actual)
                .hasMessage(Messages.get("ITEM_UI_FACTORY_MUST_NOT_BE_NULL"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void constructWithNullAsSessionStorage() {
        Throwable actual = thrownBy(() -> new SwtTimelineCompound<>(itemProvider, itemUiFactory, null));

        assertThat(actual)
                .hasMessage(Messages.get("SESSION_STORAGE_MUST_NOT_BE_NULL"))
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
        BackgroundProcessor actual = SwtTimelineCompound.createBackgroundProcessor();

        assertThat(actual).isNotNull();
    }
}
package org.jcy.timeline.swing.ui;

import org.jcy.timeline.core.model.Item;
import org.jcy.timeline.core.model.Timeline;
import org.jcy.timeline.util.BackgroundProcessor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.jcy.timeline.swing.BackgroundThreadHelper.directBackgroundProcessor;
import static org.jcy.timeline.swing.ui.Header.TITLE_MUST_NOT_BE_NULL;
import static org.jcy.timeline.test.util.ThrowableCaptor.thrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class HeaderITest {

    private BackgroundProcessor backgroundProcessor;
    private Timeline<Item> timeline;
    private Header<Item> header;

    @Before
    public void setUp() {
        timeline = mock(Timeline.class);
        backgroundProcessor = directBackgroundProcessor();
        header = createHeader(timeline, backgroundProcessor);
    }

    @Test
    public void getComponent() {
        assertThat(header.getComponent()).isNotNull();
    }

    @Test
    public void updateIfNewItemIsAvailable() {
        when(timeline.getNewCount()).thenReturn(1);

        header.update();

        assertThat(header.fetchNew.isVisible()).isTrue();
    }

    @Test
    public void updateIfNoNewItemIsAvailable() {
        when(timeline.getNewCount()).thenReturn(0);

        header.update();

        assertThat(header.fetchNew.isVisible()).isFalse();
    }

    @Test
    public void updateIfBackgroundThreadDoesNotRun() {
        doNothing().when(backgroundProcessor).process(any(Runnable.class));
        when(timeline.getNewCount()).thenReturn(1);

        header.update();

        assertThat(header.fetchNew.isVisible()).isFalse();
    }


    @Test
    public void onFetchNewNotification() throws Throwable {
        ArgumentCaptor<ActionEvent> captor = forClass(ActionEvent.class);
        ActionListener listener = mock(ActionListener.class);
        header.onFetchNew(listener);

        header.fetchNew.doClick();

        verify(listener).actionPerformed(captor.capture());
        assertThat(captor.getValue().getSource()).isSameAs(header.getComponent());
    }

    @Test
    public void getTitle() {
        String actual = header.getTitle();

        assertThat(actual).isEqualTo(Header.TITLE);
    }

    @Test
    public void setTitle() {
        String expected = "title ";

        header.setTitle(expected);
        String actual = header.getTitle();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void setTitleWithNullAsArgument() {
        Throwable actual = thrownBy(() -> header.setTitle(null));

        assertThat(actual)
                .hasMessage(TITLE_MUST_NOT_BE_NULL)
                .isInstanceOf(IllegalArgumentException.class);
    }

    private Header<Item> createHeader(Timeline<Item> timeline, BackgroundProcessor backgroundProcessor) {
        Header<Item> result = new Header<Item>(timeline, backgroundProcessor);
        result.createUi();
        return result;
    }
}
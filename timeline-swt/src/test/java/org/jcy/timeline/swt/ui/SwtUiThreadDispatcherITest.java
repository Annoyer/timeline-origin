package org.jcy.timeline.swt.ui;

import org.jcy.timeline.test.util.swt.DisplayHelper;
import org.jcy.timeline.util.BackgroundProcessor;
import org.eclipse.swt.SWTException;
import org.jcy.timeline.util.Messages;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.jcy.timeline.test.util.swt.DisplayHelper.flushPendingEvents;
import static org.jcy.timeline.test.util.ThreadHelper.sleep;
import static org.jcy.timeline.test.util.ThrowableCaptor.thrownBy;
import static java.lang.Thread.currentThread;
import static org.assertj.core.api.Assertions.assertThat;

public class SwtUiThreadDispatcherITest {

    @Rule
    public final DisplayHelper displayHelper = new DisplayHelper();

    private BackgroundProcessor backgroundProcessor;
    private SwtUiThreadDispatcher dispatcher;
    private volatile Thread backgroundThread;
    private volatile Thread uiThread;

    @Before
    public void setUp() {
        displayHelper.ensureDisplay();
        dispatcher = new SwtUiThreadDispatcher();
        backgroundProcessor = new BackgroundProcessor(dispatcher);
    }

    @Test
    public void dispatch() {
        backgroundProcessor.process(() -> {
            backgroundThread = Thread.currentThread();
            dispatcher.dispatch(() -> executeInUiThread());
        });

        sleep(200);
        flushPendingEvents();

        assertThat(uiThread)
                .isNotNull()
                .isSameAs(currentThread())
                .isNotSameAs(backgroundThread);
    }

    @Test
    public void dispatchIfDisplayIsDisposed() {
        displayHelper.getDisplay().dispose();

        Throwable actual = thrownBy(() -> dispatcher.dispatch(() -> {
            throw new RuntimeException();
        }));

        assertThat(actual).isNull();
    }

    @Test
    public void dispatchWithNullAsRunnable() {
        Throwable actual = thrownBy(() -> dispatcher.dispatch(null));

        assertThat(actual)
                .hasMessage(Messages.get("RUNNABLE_MUST_NOT_BE_NULL"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void constructWithNullAsDisplay() {
        Throwable actual = thrownBy(() -> new SwtUiThreadDispatcher(null));

        assertThat(actual)
                .hasMessage(Messages.get("DISPLAY_MUST_NOT_BE_NULL"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void constructInNonUiThread() {
        backgroundProcessor.process(() -> new SwtUiThreadDispatcher());
        sleep(200);
        Throwable actual = thrownBy(() -> flushPendingEvents());

        assertThat(actual)
                .hasMessageContaining(Messages.get("DISPLAY_MUST_NOT_BE_NULL"))
                .isInstanceOf(SWTException.class)
                .hasCauseInstanceOf(IllegalArgumentException.class);
    }

    private void executeInUiThread() {
        uiThread = Thread.currentThread();
    }
}
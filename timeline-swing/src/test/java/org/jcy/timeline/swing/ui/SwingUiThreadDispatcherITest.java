package org.jcy.timeline.swing.ui;

import org.jcy.timeline.util.Messages;
import org.junit.Before;
import org.junit.Test;

import static org.jcy.timeline.test.util.ThreadHelper.sleep;
import static org.jcy.timeline.test.util.ThrowableCaptor.thrownBy;
import static java.lang.Thread.currentThread;
import static org.assertj.core.api.Assertions.assertThat;

public class SwingUiThreadDispatcherITest {

    private SwingUiThreadDispatcher dispatcher;
    private volatile Thread uiThread;

    @Before
    public void setUp() {
        dispatcher = new SwingUiThreadDispatcher();
    }

    @Test
    public void dispatch() {
        dispatcher.dispatch(() -> executeInUiThread());

        sleep(100);

        assertThat(uiThread)
                .isNotNull()
                .isNotSameAs(currentThread());
    }

    @Test
    public void dispatchWithNullAsRunnable() {
        Throwable actual = thrownBy(() -> dispatcher.dispatch(null));

        assertThat(actual)
                .hasMessage(Messages.get("RUNNABLE_MUST_NOT_BE_NULL"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private void executeInUiThread() {
        uiThread = Thread.currentThread();
    }
}
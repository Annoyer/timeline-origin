package org.jcy.timeline.swing.ui;

import org.jcy.timeline.util.UiThreadDispatcher;

import static org.jcy.timeline.util.Assertion.checkArgument;
import static javax.swing.SwingUtilities.invokeLater;

public class SwingUiThreadDispatcher implements UiThreadDispatcher {

    static final String RUNNABLE_MUST_NOT_BE_NULL = "Argument 'runnable' must not be null.";

    @Override
    public void dispatch(Runnable runnable) {
        checkArgument(runnable != null, RUNNABLE_MUST_NOT_BE_NULL);

        invokeLater(runnable);
    }
}
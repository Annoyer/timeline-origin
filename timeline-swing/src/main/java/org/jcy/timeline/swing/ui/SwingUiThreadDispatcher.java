package org.jcy.timeline.swing.ui;

import org.jcy.timeline.util.Messages;
import org.jcy.timeline.util.UiThreadDispatcher;

import static org.jcy.timeline.util.Assertion.checkArgument;
import static javax.swing.SwingUtilities.invokeLater;

public class SwingUiThreadDispatcher implements UiThreadDispatcher {

    @Override
    public void dispatch(Runnable runnable) {
        checkArgument(runnable != null, Messages.get("RUNNABLE_MUST_NOT_BE_NULL"));

        invokeLater(runnable);
    }
}
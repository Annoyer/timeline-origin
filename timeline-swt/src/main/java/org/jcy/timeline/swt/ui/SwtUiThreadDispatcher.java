package org.jcy.timeline.swt.ui;

import org.jcy.timeline.util.Messages;
import org.jcy.timeline.util.UiThreadDispatcher;
import org.eclipse.swt.widgets.Display;

import static org.jcy.timeline.util.Assertion.checkArgument;

public class SwtUiThreadDispatcher implements UiThreadDispatcher {

    private final Display display;


    public SwtUiThreadDispatcher() {
        this(Display.getCurrent());
    }

    public SwtUiThreadDispatcher(Display display) {
        checkArgument(display != null, Messages.get("DISPLAY_MUST_NOT_BE_NULL"));

        this.display = display;
    }

    @Override
    public void dispatch(Runnable runnable) {
        checkArgument(runnable != null, Messages.get("RUNNABLE_MUST_NOT_BE_NULL"));

        if (!display.isDisposed()) {
            display.asyncExec(runnable);
        }
    }
}
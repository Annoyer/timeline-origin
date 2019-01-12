package org.jcy.timeline.swt.ui;

import org.jcy.timeline.util.UiThreadDispatcher;
import org.eclipse.swt.widgets.Display;

import static org.jcy.timeline.util.Assertion.checkArgument;

public class SwtUiThreadDispatcher implements UiThreadDispatcher {

    static final String RUNNABLE_MUST_NOT_BE_NULL = "Argument 'runnable' must not be null.";
    static final String DISPLAY_MUST_NOT_BE_NULL = "Argument 'display' must not be null.";

    private final Display display;


    public SwtUiThreadDispatcher() {
        this(Display.getCurrent());
    }

    public SwtUiThreadDispatcher(Display display) {
        checkArgument(display != null, DISPLAY_MUST_NOT_BE_NULL);

        this.display = display;
    }

    @Override
    public void dispatch(Runnable runnable) {
        checkArgument(runnable != null, RUNNABLE_MUST_NOT_BE_NULL);

        if (!display.isDisposed()) {
            display.asyncExec(runnable);
        }
    }
}
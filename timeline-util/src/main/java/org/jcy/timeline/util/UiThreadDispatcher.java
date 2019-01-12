package org.jcy.timeline.util;

public interface UiThreadDispatcher {
    void dispatch(Runnable runnable);
}
package org.jcy.timeline.core.model;

public interface SessionStorage<T extends Item> {
    void store(Memento<T> Memento);

    Memento<T> read();
}
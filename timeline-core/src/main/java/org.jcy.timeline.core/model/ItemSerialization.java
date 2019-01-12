package org.jcy.timeline.core.model;

public interface ItemSerialization<T extends Item> {
    String serialize(T item);

    T deserialize(String input);
}

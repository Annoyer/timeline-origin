package org.jcy.timeline.util;

import java.util.ArrayList;
import java.util.List;

import static org.jcy.timeline.util.Assertion.checkArgument;


public class Iterables {

    public static <T> List<T> asList(Iterable<T> iterable) {
        checkArgument(iterable != null, Messages.get("ITERABLE_MUST_NOT_BE_NULL"));

        List<T> result = new ArrayList<>();
        for (T element : iterable) {
            result.add(element);
        }
        return result;
    }

    private Iterables() {
    }
}
package org.jcy.timeline.util;

import static java.lang.String.format;

public class Assertion {

    public static void checkArgument(boolean condition, String messagePattern, Object... arguments) {
        if (!condition) {
            throw new IllegalArgumentException(formatErrorMessage(messagePattern, arguments));
        }
    }

    public static void checkState(boolean condition, String messagePattern, Object... arguments) {
        if (!condition) {
            throw new IllegalStateException(formatErrorMessage(messagePattern, arguments));
        }
    }

    public static String formatErrorMessage(String messagePattern, Object... arguments) {
        checkArgument(messagePattern != null, Messages.get("MESSAGE_PATTERN_MUST_NOT_BE_NULL"));
        checkArgument(arguments != null, Messages.get("ARGUMENTS_MUST_NOT_BE_NULL"));
        return format(messagePattern, arguments);
    }
}
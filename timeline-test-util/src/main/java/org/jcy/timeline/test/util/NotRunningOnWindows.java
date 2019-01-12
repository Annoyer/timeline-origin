package org.jcy.timeline.test.util;

import org.jcy.timeline.test.util.ConditionalIgnoreRule.IgnoreCondition;

public class NotRunningOnWindows implements IgnoreCondition {
    public boolean isSatisfied() {
        return !System.getProperty("os.name").startsWith("Windows");
    }
}
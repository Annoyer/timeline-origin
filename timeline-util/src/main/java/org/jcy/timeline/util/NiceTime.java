package org.jcy.timeline.util;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;
import java.util.Locale;

public class NiceTime {

    private PrettyTime prettyTime;

    public NiceTime() {
        prettyTime = new PrettyTime(Locale.ENGLISH);
    }

    public String format(Date then) {
        return prettyTime.format(then);
    }
}
package org.jcy.timeline.web.ui;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class WebTimelineFactoryTest {

    static final String URI = "https://github.com/Annoyer/jenkins-web-test.git";
    static final String REPOSITORY_NAME = "jenkins-web-test";

    @Test
    public void create() {
        WebTimeline timeline = new WebTimelineFactory().create("id", URI, REPOSITORY_NAME);
        Assert.assertNotNull(timeline);
    }
}
package org.jcy.timeline.swing.git;

import org.jcy.timeline.core.provider.git.GitItem;
import org.jcy.timeline.util.NiceTime;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static java.lang.System.currentTimeMillis;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GitItemUiITest {

    private static final String SOME_TIME_AGO = "Some time ago";
    private static final String NOW = "now";

    private GitItemUi gitItemUi;
    private NiceTime niceTime;
    private GitItem item;

    @Before
    public void setUp() {
        niceTime = createNiceTime();
        item = new GitItem("id", currentTimeMillis(), "author", "content");
        gitItemUi = new GitItemUi(item, niceTime);
    }

    @Test
    public void getComponent() {
        assertThat(gitItemUi.getComponent()).isNotNull();
    }

    @Test
    public void getTime() {
        String actual = gitItemUi.getTime();

        assertThat(actual).isEqualTo(NOW);
    }

    @Test
    public void update() {
        equipNiceTimeFormat(niceTime, SOME_TIME_AGO);

        gitItemUi.update();
        String actual = gitItemUi.getTime();

        assertThat(actual).isEqualTo(SOME_TIME_AGO);
    }

    private static NiceTime createNiceTime() {
        NiceTime result = mock(NiceTime.class);
        equipNiceTimeFormat(result, NOW);
        return result;
    }

    private static void equipNiceTimeFormat(NiceTime niceTime, String niceTimeFormat) {
        when(niceTime.format(any(Date.class))).thenReturn(niceTimeFormat);
    }
}
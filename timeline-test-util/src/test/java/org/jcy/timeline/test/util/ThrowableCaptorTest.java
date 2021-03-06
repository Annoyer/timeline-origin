package org.jcy.timeline.test.util;

import org.jcy.timeline.test.util.ThrowableCaptor.Actor;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

public class ThrowableCaptorTest {

    @Test
    public void thrownBy() throws Throwable {
        Throwable expected = new Throwable();
        Actor actor = createActorThatThrows(expected);

        Throwable actual = ThrowableCaptor.thrownBy(actor);

        assertThat(actual).isSameAs(expected);
    }

    @Test
    public void thrownByIfNoThrowableOccurs() {
        Actor actor = mock(Actor.class);

        Throwable actual = ThrowableCaptor.thrownBy(actor);

        assertThat(actual).isNull();
    }

    private static Actor createActorThatThrows(Throwable toBeThrown) throws Throwable {
        Actor result = mock(Actor.class);
        doThrow(toBeThrown).when(result).act();
        return result;
    }
}

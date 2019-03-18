package org.jcy.timeline.web.websocket;

import org.jcy.timeline.web.service.TimelineService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import javax.validation.constraints.AssertTrue;
import java.nio.file.attribute.UserPrincipal;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WebSocketEventListenerTest {

    @Mock
    private TimelineService timelineService;
    @InjectMocks
    private WebSocketEventListener listener;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(timelineService.unregister("1")).thenReturn(true);
        when(timelineService.unregister("2")).thenReturn(false);
    }

    @Test
    public void handleDisconnectListenerTest() {
        SessionDisconnectEvent event = new SessionDisconnectEvent(new Object(),
                new GenericMessage<>(new byte[0]), "1", CloseStatus.NORMAL, (UserPrincipal) () -> "1");
        listener.handleDisconnectListener(event);
        verify(timelineService).unregister("1");
    }

    @Test
    public void handleDisconnectListenerWithoutPrincipalTest() {
        SessionDisconnectEvent event = new SessionDisconnectEvent(new Object(),
                new GenericMessage<>(new byte[0]), "1", CloseStatus.NORMAL);
        listener.handleDisconnectListener(event);
        verify(timelineService, never()).unregister(anyString());
    }

    @Test
    public void handleDisconnectListenerWithUnknownPrincipalTest() {
        SessionDisconnectEvent event = new SessionDisconnectEvent(new Object(),
                new GenericMessage<>(new byte[0]), "2", CloseStatus.NORMAL, (UserPrincipal) () -> "2");
        listener.handleDisconnectListener(event);
        verify(timelineService).unregister("2");
    }
}
package org.jcy.timeline.web.service;

import com.google.common.collect.Lists;
import org.jcy.timeline.web.ItemFactory;
import org.jcy.timeline.web.model.UpdateInfo;
import org.jcy.timeline.web.websocket.WebSocketConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class AutoUpdateServiceTest {

    @Mock
    private SimpMessagingTemplate sender;
    @InjectMocks
    private AutoUpdateService autoUpdateService;

    private static final String TOPIC = WebSocketConfig.UNICAST_PREFIX + "/autoUpdate";

    private static final String sessionId = "a";

    private static final UpdateInfo updateInfo = new UpdateInfo(5, Lists.newArrayList(ItemFactory.createNewItems(1, 5)));

    @Test
    public void notifyUpdate() {
        autoUpdateService.notifyUpdate(sessionId, updateInfo);
        verify(sender).convertAndSendToUser(sessionId, TOPIC, updateInfo);
    }
}
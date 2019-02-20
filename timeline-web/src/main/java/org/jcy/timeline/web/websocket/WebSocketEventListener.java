package org.jcy.timeline.web.websocket;

import org.jcy.timeline.web.service.TimelineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import java.security.Principal;


@Component
public class WebSocketEventListener {

    private static final Logger log = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    private TimelineService timelineService;

    @EventListener
    public void handleUnsubscribeEvent(SessionUnsubscribeEvent event) {
        log.info("[ws-unsubscribe] socket disconnect: {}", event.getMessage());
        this.unregister(event.getUser());

    }

    @EventListener
    public void handleDisconnectListener(SessionDisconnectEvent event) {
        log.info("[ws-disconnect] socket disconnect: {}", event.getMessage());
        this.unregister(event.getUser());

    }

    private void unregister(Principal principal) {
        if (principal != null) {
            if (timelineService.unregister(principal.getName())) {
                log.info("[ws] unregister: {}", principal.getName());
            } else {
                log.info("[ws] already unregistered: {}", principal.getName());
            }
        } else {
            log.warn("[ws] Fail to unregister since principal is NULL!");
        }
    }
}

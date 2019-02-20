package org.jcy.timeline.web.websocket;

import com.sun.security.auth.UserPrincipal;
import org.jcy.timeline.web.service.TimelineService;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.util.StringUtils;

public class WebSocketHandleInterceptor extends ChannelInterceptorAdapter {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String sessionId = accessor.getFirstNativeHeader("sessionId");
            // 若sessionId未注册，websocket连接不成功
            if (StringUtils.isEmpty(sessionId) || !TimelineService.isValid(sessionId)) {
                return null;
            }
            accessor.setUser(new UserPrincipal(sessionId));
        }
        return message;
    }
}

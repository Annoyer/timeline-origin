package org.jcy.timeline.web.service;

import org.jcy.timeline.web.model.UpdateInfo;
import org.jcy.timeline.web.websocket.WebSocketConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class AutoUpdateService {

	@Autowired
	private SimpMessagingTemplate sender;

	private static final String TOPIC = WebSocketConfig.UNICAST_PREFIX + "/autoUpdate";

	/**
	 *
	 * @param sessionId
	 * @param itemList
	 */
	public void notifyUpdate(String sessionId, UpdateInfo itemList) {
		sender.convertAndSendToUser(sessionId, TOPIC, itemList);
	}

}
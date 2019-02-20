package org.jcy.timeline.web.util;

import org.jcy.timeline.web.model.UpdateInfo;
import org.jcy.timeline.web.service.AutoUpdateService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class MessageUtils implements ApplicationContextAware {

    private static AutoUpdateService autoUpdateService;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        autoUpdateService = applicationContext.getBean(AutoUpdateService.class);
    }

    public static void send(String sessionId, UpdateInfo updateInfo) {
        autoUpdateService.notifyUpdate(sessionId, updateInfo);
    }

}

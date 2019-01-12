package org.jcy.timeline.web;

import org.eclipse.rap.rwt.application.Application;
import org.eclipse.rap.rwt.application.ApplicationConfiguration;

import static com.eclipsesource.tabris.TabrisClientInstaller.install;

public class Configuration implements ApplicationConfiguration {

    @Override
    public void configure(Application application) {
        install(application);
        application.addEntryPoint("/timeline", TimelineEntryPoint.class, null);
    }
}
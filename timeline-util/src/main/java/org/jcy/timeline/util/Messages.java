package org.jcy.timeline.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public final class Messages {

    private static final Logger log = LoggerFactory.getLogger(Messages.class);

    private static final String FILE_LOCATION;

    private static final Properties properties;

    static {
        FILE_LOCATION = Messages.class.getResource("/").getPath() + "messages.properties";
        properties = new Properties();
        try {
            FileInputStream inputStream = new FileInputStream(FILE_LOCATION);
            properties.load(inputStream);
        } catch (FileNotFoundException e) {
            log.error("Fail to find the message pattern properties file with path [{}].", FILE_LOCATION, e);
        } catch (IOException e) {
            log.error("Fail to load the message pattern properties from the path [{}].", FILE_LOCATION, e);
        }
    }

    public static String get(String patternName) {
        return properties.getProperty(patternName, "");
    }

    public static String get(String patternName, int num) {
        return String.format(properties.getProperty(patternName, ""), num);
    }

    public static String get(String patternName, Object...args) {
        return String.format(properties.getProperty(patternName, ""), args);
    }
}

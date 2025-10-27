package de.construkter.marvinjuniorbot.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {

    private final Properties properties = new Properties();

    public Config(String path) {
        try (FileInputStream fis = new FileInputStream(path)) {
            properties.load(fis);
        } catch (IOException e) {
            Logger logger = LoggerFactory.getLogger(getClass());
            logger.error("Unable to load config file {}", path);
        }
    }

    public String get(String key) {
        return properties.getProperty(key);
    }
}

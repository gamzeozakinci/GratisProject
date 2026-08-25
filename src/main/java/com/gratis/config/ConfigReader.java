package com.gratis.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public final class ConfigReader {

    private static final Logger log = LoggerFactory.getLogger(ConfigReader.class);
    private static final Properties PROPS = new Properties();

    static {
        try (InputStream is = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (is == null) {
                throw new RuntimeException("config.properties not found on classpath. " +
                        "Check that src/test/resources is not excluded from the build output.");
            }
            PROPS.load(new InputStreamReader(is, StandardCharsets.UTF_8));
            log.info("Loaded config.properties with {} keys", PROPS.size());
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    private ConfigReader() {
    }

    public static String get(String key) {
        String value = System.getProperty(key, PROPS.getProperty(key));
        if (value == null) {
            throw new RuntimeException("Missing config key: " + key);
        }
        return value;
    }

    public static String get(String key, String defaultValue) {
        return System.getProperty(key, PROPS.getProperty(key, defaultValue));
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }

    public static String baseUrl() {
        return get("base.url");
    }
}

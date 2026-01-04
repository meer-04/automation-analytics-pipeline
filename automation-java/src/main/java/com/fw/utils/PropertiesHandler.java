package com.fw.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import static org.apache.logging.log4j.Level.ERROR;

public class PropertiesHandler {

    private static final Map<String, Properties> propertiesCache = new HashMap<>();

    private static Properties loadFile(String fileNameWithPath) {
        String fileName = fileNameWithPath.contains("/")
                ? fileNameWithPath.substring(fileNameWithPath.lastIndexOf("/") + 1)
                : fileNameWithPath;
        if (!propertiesCache.containsKey(fileName)) {
            Properties props = new Properties();

            try (InputStream input = new FileInputStream(fileNameWithPath)) {
                props.load(input);
                propertiesCache.put(fileName, props);
            } catch (IOException ex) {
                Logger logger = new Logger(PropertiesHandler.class);
                logger.logMessage(ERROR, "Could not find or load file: " + fileNameWithPath +
                        "\n" + ex.getMessage());
            }
        }
        return propertiesCache.get(fileName);
    }

    public static String getProperty(String fileName, String key) {
        return loadFile(fileName).getProperty(key);
    }

    public static Properties getAllProperties(String fileNameWithPath) {
        return loadFile(fileNameWithPath);
    }

    public static void clearCache() {
        propertiesCache.clear();
    }
}
package com.fw.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

public class PropertiesHandler {

    private static final Map<String, Properties> propertiesCache = new HashMap<>();
    private static final String RESOURCE_PATH = "src/test/resources/";

    private static Properties loadFile(String fileName) {
        if (!propertiesCache.containsKey(fileName)) {
            Properties props = new Properties();
            String fullPath = RESOURCE_PATH + fileName + ".properties";

            try (InputStream input = new FileInputStream(fullPath)) {
                props.load(input);
                propertiesCache.put(fileName, props);
            } catch (IOException ex) {
                throw new RuntimeException("Could not find or load file: " + fullPath, ex);
            }
        }
        return propertiesCache.get(fileName);
    }

    public static String getProperty(String fileName, String key) {
        return loadFile(fileName).getProperty(key);
    }

    public static Properties getAllProperties(String fileName) {
        return loadFile(fileName);
    }

    public static void clearCache() {
        propertiesCache.clear();
    }
}
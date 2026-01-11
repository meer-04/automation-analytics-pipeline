package com.fw.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Map;
import java.util.HashMap;

public class PropertiesHandler {

    private static final Map<String, Properties> propertiesCache = new HashMap<>();
    private static final String RESOURCE_PATH = "src/test/resources/properties/";

    private static Properties loadFile(String fileName) {
        if (!propertiesCache.containsKey(fileName)) {
            Properties props = new Properties();
            String fileNameWithPath = RESOURCE_PATH + fileName + ".properties";
            try (InputStream input = new FileInputStream(fileNameWithPath)) {
                props.load(input);
                propertiesCache.put(fileName, props);
            } catch (IOException ex) {
                Logger logger = new Logger(PropertiesHandler.class);
                throw new FrameworkException("Could not find or load file: " + fileNameWithPath, ex);
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
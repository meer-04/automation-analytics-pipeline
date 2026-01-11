package com.fw.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;

public class YamlLoader {

    private static final ObjectMapper MAPPER = new ObjectMapper(new YAMLFactory()).findAndRegisterModules();

    private static final ConcurrentHashMap<String, Object> CACHE = new ConcurrentHashMap<>();

    private YamlLoader() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T load(String yamlFileName, Class<T> clazz) {
        return (T) CACHE.computeIfAbsent(yamlFileName, key -> readYaml(key, clazz));
    }

    private static <T> T readYaml(String yamlFileName, Class<T> clazz) {
        try (InputStream is = YamlLoader.class.getClassLoader().getResourceAsStream(yamlFileName)) {
            if (is == null) {
                throw new FrameworkException("YAML file not found in classpath: " + yamlFileName);
            }
            return MAPPER.readValue(is, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load YAML: " + yamlFileName, e);
        }
    }
}



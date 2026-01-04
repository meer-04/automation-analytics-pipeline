package com.fw.core;

import com.fw.utils.Logger;
import org.apache.logging.log4j.Level;

import java.util.Arrays;

public enum ExecutionPlatform {
    CHROME, EDGE, FIREFOX, SAFARI;

    public static ExecutionPlatform getPlatformName(String browserName) {
        try {
            return ExecutionPlatform.valueOf(browserName.toUpperCase());
        } catch (IllegalArgumentException e) {
            Logger logger = new Logger(ExecutionPlatform.class);
            logger.logMessage(Level.ERROR, "The platform '" + browserName +
                    "' is not supported. Supported platforms are: " + Arrays.toString(ExecutionPlatform.values()) +
                    "\n" + e.getMessage());
        }
        return null;
    }
}

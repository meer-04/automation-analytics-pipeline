package com.fw.core;

import com.fw.utils.FrameworkException;

import java.util.Arrays;

public enum ExecutionPlatform {
    CHROME, EDGE, FIREFOX, SAFARI;

    public static ExecutionPlatform getPlatformName(String browserName) {
        try {
            return ExecutionPlatform.valueOf(browserName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new FrameworkException("The platform '" + browserName +
                    "' is not supported. Supported platforms are: " + Arrays.toString(ExecutionPlatform.values()), e);
        }
    }
}

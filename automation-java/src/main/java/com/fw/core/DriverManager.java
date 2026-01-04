package com.fw.core;

import lombok.AccessLevel;
import lombok.Getter;
import org.openqa.selenium.WebDriver;

public class DriverManager {

    @Getter(AccessLevel.PACKAGE)
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    public static WebDriver getDriver() {
        return getDriverThreadLocal().get();
    }

    static void setDriver(WebDriver driver) {
        getDriverThreadLocal().set(driver);
    }

    public static void unload() {
        if (getDriver() != null) {
            getDriver().quit();
            driverThreadLocal.remove();
        }
    }
}


package com.fw.core;

import lombok.AccessLevel;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;

public class DriverManager extends DriverFactory {

    @Getter(AccessLevel.PACKAGE)
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    public static WebDriver getDriver() {
        return getDriverThreadLocal().get();
    }

    static void setDriver(WebDriver driver) {
        getDriverThreadLocal().set(driver);
    }

    @AfterMethod
    public void quitDriver() {
        if (getDriver() != null) {
            getDriver().quit();
            getDriverThreadLocal().remove();
        }
    }
}


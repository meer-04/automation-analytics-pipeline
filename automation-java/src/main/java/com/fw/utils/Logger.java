package com.fw.utils;

import io.qameta.allure.Step;
import org.apache.logging.log4j.Level;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class Logger {

    org.apache.logging.log4j.Logger logger = null;
    private WebDriver driver;

    public Logger(Class<?> clazz) {
        this.logger = org.apache.logging.log4j.LogManager.getLogger(clazz);
    }

    public Logger(Class<?> clazz, WebDriver driver) {
        this.logger = org.apache.logging.log4j.LogManager.getLogger(clazz);
        this.driver = driver;
    }

    @Step("{0}: {1}")
    public void logMessage(Level level, String message) {
        logger.atLevel(level).log(message);
        System.out.println(level + ": " + message);
        if (level.equals(Level.ERROR)) throw new AssertionError(message);
    }

    @Step("{0}: {1}")
    public void logMessageWithScreenshot(Level level, String message) {
        logger.atLevel(level).log(message);
        System.out.println(level + ": " + message);
        ScreenshotUtils.captureAllureScreenshot(driver, "LogScreenshot");
        if (level.equals(Level.ERROR)) throw new AssertionError(message);
    }

    @Step("Assert: {2}")
    public void logAssertion(Object actual, Object expected, String message) {
        if (actual.equals(expected)) {
            System.out.println("Assertion Passed: " + actual + " equals " + expected);
            logger.atLevel(Level.INFO).log("Assertion Passed: " + actual + " equals " + expected);
        } else {
            System.out.println("Assertion Failed: " + actual + " does not equal " + expected);
            logger.atLevel(Level.ERROR).log("Assertion Failed: " + actual + " does not equal " + expected);
            ScreenshotUtils.captureAllureScreenshot(driver, "AssertionFailureScreenshot");
            Assert.assertEquals(actual, expected, message);
        }
    }

}

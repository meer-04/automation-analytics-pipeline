package com.fw.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public final class PageLoadWait {

    // As it's utility class no objects should be created
    private PageLoadWait() {
    }

    public static void waitForPageLoad(WebDriver driver, Duration timeout) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, timeout);

            wait.until(webDriver ->
                    ((JavascriptExecutor) webDriver)
                            .executeScript("return document.readyState")
                            .equals("complete")
            );

        } catch (Exception e) {
            throw new FrameworkException("Page has not finished loading yet", e);
        }
    }
}

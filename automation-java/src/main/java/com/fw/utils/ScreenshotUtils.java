package com.fw.utils;

import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtils {

    // Attaches screenshot to Allure Report
    public static void captureAllureScreenshot(WebDriver driver, String screenshotName) {
        if (driver != null) {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment(screenshotName + "_" + timeStamp, new ByteArrayInputStream(screenshot));
        }
    }

    // Attaches screenshot to Cucumber HTML Report
    public static void captureCucumberScreenshot(WebDriver driver, Scenario scenario) {
        if (driver != null) {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", scenario.getName() + "_" + timeStamp);
        }
    }
}
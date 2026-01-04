package com.fw.listeners;


import com.fw.core.DriverManager;
import com.fw.utils.ScreenshotUtils;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        // Get driver from your DriverManager class
        WebDriver driver = DriverManager.getDriver();
        ScreenshotUtils.captureAllureScreenshot(driver, "FailureScreenshot");
    }
}

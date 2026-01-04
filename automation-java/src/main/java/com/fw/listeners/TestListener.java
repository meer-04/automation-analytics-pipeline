package com.fw.listeners;


import com.fw.core.DriverManager;
import com.fw.utils.PropertiesHandler;
import com.fw.utils.ScreenshotUtils;
import lombok.SneakyThrows;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.Map;
import java.util.Properties;

public class TestListener implements ITestListener {

    private static final String CONFIG_PATH = "src/test/resources/configproperties/config.properties";
    private static final Properties CONFIG_PROPERTIES = PropertiesHandler.getAllProperties(CONFIG_PATH);

    @SneakyThrows
    public void onStart(ITestContext iTestContext) {
        fetchXmlValues(iTestContext);

    }

    @Override
    public void onTestFailure(ITestResult result) {
        // Get driver from your DriverManager class
        WebDriver driver = DriverManager.getDriver();
        ScreenshotUtils.captureAllureScreenshot(driver, "FailureScreenshot");
    }

    private void fetchXmlValues(ITestContext iTestContext) {
        Map<String, String> s = iTestContext.getCurrentXmlTest().getAllParameters();
        CONFIG_PROPERTIES.putAll(s);
        for (String key : CONFIG_PROPERTIES.stringPropertyNames()) {
            if (System.getProperty(key) != null) {
                CONFIG_PROPERTIES.put(key, System.getProperty(key));
            }
        }
    }
}

package com.fw.listeners;


import com.fw.core.DriverManager;
import com.fw.utils.Logger;
import com.fw.utils.ScreenshotUtils;
import io.cucumber.testng.FeatureWrapper;
import io.cucumber.testng.PickleWrapper;
import org.apache.logging.log4j.Level;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        String scenarioName = "Unknown Scenario";
        for (Object parameter : result.getParameters()) {
            if (parameter instanceof PickleWrapper) {
                scenarioName = ((PickleWrapper) parameter).getPickle().getName();
            }
        }
        String thread = Thread.currentThread().getName();

        new Logger(TestListener.class).logMessage(Level.ERROR, "\n*******************Test failed*******************" +
                "\nScenario: " + scenarioName +
                "\nThread: " + thread +
                "\nThrowable: " + result.getThrowable() +
                "\n***********************************************");
    }
}

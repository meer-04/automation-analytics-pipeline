package com.fw.hooks;

import com.fw.core.DriverFactory;
import com.fw.core.DriverManager;
import com.fw.utils.ScreenshotUtils;
import io.cucumber.java.After;
import io.cucumber.java.Before;

public class ServiceHooks {

    @Before(order = 1)
    public void setup() {
        DriverFactory.getInstance().initializeDriver();
    }

    @After(order = 1)
    public void tearDown() {
        ScreenshotUtils.captureAllureScreenshot(DriverManager.getDriver(), "TearDownScreenshot");
        DriverManager.unload();
    }

}

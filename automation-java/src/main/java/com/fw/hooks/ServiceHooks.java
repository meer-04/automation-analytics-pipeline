package com.fw.hooks;

import com.fw.core.DriverFactory;
import com.fw.core.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class ServiceHooks {

    @Before(order = 1)
    public void setup() {
        DriverFactory.getInstance().initializeDriver();
    }

    @After(order = 1)
    public void tearDown(Scenario scenario) {
        DriverManager.unload();
    }
}

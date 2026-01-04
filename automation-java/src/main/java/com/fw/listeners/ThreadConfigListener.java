package com.fw.listeners;

import org.testng.ISuite;
import org.testng.ISuiteListener;

public class ThreadConfigListener implements ISuiteListener {
    @Override
    public void onStart(ISuite suite) {
        String mavenThreadCount = System.getProperty("threads");

        if (mavenThreadCount != null && !mavenThreadCount.isEmpty()) {
            // If Maven arg exists, override the XML value
            int count = Integer.parseInt(mavenThreadCount);
            suite.getXmlSuite().setDataProviderThreadCount(count);
            System.out.println("MAVEN OVERRIDE: Data Provider Thread Count set to: " + count);
        } else {
            System.out.println("XML CONFIG: Using thread count from testng.xml: "
                    + suite.getXmlSuite().getDataProviderThreadCount());
        }
    }
}
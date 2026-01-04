package com.fw.listeners;

import com.fw.utils.Logger;
import org.apache.logging.log4j.Level;
import org.testng.ISuite;
import org.testng.ISuiteListener;

public class ThreadConfigListener implements ISuiteListener {
    @Override
    public void onStart(ISuite suite) {
        Logger logger = new Logger(ThreadConfigListener.class);
        String mavenThreadCount = System.getProperty("threads");

        if (mavenThreadCount != null && !mavenThreadCount.isEmpty()) {
            // If Maven arg exists, override the XML value
            int count = Integer.parseInt(mavenThreadCount);
            if (count > 0) {
                suite.getXmlSuite().setDataProviderThreadCount(count);
                logger.logMessage(Level.INFO, "MAVEN OVERRIDE: Data Provider Thread Count set to: " + count);
            }
        } else {
            logger.logMessage(Level.INFO, "XML CONFIG: Using thread count from testng.xml: "
                    + suite.getXmlSuite().getDataProviderThreadCount());
        }
    }
}
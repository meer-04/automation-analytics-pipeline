package com.fw.core;

import com.fw.utils.PropertiesHandler;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.testng.Reporter;

import java.util.Properties;

@Getter(AccessLevel.PACKAGE)
@Setter(AccessLevel.PACKAGE)
public class ExecutionParameters {
    private static final String CONFIG_PATH = "src/test/resources/configproperties/config.properties";
    private static final Properties configProperties = PropertiesHandler.getAllProperties(CONFIG_PATH);
    private String browser;
    private String url;

    public ExecutionParameters() {
        System.out.println("Fetching Execution Parameters...");
        this.browser = setValues("browser");
        this.url = setValues("url");
    }

    String setValues(String key) {
        if (null != System.getProperty(key))
            return System.getProperty(key);
        String xmlPRop = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter(key);
        if (xmlPRop != null) return xmlPRop;
        if (configProperties != null) {
            String prop = configProperties.getProperty(key);
            if (prop != null && !prop.isEmpty())
                return prop;
        }
        return null;
    }
}

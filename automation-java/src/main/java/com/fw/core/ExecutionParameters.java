package com.fw.core;

import com.fw.utils.PropertiesHandler;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.util.Properties;

@Getter(AccessLevel.PACKAGE)
@Setter(AccessLevel.PACKAGE)
public class ExecutionParameters {
    private Properties configProperties = PropertiesHandler.getAllProperties("config");
    private String browser;
    private String url;

    @BeforeClass(alwaysRun = true)
    @Parameters({"browser", "url"})
    public void setupParameters(@Optional String browser, @Optional String url) {
        this.browser = setValues("browser", browser);
        this.url = setValues("url", url);
    }

    String setValues(String key, String xmlParameter) {
        if (null != System.getProperty(key))
            return System.getProperty(key);
        if (null != xmlParameter && !xmlParameter.isEmpty())
            return xmlParameter;
        if (configProperties != null) {
            String prop = configProperties.getProperty(key);
            if (prop != null && !prop.isEmpty())
                return prop;
        }
        return null;
    }
}

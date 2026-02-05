package com.fw.core;

import com.fw.utils.FrameworkException;
import com.fw.utils.Logger;
import com.fw.utils.PageLoadWait;
import com.fw.utils.PropertiesHandler;
import org.apache.logging.log4j.Level;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;


public class DriverFactory extends ExecutionParameters {
    private static final Properties CONFIG_PROPERTIES = PropertiesHandler.getAllProperties("config");
    private static DriverFactory driverFactoryInstance = null;
    Logger logger;

    // Private constructor prevents anyone else from saying "new DriverFactory()"
    private DriverFactory() {
        super();
        logger = new Logger(DriverFactory.class);
    }

    public static DriverFactory getInstance() {
        if (driverFactoryInstance == null) {
            driverFactoryInstance = new DriverFactory();
        }
        return driverFactoryInstance;
    }

    public void initializeDriver() {
        ExecutionPlatform executionPlatform = ExecutionPlatform.getPlatformName(getBrowser());
        createDriverInstance(executionPlatform);
        logger.logMessage(Level.INFO, "Initialized " + executionPlatform + " driver");
        setGlobalWaits();
        DriverManager.getDriver().manage().window().maximize();
        logger.logMessage(Level.INFO, "Maximized the browser window");
        DriverManager.getDriver().get(getUrl());
        logger.logMessage(Level.INFO, "Navigated to URL: " + getUrl());
        PageLoadWait.waitForPageLoad(DriverManager.getDriver(), Duration.ofSeconds(30));
    }

    private void createDriverInstance(ExecutionPlatform platform) {
        try {
            MutableCapabilities capabilities = setCapabilities(platform);
            DriverManager.setDriver(configureDriver(platform, capabilities));
        } catch (Exception e) {
            throw new FrameworkException("Failed to initialize " + platform +
                    " driver. Check configurations. Error: ", e);
        }
    }

    private MutableCapabilities setCapabilities(ExecutionPlatform platform) {
        return switch (platform) {
            case CHROME -> setChromeOptions();
            case EDGE -> setEdgeOptions();
            case FIREFOX -> setFirefoxOptions();
            case SAFARI -> {
                if (!System.getProperty("os.name").toLowerCase().contains("mac")) {
                    throw new FrameworkException(platform + " execution is only supported on macOS. Current OS: "
                            + System.getProperty("os.name"));
                }
                yield setSafariOptions();
            }
        };
    }

    private WebDriver configureDriver(ExecutionPlatform platform, MutableCapabilities capabilities) {
        switch (platform) {
            case CHROME:
                return new ChromeDriver((ChromeOptions) capabilities);
            case FIREFOX:
                return new FirefoxDriver((FirefoxOptions) capabilities);
            case EDGE:
                return new EdgeDriver((EdgeOptions) capabilities);
            case SAFARI:
                return new SafariDriver((SafariOptions) capabilities);
        }
        return null;
    }

    private ChromeOptions setChromeOptions() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setAcceptInsecureCerts(true);

        // Argument Handling
        List<String> argsList = new ArrayList<>();
        if (isHeadless())
            argsList.add("--headless=new");
        String extraArgs = CONFIG_PROPERTIES.getProperty("chrome.options");
        if (extraArgs != null && !extraArgs.isEmpty()) {
            argsList.addAll(Arrays.asList(extraArgs.split(";")));
        }
        chromeOptions.addArguments(argsList);
        // Capabilities Handling
        Map<String, Object> additionalCaps = getAdditionalCapabilities("chrome");
        if (additionalCaps != null) {
            additionalCaps.forEach(chromeOptions::setCapability);
        }
        return chromeOptions;
    }

    private EdgeOptions setEdgeOptions() {
        EdgeOptions edgeOptions = new EdgeOptions();
        edgeOptions.setAcceptInsecureCerts(true);

        // Argument Handling
        List<String> argsList = new ArrayList<>();
        if (isHeadless())
            argsList.add("--headless");
        String extraArgs = CONFIG_PROPERTIES.getProperty("edge.options");
        if (extraArgs != null && !extraArgs.isEmpty()) {
            argsList.addAll(Arrays.asList(extraArgs.split(";")));
        }
        edgeOptions.addArguments(argsList);
        // Capabilities Handling
        Map<String, Object> additionalCaps = getAdditionalCapabilities("edge");
        if (additionalCaps != null) {
            additionalCaps.forEach(edgeOptions::setCapability);
        }
        return edgeOptions;
    }

    private FirefoxOptions setFirefoxOptions() {
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setAcceptInsecureCerts(true);
        // Argument Handling
        List<String> argsList = new ArrayList<>();
        if (isHeadless())
            argsList.add("--headless");
        String extraArgs = CONFIG_PROPERTIES.getProperty("firefox.options");
        if (extraArgs != null && !extraArgs.isEmpty()) {
            argsList.addAll(Arrays.asList(extraArgs.split(";")));
        }
        firefoxOptions.addArguments(argsList);
        // Capabilities Handling
        Map<String, Object> additionalCaps = getAdditionalCapabilities("firefox");
        if (additionalCaps != null) {
            additionalCaps.forEach(firefoxOptions::setCapability);
        }
        // Preferences Handling
        Map<String, Object> additionalPref = getAdditionalPreferences();
        if (additionalPref != null) {
            additionalPref.forEach((key, value) -> {
                if (value instanceof Integer) {
                    firefoxOptions.addPreference(key, (Integer) value);
                } else if (value instanceof Boolean) {
                    firefoxOptions.addPreference(key, (Boolean) value);
                } else {
                    firefoxOptions.addPreference(key, String.valueOf(value));
                }
            });
        }
        return firefoxOptions;
    }

    private SafariOptions setSafariOptions() {
        SafariOptions safariOptions = new SafariOptions();
        safariOptions.setAcceptInsecureCerts(true);
        safariOptions.setCapability("safari:cleanSession", true);
        // Capabilities Handling
        Map<String, Object> additionalCaps = getAdditionalCapabilities("safari");
        if (additionalCaps != null) {
            additionalCaps.forEach(safariOptions::setCapability);
        }
        return safariOptions;
    }

    private void setGlobalWaits() {
        if (CONFIG_PROPERTIES.getProperty("page.load.timeout") != null) {
            DriverManager.getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(Long.parseLong(CONFIG_PROPERTIES.getProperty("page.load.timeout"))));
        }
        if (CONFIG_PROPERTIES.getProperty("implicit.wait") != null) {
            DriverManager.getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(Long.parseLong(CONFIG_PROPERTIES.getProperty("implicit.wait"))));
        }
        if (CONFIG_PROPERTIES.getProperty("script.wait") != null) {
            DriverManager.getDriver().manage().timeouts().scriptTimeout(Duration.ofSeconds(Long.parseLong(CONFIG_PROPERTIES.getProperty("script.wait"))));
        }
    }


    private Map<String, Object> getAdditionalCapabilities(String browser) {
        return getBrowserPropertiesFromConfig(browser, "caps").entrySet().stream().collect(Collectors.toMap(
                k -> String.valueOf(k.getKey()),
                v -> v.getValue()));
    }

    private Map<String, Object> getAdditionalPreferences() {
        return getBrowserPropertiesFromConfig("firefox", "prefs").entrySet().stream().collect(Collectors.toMap(
                e -> String.valueOf(e.getKey()),
                e -> e.getValue()));
    }

    private Map<String, Object> getBrowserPropertiesFromConfig(String browser, String prefsOrCaps) {
        Map<String, Object> browserProperties = new HashMap<>();
        CONFIG_PROPERTIES.forEach((key, value) -> {
            if (key.toString().startsWith(browser + "." + prefsOrCaps + ".")) {
                String cleanKey = key.toString().replace(browser + "." + prefsOrCaps + ".", "");
                browserProperties.put(cleanKey, parseValue(String.valueOf(value)));
            }
        });
        return browserProperties;
    }

    private Object parseValue(String value) {
        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false"))
            return Boolean.parseBoolean(value);
        if (value.matches("-?\\d+"))
            return Integer.parseInt(value);
        return value;
    }

}

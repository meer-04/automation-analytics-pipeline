package com.fw.core;

import com.fw.utils.FrameworkException;
import com.fw.utils.Logger;
import com.fw.utils.PropertiesHandler;
import lombok.Getter;
import org.apache.logging.log4j.Level;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Properties;

public abstract class Base {

    public WebDriver driver;
    @Getter
    private JavascriptExecutor javascriptExecutor;
    private final Logger logger;
    private final WebDriverWait wait;

    public Base(WebDriver driver) {
        this.driver = driver;
        this.javascriptExecutor = (JavascriptExecutor) this.driver;
        this.logger = new Logger(Base.class);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    protected WebElement getElement(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (NoSuchElementException e) {
            throw new FrameworkException("Element not found: " + locator, e);
        }
    }

    protected void sleep(Integer milliSeconds) {
        long secondsLong = (long) milliSeconds;
        try {
            Thread.sleep(secondsLong);
        } catch (InterruptedException e) {
            throw new FrameworkException("Error during wait.", e);
        }
    }

    protected void clickElement(By by) {
        getElement(by).click();
    }

    protected void enterValue(By by, CharSequence... charSequences) {
        getElement(by).sendKeys(charSequences);
    }

    protected String getText(By by) {
        return getElement(by).getText();
    }

    protected String getText(By by, String defaultValue) {
        try {
            return getElement(by).getText();
        } catch (TimeoutException e) {
            return defaultValue;
        }
    }

    protected String getAttribute(By by, String attributeName) {
        return getElement(by).getAttribute(attributeName);
    }

    protected boolean isElementDisplayed(By by) {
        try {
            return getElement(by).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    protected boolean isElementEnabled(By by) {
        return getElement(by).isEnabled();
    }

    protected boolean isElementSelected(By by) {
        return getElement(by).isSelected();
    }

    protected void selectFromDropDownByIndex(By by, int index) {
        getDropDownElement(by).selectByIndex(index);
    }

    protected void selectFromDropDownByValue(By by, String value) {
        getDropDownElement(by).selectByValue(value);
    }

    protected void selectFromDropDownByVisibleText(By by, String visibleText) {
        getDropDownElement(by).selectByVisibleText(visibleText);
    }

    protected Select getDropDownElement(By by) {
        return new Select(getElement(by));
    }

    protected Properties properties(String fileName) {
        return PropertiesHandler.getAllProperties(fileName);
    }

    protected WebDriverWait webDriverWait(int seconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(seconds));
    }

    protected void pressKey(Keys key) {
        Actions actions = new Actions(driver);
        actions.sendKeys(key).build().perform();
    }

}



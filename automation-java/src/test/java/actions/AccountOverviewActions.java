package actions;

import com.fw.core.Base;
import com.fw.core.DriverManager;
import com.fw.utils.FrameworkException;
import com.fw.utils.Logger;
import com.fw.utils.PageLoadWait;
import io.qameta.allure.Param;
import io.qameta.allure.Step;
import io.qameta.allure.model.Parameter;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

public class AccountOverviewActions extends Base {
    private static final By TXT_USERNAME = By.name("username");
    private static final By TXT_PASS = By.name("password");
    private static final By BTN_LOGIN = By.className("button");
    private final Logger logger;

    public AccountOverviewActions() {
        super(DriverManager.getDriver());
        this.logger = new Logger(AccountOverviewActions.class);
    }

    @Step("Fetch data from account overview table and store to {fileName} file")
    public void fetchDataFromTableAndStoreToFile(String fileName) {
        String url = properties("config").getProperty("url");
        if (!driver.getCurrentUrl().equals(url)) {
            driver.get(url);
        }
    }



}

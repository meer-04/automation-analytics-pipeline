package actions;

import com.fw.core.Base;
import com.fw.core.DriverManager;
import com.fw.utils.Logger;
import com.fw.utils.PageLoadWait;
import io.qameta.allure.Param;
import io.qameta.allure.Step;
import io.qameta.allure.model.Parameter;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.time.Duration;

public class LoginActions extends Base {
    private static final By TXT_USERNAME = By.name("username");
    private static final By TXT_PASS = By.name("password");
    private static final By BTN_LOGIN = By.className("button");
    private final Logger logger;

    public LoginActions() {
        super(DriverManager.getDriver());
        this.logger = new Logger(LoginActions.class);
    }

    @Step("Navigate to Login Page")
    public void navigateToLoginPage() {
        String url = properties("config").getProperty("url");
        if (!driver.getCurrentUrl().equals(url)) {
            driver.get(url);
        }
    }

    @Step("Enter invalid credentials for {username}")
    public void enterInvalidCredentialsFor(String username) {
        String user = properties("user").getProperty("test." + username);
        loginParaBank(user, RandomStringUtils.randomAlphanumeric(8));
    }

    @Step("Login to ParaBank with username: {user} and password")
    private void loginParaBank(String user, @Param(mode = Parameter.Mode.HIDDEN) String pass) {
        enterValue(TXT_USERNAME, user);
        enterValue(TXT_PASS, pass);
        pressKey(Keys.ENTER);
        PageLoadWait.waitForPageLoad(driver, Duration.ofSeconds(30));
    }

}

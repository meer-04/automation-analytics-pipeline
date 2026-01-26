package actions;

import com.fw.core.Base;
import com.fw.core.DriverManager;
import com.fw.utils.CSVUtil;
import com.fw.utils.FrameworkException;
import com.fw.utils.Logger;
import com.fw.utils.PageLoadWait;
import io.qameta.allure.Param;
import io.qameta.allure.Step;
import io.qameta.allure.model.Parameter;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.Level;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class AccountOverviewActions extends Base {
    private static final By ACC_NO = By.xpath("//table[@id='accountTable']//td/a");
    private static final By ACC_BAL = By.xpath("//table[@id='accountTable']//td[2]");
    private static final By TRANS_DATE = By.xpath("//table[@id='transactionTable']//td[1]");
    private static final By TRANS_DESC = By.xpath("//table[@id='transactionTable']//td[2]");
    private static final By TRANS_DEBT_AMT = By.xpath("//table[@id='transactionTable']//td[3]");
    private static final By TRANS_CRED_AMT = By.xpath("//table[@id='transactionTable']//td[4]");
    private static final By NO_TRANS = By.id("noTransactions");
    private static final String HEADERS = "Account Number,Account Balance,Transaction Date,Transaction Description,Debit Amount,Credit Amount";
    private final Logger logger;

    public AccountOverviewActions() {
        super(DriverManager.getDriver());
        this.logger = new Logger(AccountOverviewActions.class);
    }

    @Step("Fetch data from account overview table and store to {fileName} file")
    public void fetchDataFromTableAndStoreToFile(String fileName) {
        List<List<String>> accountData = new ArrayList<>();

        logger.logMessage(Level.INFO, "Starting data fetch from account overview page");
        webDriverWait(10).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(ACC_NO));

        List<WebElement> accNo = driver.findElements(ACC_NO);
        List<WebElement> accBal = driver.findElements(ACC_BAL);
        int accCount = driver.findElements(ACC_NO).size();
        logger.logMessage(Level.INFO, "Total accounts found: " + accCount);

        for (int i = 0; i < accCount; i++) {
            String accountNumber = accNo.get(i).getText();
            String accountBalance = accBal.get(i).getText();
            accNo.get(i).click();
            PageLoadWait.waitForPageLoad(driver, Duration.ofSeconds(10));

            try {
                webDriverWait(10).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(TRANS_DATE));
            } catch (Exception e) {
                if (isElementDisplayed(NO_TRANS)) {
                    logger.logMessage(Level.WARN, "No transactions found for account " + accountNumber);
                    driver.navigate().back();
                    PageLoadWait.waitForPageLoad(driver, Duration.ofSeconds(10));
                    continue;
                } else {
                    throw new FrameworkException("Transaction table not loaded for " + accountNumber, e);
                }
            }
            List<WebElement> transDate = driver.findElements(TRANS_DATE);
            List<WebElement> transDesc = driver.findElements(TRANS_DESC);
            List<WebElement> transDebtAmt = driver.findElements(TRANS_DEBT_AMT);
            List<WebElement> transCrdAmt = driver.findElements(TRANS_CRED_AMT);
            int transCount = transDate.size();
            logger.logMessage(Level.INFO, "Total transactions for account " + accountNumber + ": " + transCount);
            for (int j = 0; j < transCount; j++) {
                accountData.add(List.of(
                        accountNumber,
                        accountBalance,
                        transDate.get(j).getText(),
                        transDesc.get(j).getText(),
                        transDebtAmt.get(j).getText(),
                        transCrdAmt.get(j).getText()));
            }
            logger.logMessage(Level.INFO, "Fetched " + transCount + " transactions for account " + accountNumber);
            driver.navigate().back();
            PageLoadWait.waitForPageLoad(driver, Duration.ofSeconds(10));
            logger.logMessage(Level.INFO, "Navigated back to account overview page");
        }

        if (accountData.isEmpty()) {
            throw new FrameworkException("No account data fetched. Check if accounts have transactions. Exiting without writing to file.");
        }

        logger.logMessage(Level.INFO, "Data fetch complete. Writing to file: " + fileName);
        Path path = Path.of(properties("config").getProperty("csv.path"), fileName + ".csv");
        List<String> headers = Arrays.stream(HEADERS.split(","))
                .map(String::trim)
                .toList();
        CSVUtil.writeCsv(path, headers, accountData);
        logger.logMessage(Level.INFO, "Data successfully written to file: " + path);
    }

    @Step("Validate data is fetched and stored successfully in {fileName} file")
    public void validateDataIsFetchedAndStoredSuccessfullyInFile(String fileName) {
        Path path = Path.of(properties("config").getProperty("csv.path"), fileName + ".csv");
        CSVUtil.validateCsv(path);
        logger.logMessage(Level.INFO, "CSV file validation successful: " + path);
    }
}

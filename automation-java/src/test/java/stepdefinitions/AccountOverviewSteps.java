package stepdefinitions;

import actions.AccountOverviewActions;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;

public class AccountOverviewSteps {

    private final AccountOverviewActions accountOverviewActions;

    public AccountOverviewSteps(AccountOverviewActions accountOverviewActions) {
        this.accountOverviewActions = accountOverviewActions;
    }

    @And("the user fetches data from the account overview page into {string} file")
    public void theUserFetchesDataFromTheAccountOverviewPageIntoFile(String fileName) {
        accountOverviewActions.fetchDataFromTableAndStoreToFile(fileName);
    }

    @Then("the user validates the data is fetched and stored successfully in {string} file")
    public void theUserValidatesTheDataIsFetchedAndStoredSuccessfullyInFile(String fileName) {
        accountOverviewActions.validateDataIsFetchedAndStoredSuccessfullyInFile(fileName);
    }
}

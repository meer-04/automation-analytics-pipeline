package stepdefinitions;

import actions.AccountOverviewActions;
import io.cucumber.java.en.And;

public class AccountOverviewSteps {

    private final AccountOverviewActions accountOverviewActions;

    public AccountOverviewSteps(AccountOverviewActions accountOverviewActions) {
        this.accountOverviewActions = accountOverviewActions;
    }

    @And("the user fetches data from the account overview page into {string} file")
    public void theUserFetchesDataFromTheAccountOverviewPageIntoFile(String fileName) {
        accountOverviewActions.iFetchDataFromTheAccountOverviewPageIntoFile(fileName);
    }
}

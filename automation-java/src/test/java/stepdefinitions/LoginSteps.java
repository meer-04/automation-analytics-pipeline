package stepdefinitions;

import actions.LoginActions;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class LoginSteps {
    private final LoginActions loginActions;

    // Cucumber PicoContainer automatically injects this and makes thread-safe
    public LoginSteps(LoginActions loginActions) {
        this.loginActions = loginActions;
    }

    @Given("the user is on the login page")
    public void theUserIsOnTheLoginPage() {
        loginActions.navigateToLoginPage();
    }

    @When("the user enters invalid credentials for {string}")
    public void theUserEntersInvalidCredentialsFor(String username) {
        loginActions.enterInvalidCredentialsFor(username);
    }

    @Then("the user validates the login failure message")
    public void theUserValidatesTheLoginFailureMessage() {
    }

    @When("the user enters valid credentials for {string}")
    public void theUserEntersValidCredentialsFor(String username) {
        loginActions.enterValidCredentialsFor(username);
    }
}

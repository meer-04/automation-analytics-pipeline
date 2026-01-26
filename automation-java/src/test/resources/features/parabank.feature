@parabank
@allure.label.epic:AutomationDemoTest
@allure.label.feature:FeatureAutomationAndAnalysisDemoPipelineParaBank
Feature: Automation and Analysis Demo Pipeline - ParaBank

  @negativeLogin
  @issue:StoryJira-101    # This is the Jira User Story ID
  @tms:TCJira-102        # This is the Zephyr/Xray Test Case ID
  Scenario: Verify user cannot log in with invalid credentials
    Given the user is on the login page
    When the user enters invalid credentials for "user1"
    Then the user validates the login failure message

  @automation-analytics
  Scenario: Verify user can fetch account overview and analyze data
    Given the user is on the login page
    When the user enters valid credentials for "user1"
    And the user fetches data from the account overview page into "account_data" file
    Then the user validates the data is fetched and stored successfully in "account_data" file
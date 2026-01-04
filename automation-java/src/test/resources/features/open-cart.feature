@allure.label.epic:AutomationDemoTest
@allure.label.feature:FeatureOpenCart
Feature: Automation and Analysis Demo Pipeline - Open Cart

  @test1
  @issue:StoryJira-101    # This is the Jira User Story ID
  @tms:TCJira-102        # This is the Zephyr/Xray Test Case ID
  Scenario: Test Parallel Execution
    Given the user has MFA enabled
    When they enter valid credentials
    Then they are prompted for a TOTP code
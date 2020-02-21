Feature: Get Telephone Details (Integration Tests)

     @Sandbox @SIT @Integration  
  Scenario Outline: Validate Health Check of Health Board Api
    When I make a request for health check to Health Board Api
    Then I should get the response with StatusCode as <StatusCode> and health check message as <healthcheckmessage>
    Examples:
      | User               | StatusCode | healthcheckmessage    |
      | JamesValidUser     | 200        | Health Board api is up |

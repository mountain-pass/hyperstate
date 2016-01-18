Feature: Service Exposer
    In order minimise the code I need to write to produce a service
    and to ensure consistency for the services I create
    As a service developer
    I want to have REST services automatically created for my domain entity classes
    
Scenario: Expose single domain entity
    Given an "Account" domain entity with
    | username     | tom        |
    | creationDate | 2016/01/15 |
    And it has no actions
    And it has no links apart from "self"
    And it has a self link referencing "/test/testAccount"
    And it is exposed at "/test/testAccount"
    When request is made to "/test/testAccount"
    Then the response will be an "Account" domain entity with
    | username     | tom        |
    | creationDate | 2016/01/15 |
    And it will have no actions
    And it will have no links apart from "self"
    And it will have a self link referencing "/test/testAccount"

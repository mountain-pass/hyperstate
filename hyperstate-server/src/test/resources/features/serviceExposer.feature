    Feature: Service Exposer
    In order minimise the code I need to write to produce a service
    and to ensure consistency for the services I create
    As a service developer
    I want to have REST services automatically created for my domain entity classes

Scenario: Get Controller Root
    Given a Hyperstate controller "hyperstateTestController" at "/"
    When request is made to "/"
    Then the response will be a "HyperstateRootEntity" domain entity
    And it will have a self link referencing "/"

Scenario: Add Relationship
    Given a Hyperstate controller "hyperstateTestController" at "/"
    And the controller's root has an "accounts" link to an "Accounts" domain entity
    When request is made to "/"
    And its "accounts" link is followed
    Then the response will be a "Accounts" domain entity

Scenario: Expose single domain entity
    Given an "Account" domain entity with
    | username     | tom        |
    | creationDate | 2016-01-15T12:00:00 |
    And it has no actions
    And it has no additional links
    And it is exposed at "/accounts/testAccount"
    When request is made to "/accounts/testAccount" for an "au.com.mountainpass.hyperstate.server.entities.Account"
    Then the response will be an "Account" domain entity with
    | username     | tom        |
    | creationDate | 2016-01-15T12:00:00 |
    And it will have no actions
    And it will have no links apart from "self"
    And it will have a self link referencing "/accounts/testAccount"

Scenario: Expose single domain entity with delete action
    Given an "Account" domain entity with
    | username     | tom        |
    | creationDate | 2016-01-15T12:00:00 |
    And it has a "delete" action
    And it is exposed at "/accounts/testAccount"
    When request is made to "/accounts/testAccount" for an "au.com.mountainpass.hyperstate.server.entities.AccountWithDelete"
    Then the response will be an "Account" domain entity with
    | username     | tom        |
    | creationDate | 2016-01-15T12:00:00 |
    And it will have a "delete" action
    And it will have no links apart from "self"
    And it will have a self link referencing "/accounts/testAccount"

Scenario: Delete domain entity
    Given an "Account" domain entity with
    | username     | tom        |
    | creationDate | 2016-01-15T12:00:00 |
    And it has a "delete" action
    And it is exposed at "/accounts/testAccount"
    When request is made to "/accounts/testAccount" for an "au.com.mountainpass.hyperstate.server.entities.AccountWithDelete"
    And the response entity is deleted
    Then there will no longer be an entity at "/accounts/testAccount"


Scenario: Expose single domain entity with update action
    Given an "Account" domain entity with
    | username     | tom        |
    | creationDate | 2016-01-15T12:00:00 |
    And it has a "update" action
    And it is exposed at "/accounts/testAccount"
    When request is made to "/accounts/testAccount" for an "au.com.mountainpass.hyperstate.server.entities.AccountWithUpdate"
    Then the response will be an "Account" domain entity with
    | username     | tom        |
    | creationDate | 2016-01-15T12:00:00 |
    And it will have a "update" action
    And it will have no links apart from "self"
    And it will have a self link referencing "/accounts/testAccount"
    
Scenario: Update a domain entity
    Given an "Account" domain entity with
    | username     | tom        |
    | creationDate | 2016-01-15T12:00:00 |
    And it has a "update" action
    And it is exposed at "/accounts/testAccount"
    When request is made to "/accounts/testAccount" for an "au.com.mountainpass.hyperstate.server.entities.AccountWithUpdate"
    And the response entity is updated with
    | username | nick |
    Then the response will be an "Account" domain entity with
    | username     | nick       |
    | creationDate | 2016-01-15T12:00:00 |

Scenario: Expose single domain entity with create action
    Given an "Accounts" domain entity
    And it has a "createAccount" action
    And it is exposed at "/accounts"
    When request is made to "/accounts"
    Then the response will be an "Accounts" domain entity
    And it will have a "createAccount" action


@tom
Scenario: Create a domain entity
    Given an "Accounts" domain entity
    And it has a "createAccount" action
    And it is exposed at "/accounts"
    When request is made to "/accounts" for an "au.com.mountainpass.hyperstate.server.entities.Accounts"
    And the response entity's "createAccount" action is called with
    | username | nick |
    Then the response will be an "Account" domain entity with
    | username     | nick       |
    And it's creation date will be today    
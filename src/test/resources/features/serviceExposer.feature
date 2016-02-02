Feature: Service Exposer
    In order minimise the code I need to write to produce a service
    and to ensure consistency for the services I create
    As a service developer
    I want to have REST services automatically created for my domain entity classes

Scenario: Get Controller Root
    Given a Hyperstate controller "hyperstateTestController" at "/hyperstateTest"
    When request is made to "/hyperstateTest"
    Then the response will be a "HyperstateRootEntity" domain entity
    And it will have a self link referencing "/hyperstateTest"

Scenario: Add Relationship
    Given a Hyperstate controller "hyperstateTestController" at "/hyperstateTest"
    And the controller's root has an "accounts" link to an "Accounts" domain entity
    When request is made to "/hyperstateTest"
    And its "accounts" link is followed
    Then the response will be a "Accounts" domain entity

Scenario: Expose single domain entity
    Given an "Account" domain entity with
    | username     | tom        |
    | creationDate | 2016/01/15 |
    And it has no actions
    And it's only link is self link referencing "/hyperstateTest/test/testAccount"
    And it is exposed at "/hyperstateTest/test/testAccount"
    When request is made to "/hyperstateTest/test/testAccount" for an "au.com.mountainpass.hyperstate.server.entities.Account"
    Then the response will be an "Account" domain entity with
    | username     | tom        |
    | creationDate | 2016/01/15 |
    And it will have no actions
    And it will have no links apart from "self"
    And it will have a self link referencing "/hyperstateTest/test/testAccount"

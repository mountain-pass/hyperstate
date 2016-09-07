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

Scenario: Expose single domain entity - concrete
    Given an "Account" domain entity with
    | username     | tom        |
    | creationDate | 2016-01-15T12:00:00 |
    And it has no actions
    And it has no additional links
    And it is exposed at "/accounts/testAccount"
    When request is made to "/accounts/testAccount" for an "Account"
    Then the response will be an "Account" domain entity with
    | username     | tom        |
    | creationDate | 2016-01-15T12:00:00 |
    And it will have no actions
    And it will have no links apart from "self"
    And it will have a self link referencing "/accounts/testAccount"

Scenario: Expose single domain entity - generic
    Given an "Account" domain entity with
    | username     | tom        |
    | creationDate | 2016-01-15T12:00:00 |
    And it has no actions
    And it has no additional links
    And it is exposed at "/accounts/testAccount"
    When request is made to "/accounts/testAccount" for a "VanillaEntity"
    Then the response will be an "Account" domain entity with
    | username     | tom        |
    | creationDate | 2016-01-15T12:00:00 |
    And it will have no actions
    And it will have no links apart from "self"
    And it will have a self link referencing "/accounts/testAccount"


Scenario: Expose single domain entity with delete action - concrete
    Given an "Account" domain entity with
    | username     | tom        |
    | creationDate | 2016-01-15T12:00:00 |
    And it has a "delete" action
    And it is exposed at "/accounts/testAccount"
    When request is made to "/accounts/testAccount" for an "AccountWithDelete"
    Then the response will be an "Account" domain entity with
    | username     | tom        |
    | creationDate | 2016-01-15T12:00:00 |
    And it will have a "delete" action
    And it will have no links apart from "self"
    And it will have a self link referencing "/accounts/testAccount"

Scenario: Expose single domain entity with delete action - generic
    Given an "Account" domain entity with
    | username     | tom        |
    | creationDate | 2016-01-15T12:00:00 |
    And it has a "delete" action
    And it is exposed at "/accounts/testAccount"
    When request is made to "/accounts/testAccount" for a "VanillaEntity"
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
    When request is made to "/accounts/testAccount" for an "AccountWithDelete"
    And the response entity is deleted
    Then there will no longer be an entity at "/accounts/testAccount"


Scenario: Expose single domain entity with update action - concrete
    Given an "Account" domain entity with
    | username     | tom        |
    | creationDate | 2016-01-15T12:00:00 |
    And it has a "update" action
    And it is exposed at "/accounts/testAccount"
    When request is made to "/accounts/testAccount" for an "AccountWithUpdate"
    Then the response will be an "Account" domain entity with
    | username     | tom        |
    | creationDate | 2016-01-15T12:00:00 |
    And it will have a "update" action
    And it will have no links apart from "self"
    And it will have a self link referencing "/accounts/testAccount"

Scenario: Expose single domain entity with update action - generic
    Given an "Account" domain entity with
    | username     | tom        |
    | creationDate | 2016-01-15T12:00:00 |
    And it has a "update" action
    And it is exposed at "/accounts/testAccount"
    When request is made to "/accounts/testAccount" for an "VanillaEntity"
    Then the response will be an "Account" domain entity with
    | username     | tom        |
    | creationDate | 2016-01-15T12:00:00 |
    And it will have a "update" action
    And it will have no links apart from "self"
    And it will have a self link referencing "/accounts/testAccount"

    
Scenario: Update a domain entity - concrete
    Given an "Account" domain entity with
    | username     | tom        |
    | creationDate | 2016-01-15T12:00:00 |
    And it has a "update" action
    And it is exposed at "/accounts/testAccount"
    When request is made to "/accounts/testAccount" for an "AccountWithUpdate"
    And the response entity is updated with
    | username | nick |
    Then the response will be an "Account" domain entity with
    | username     | nick       |
    | creationDate | 2016-01-15T12:00:00 |

Scenario: Update a domain entity - generic
    Given an "Account" domain entity with
    | username     | tom        |
    | creationDate | 2016-01-15T12:00:00 |
    And it has a "update" action
    And it is exposed at "/accounts/testAccount"
    When request is made to "/accounts/testAccount" for a "VanillaEntity"
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


Scenario: Create a domain entity - concrete
    Given an "Accounts" domain entity
    And it has a "createAccount" action
    And it is exposed at "/accounts"
    When request is made to "/accounts" for an "Accounts"
    And the response entity's "createAccount" action is called for an "Account" with
    | username | nick |
    Then the response will be an "Account" domain entity with
    | username     | nick       |
    And it's creation date will be today    

Scenario: Create a domain entity - generic
    Given an "Accounts" domain entity
    And it has a "createAccount" action
    And it is exposed at "/accounts"
    When request is made to "/accounts" for a "VanillaEntity"
    And the response entity's "createAccount" action is called for an "VanillaEntity" with
    | username | nick |
    Then the response will be an "Account" domain entity with
    | username     | nick       |
    And it's creation date will be today    


Scenario: Expose single domain entity with get action
    Given an "Accounts" domain entity
    And it has a "get" action
    And it is exposed at "/accounts"
    When request is made to "/accounts"
    Then the response will be an "Accounts" domain entity
    And it will have a "get" action

Scenario: search for a domain entity
    Given an "Accounts" domain entity
    And it has a "get" action
    And it is exposed at "/accounts"
    And an "Account" domain entity with
    | username     | tom        |
    | creationDate | 2016-01-15T12:00:00 |
    And it is exposed at "/accounts/testAccount"
    And an "Account" domain entity with
    | username     | nick        |
    | creationDate | 2016-01-15T12:00:00 |
    And it is exposed at "/accounts/testAccount2"
    When request is made to "/accounts" for an "Accounts"
    And the response entity's "get" action is called for an "Account" with
    | username | nick |
    Then the response will be an "Account" domain entity with
    | username     | nick       |
    | creationDate | 2016-01-15T12:00:00 |

@skip-local @tom
Scenario: Tries to remotely execute an unexposed method
    Given an "Account" domain entity with
    | username     | tom        |
    | creationDate | 2016-01-15T12:00:00 |
    And it is exposed at "/accounts/testAccount"
    When request is made to "/accounts/testAccount" for an "Account"
    Then calling it's native "localMethod" action should result in a "IllegalAccessException" exception


    
# todo: test with a get action returning a collection
# todo: test with embedded entities (including rel check)
# todo: test that tries to remotely execute an unexposed method
# todo: test that validates the mediatype serializer
# todo: test delete of already deleted entity
# todo: test response header vary
# todo: test message source deserializer
# todo: test exists in repository
# todo: test rest output complies with siren json schema
# todo: test titles & tool tips with translation
# todo: test caching
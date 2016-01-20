package au.com.windyroad.hyperstate.server;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import au.com.windyroad.hyperstate.core.EntityRepository;
import au.com.windyroad.hyperstate.core.Relationship;
import au.com.windyroad.hyperstate.core.Resolver;
import au.com.windyroad.hyperstate.core.entities.EntityWrapper;
import au.com.windyroad.hyperstate.server.entities.Account;
import au.com.windyroad.hyperstate.server.entities.AccountProperties;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@ContextConfiguration(classes = {
        HyperstateTestConfiguration.class }, loader = SpringApplicationContextLoader.class)
@SpringApplicationConfiguration(classes = { HyperstateTestConfiguration.class })
@WebIntegrationTest({ "server.port=0", "management.port=0" })
public class StepDefs {

    public final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Resolver resolver;

    @Autowired
    CloseableHttpAsyncClient httpAsyncClient;

    @Autowired
    HyperstateTestConfiguration config;

    @Autowired
    EntityRepository repository;

    @Autowired
    ApplicationContext context;

    private EntityWrapper<?> currentEntity;

    private AccountBuilder currentAccountBuilder;

    class AccountBuilder {

        private AccountProperties properties;
        private String[] expectedActions;
        private Map<String, String> expectedLinks = new HashMap<String, String>();
        private String path;

        public AccountBuilder(AccountProperties accountProperties) {
            this.properties = accountProperties;
        }

        public void setExpectedActions(String[] actions) {
            this.expectedActions = actions;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public void setExpectedLinkAddress(String rel, String path) {
            this.expectedLinks.put(rel, path);
        }

        public CompletableFuture<Account> build(String path)
                throws InterruptedException, ExecutionException {
            Account entity = new Account(context, repository, path, properties,
                    "The Account");

            String[] actionNames = entity.getActions().stream()
                    .map(a -> a.getLabel()).collect(Collectors.toList())
                    .toArray(new String[] {});
            assertThat(actionNames, equalTo(expectedActions));

            Map<String, String> actualLinks = new HashMap<>();

            entity.getLinks().stream().forEach(nav -> {
                for (String rel : nav.getNature()) {
                    actualLinks.put(rel, nav.getLink().getPath());
                }
            });

            assertThat(actualLinks, equalTo(expectedLinks));
            return repository.save(entity);
        }

    }

    @Given("^an \"([^\"]*)\" domain entity with$")
    public void an_domain_entity_with(String entityName,
            Map<String, String> properties) throws Throwable {
        assertThat(entityName, equalTo("Account"));

        currentAccountBuilder = new AccountBuilder(
                new AccountProperties(properties));
    }

    @Given("^it has no actions$")
    public void it_has_no_actions() throws Throwable {
        currentAccountBuilder.setExpectedActions(new String[] {});
    }

    @Given("^it's only link is self link referencing \"([^\"]*)\"$")
    public void it_s_only_link_is_self_link_referencing(String path)
            throws Throwable {
        currentAccountBuilder.setExpectedLinkAddress(Relationship.SELF, path);
    }

    @Given("^it is exposed at \"([^\"]*)\"$")
    public void it_is_exposed_at(String path) throws Throwable {
        currentAccountBuilder.build(path);
    }

    @When("^request is made to \"([^\"]*)\"$")
    public void request_is_made_to(String path) throws Throwable {
        currentEntity = resolver.get(path, Account.class).get();
    }

    @Then("^the response will be an? \"([^\"]*)\" domain entity with$")
    public void the_response_will_be_an_domain_entity_with(String type,
            Map<String, String> properties) throws Throwable {
        assertThat(currentEntity.getNatures(), hasItem(type));
        throw new PendingException("check properties");
    }

    @Then("^it will have no actions$")
    public void it_will_have_no_actions() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^it will have no links apart from \"([^\"]*)\"$")
    public void it_will_have_no_links_apart_from(String arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^it will have a self link referencing \"([^\"]*)\"$")
    public void it_will_have_a_self_link_referencing(String arg1)
            throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }
}

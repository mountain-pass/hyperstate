package au.com.mountainpass.hyperstate.server;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;

import au.com.mountainpass.hyperstate.core.EntityRelationship;
import au.com.mountainpass.hyperstate.core.EntityRepository;
import au.com.mountainpass.hyperstate.core.Relationship;
import au.com.mountainpass.hyperstate.core.Resolver;
import au.com.mountainpass.hyperstate.core.entities.EntityWrapper;
import au.com.mountainpass.hyperstate.core.entities.VanillaEntity;
import au.com.mountainpass.hyperstate.server.config.HyperstateTestConfiguration;
import au.com.mountainpass.hyperstate.server.entities.Account;
import au.com.mountainpass.hyperstate.server.entities.AccountProperties;
import cucumber.api.PendingException;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@ContextConfiguration(classes = {
        HyperstateTestConfiguration.class }, loader = SpringApplicationContextLoader.class)
@SpringApplicationConfiguration(classes = { HyperstateTestConfiguration.class })
@WebIntegrationTest({ "server.port=0", "management.port=0" })
public class StepDefs {

    class AccountBuilder {

        private String[] expectedActions;
        private final Map<String, String> expectedLinks = new HashMap<String, String>();
        private String path;
        private final AccountProperties properties;

        public AccountBuilder(final AccountProperties accountProperties) {
            this.properties = accountProperties;
        }

        public CompletableFuture<Account> build(final String path)
                throws InterruptedException, ExecutionException {
            final Account entity = new Account(path, properties, "The Account");

            final String[] actionNames = entity.getActions().stream()
                    .map(a -> a.getLabel()).collect(Collectors.toList())
                    .toArray(new String[] {});
            assertThat(actionNames, equalTo(expectedActions));

            final Map<String, String> actualLinks = new HashMap<>();

            entity.getLinks().stream().forEach(nav -> {
                for (final String rel : nav.getNature()) {
                    actualLinks.put(rel, nav.getLink().getPath());
                }
            });

            assertThat(actualLinks, equalTo(expectedLinks));
            return repository.save(entity);
        }

        public void setExpectedActions(final String[] actions) {
            this.expectedActions = actions;
        }

        public void setExpectedLinkAddress(final String rel,
                final String path) {
            this.expectedLinks.put(rel, path);
        }

        public void setPath(final String path) {
            this.path = path;
        }

    }

    @Autowired
    HyperstateTestConfiguration config;

    @Autowired
    ApplicationContext context;

    private HyperstateController controller;

    private AccountBuilder currentAccountBuilder;

    private EntityWrapper<?> currentEntity;

    @Autowired
    CloseableHttpAsyncClient httpAsyncClient;

    public final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    EntityRepository repository;

    @Autowired
    private Resolver resolver;

    @Before
    public void before() {
        URI baseUri = config.getBaseUri();
        resolver.setBaseUri(baseUri);
    }

    @Given("^a Hyperstate controller \"([^\"]*)\" at \"([^\"]*)\"$")
    public void a_Hyperstate_controller_at(final String beanName,
            final String path) throws Throwable {

        controller = context.getAutowireCapableBeanFactory().getBean(beanName,
                HyperstateController.class);
        assertThat(controller, is(notNullValue()));

        // check path
        final RequestMapping requestMapping = AnnotationUtils
                .findAnnotation(controller.getClass(), RequestMapping.class);
        assertThat(requestMapping, is(notNullValue()));
        assertThat(requestMapping.value(), is(arrayContaining(path)));

    }

    @Given("^an \"([^\"]*)\" domain entity with$")
    public void an_domain_entity_with(final String entityName,
            final Map<String, String> properties) throws Throwable {

        assertThat(entityName, equalTo("Account"));
        assertThat(properties.keySet(), contains("username", "creationDate"));

        currentAccountBuilder = new AccountBuilder(new AccountProperties(
                properties.get("username"), properties.get("creationDate")));
    }

    @Given("^it has no actions$")
    public void it_has_no_actions() throws Throwable {
        currentAccountBuilder.setExpectedActions(new String[] {});
    }

    @Given("^it is exposed at \"([^\"]*)\"$")
    public void it_is_exposed_at(final String path) throws Throwable {
        currentAccountBuilder.build(path).get();
    }

    @Given("^it's only link is self link referencing \"([^\"]*)\"$")
    public void it_s_only_link_is_self_link_referencing(final String path)
            throws Throwable {
        currentAccountBuilder.setExpectedLinkAddress(Relationship.SELF, path);
    }

    @Then("^it will have a self link referencing \"([^\"]*)\"$")
    public void it_will_have_a_self_link_referencing(final String path)
            throws Throwable {
        assertThat(currentEntity.getLink(Relationship.SELF).getPath(),
                endsWith(path));
    }

    @Then("^it will have no actions$")
    public void it_will_have_no_actions() throws Throwable {
        assertThat(currentEntity.getActions(), empty());
    }

    @Then("^it will have no links apart from \"([^\"]*)\"$")
    public void it_will_have_no_links_apart_from(final String rel)
            throws Throwable {
        assertThat(currentEntity.getLinks().size(), equalTo(1));
        assertThat(currentEntity.getLinks().asList().get(0).getNature(),
                hasItemInArray(rel));
    }

    @When("^its \"([^\"]*)\" link is followed$")
    public void its_link_is_followed(final String rel) throws Throwable {
        currentEntity = currentEntity.getLink(rel).resolve(VanillaEntity.class);
    }

    @When("^request is made to \"([^\"]*)\"$")
    public void request_is_made_to(final String path) throws Throwable {
        currentEntity = resolver.get(path, VanillaEntity.class).get();
    }

    @When("^request is made to \"([^\"]*)\" for an \"([^\"]*)\"$")
    public void request_is_made_to_for_an(final String path,
            final String typeName) throws Throwable {
        @SuppressWarnings("unchecked")
        final Class<? extends EntityWrapper<?>> type = (Class<? extends EntityWrapper<?>>) Class
                .forName(typeName);
        currentEntity = resolver.get(path, type).get();
    }

    @Given("^the controller's root has an? \"([^\"]*)\" link to an \"([^\"]*)\" domain entity$")
    public void the_controller_s_root_has_an_link_to_an_domain_entity(
            final String rel, final String typeName) throws Throwable {
        controller.getRoot().thenAcceptAsync(root -> {
            root.getEntities().thenAcceptAsync(entities -> {
                final Optional<EntityRelationship> match = entities.stream()
                        .filter(entityRel -> {
                    return entityRel.hasNature(rel);
                }).filter(entityRel -> {
                    return entityRel.getEntity().hasNature(typeName);
                }).findAny();
                assertThat(match.isPresent(), is(equalTo(true)));
            });
        });
    }

    @Then("^the response will be an? \"([^\"]*)\" domain entity$")
    public void the_response_will_be_an_domain_entity(final String type)
            throws Throwable {
        final Set<String> natures = currentEntity.getNatures();
        assertThat(natures, hasItem(type));
    }

    @Then("^the response will be an? \"([^\"]*)\" domain entity with$")
    public void the_response_will_be_an_domain_entity_with(final String type,
            final Map<String, String> properties) throws Throwable {
        the_response_will_be_an_domain_entity(type);

        switch (type) {
        case "Account":
            assertThat(properties.keySet(),
                    contains("username", "creationDate"));
            final AccountProperties entityProperties = (AccountProperties) currentEntity
                    .getProperties();
            assertThat(entityProperties.getUsername(),
                    equalTo(properties.get("username")));
            assertThat(entityProperties.getCreationDate(),
                    equalTo(properties.get("creationDate")));
            break;
        default:
            throw new PendingException("checking properties for a " + type
                    + " has not been coded");
        }
    }

}

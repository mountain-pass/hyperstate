package au.com.mountainpass.hyperstate.server;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.AsyncRestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import au.com.mountainpass.hyperstate.client.RepositoryResolver;
import au.com.mountainpass.hyperstate.client.RestTemplateResolver;
import au.com.mountainpass.hyperstate.client.webdriver.WebDriverResolver;
import au.com.mountainpass.hyperstate.core.Action;
import au.com.mountainpass.hyperstate.core.EntityRelationship;
import au.com.mountainpass.hyperstate.core.EntityRepository;
import au.com.mountainpass.hyperstate.core.Link;
import au.com.mountainpass.hyperstate.core.Relationship;
import au.com.mountainpass.hyperstate.core.Resolver;
import au.com.mountainpass.hyperstate.core.entities.EntityWrapper;
import au.com.mountainpass.hyperstate.core.entities.VanillaEntity;
import au.com.mountainpass.hyperstate.exceptions.EntityNotFoundException;
import au.com.mountainpass.hyperstate.server.config.HyperstateTestConfiguration;
import au.com.mountainpass.hyperstate.server.entities.Account;
import au.com.mountainpass.hyperstate.server.entities.AccountBuilder;
import au.com.mountainpass.hyperstate.server.entities.AccountProperties;
import au.com.mountainpass.hyperstate.server.entities.AccountWithDelete;
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

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AsyncRestTemplate asyncRestTemplate;

    @Autowired
    private ApplicationContext context;

    private HyperstateController controller;

    private AccountBuilder currentAccountBuilder;

    private EntityWrapper<?> currentEntity;

    @Autowired
    private Environment environment;

    @Autowired
    private ObjectMapper om;

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private EntityRepository repository;

    private Resolver resolver;

    @Value("${au.com.mountainpass.hyperstate.test.ssl.hostname}")
    private String sslHostname;

    @Autowired(required = false)
    private WebDriver webDriver;

    @Autowired
    private RepositoryResolver repositoryResovler;

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

        currentAccountBuilder = Account.builder()
                .userName(properties.get("username"))
                .creationDate(properties.get("creationDate"));
    }

    @Before
    public void before() {
        final URI baseUri = getBaseUri();
        final List<String> activeProfiles = Arrays
                .asList(this.environment.getActiveProfiles());
        if (activeProfiles.contains("integration")) {
            resolver = new RestTemplateResolver(baseUri, om, asyncRestTemplate);
        } else if (activeProfiles.contains("ui-integration")) {
            resolver = new WebDriverResolver(baseUri, webDriver);
        } else {
            resolver = repositoryResovler;
        }
    }

    public URI getBaseUri() {
        return URI.create("https://" + sslHostname + ":" + port);
    }

    @Given("^it has no actions$")
    public void it_has_no_actions() throws Throwable {
        // noop
    }

    @Given("^it has no additional links$")
    public void it_has_no_additional_links() throws Throwable {
        // noop
    }

    @Given("^it is exposed at \"([^\"]*)\"$")
    public void it_is_exposed_at(final String path) throws Throwable {
        currentAccountBuilder.build(repositoryResovler, repository, path).get();
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
        assertThat(currentEntity.getLinks().asList().get(0).getRelationships(),
                hasItemInArray(rel));
    }

    @When("^its \"([^\"]*)\" link is followed$")
    public void its_link_is_followed(final String rel) throws Throwable {
        Link link = currentEntity.getLink(rel);
        currentEntity = link.resolve(VanillaEntity.class).get();
    }

    @When("^request is made to \"([^\"]*)\"$")
    public void request_is_made_to(final String path) throws Throwable {
        currentEntity = resolver.get(path, VanillaEntity.class).get();
    }

    @When("^request is made to \"([^\"]*)\" for an \"([^\"]*)\"$")
    public void request_is_made_to_for_an(final String path,
            final String typeName) throws Throwable {
        final org.springframework.web.servlet.resource.ResourceHttpRequestHandler x;
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
                    return entityRel.hasRelationship(rel);
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
        final Set<String> natures = currentEntity.getClasses();
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

    @Given("^it has a \"([^\"]*)\" action$")
    public void it_has_a_action(String actionName) throws Throwable {
        if ("delete".equals(actionName)) {
            currentAccountBuilder.isDeletable(true);
        } else if ("update".equals(actionName)) {
            currentAccountBuilder.isUpdatable(true);
        } else {
            throw new PendingException("TODO");
        }
    }

    @Then("^it will have a \"([^\"]*)\" action$")
    public void it_will_have_a_action(String actionName) throws Throwable {
        Action<?> action = currentEntity.getAction(actionName);
        assertThat(action, notNullValue());
    }

    @When("^the response entity is deleted$")
    public void the_response_entity_is_deleted() throws Throwable {
        assertTrue(currentEntity instanceof AccountWithDelete);
        AccountWithDelete account = (AccountWithDelete) currentEntity;
        account.delete().get();
    }

    @Then("^there will no longer be an entity at \"([^\"]*)\"$")
    public void there_will_no_longer_be_an_entity_at(String path)
            throws Throwable {

        try {
            resolver.get(path, VanillaEntity.class).handle((entity, ee) -> {
                assertThat(entity, nullValue());
                assertThat(ee, notNullValue());
                LOGGER.error("Exception resolving entity", ee.getCause());
                assertThat(ee.getCause(),
                        instanceOf(EntityNotFoundException.class));
                return entity;
                // throw new RuntimeException(ee);
            }).get();
        } catch (ExecutionException ee) {
            throw ee.getCause();
        }
    }

}

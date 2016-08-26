package au.com.mountainpass.hyperstate.core.entities;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.common.collect.ImmutableSet;

import au.com.mountainpass.hyperstate.core.Action;
import au.com.mountainpass.hyperstate.core.Address;
import au.com.mountainpass.hyperstate.core.EntityRelationship;
import au.com.mountainpass.hyperstate.core.EntityRepository;
import au.com.mountainpass.hyperstate.core.JavaAction;
import au.com.mountainpass.hyperstate.core.JavaAddress;
import au.com.mountainpass.hyperstate.core.Link;
import au.com.mountainpass.hyperstate.core.NavigationalRelationship;
import au.com.mountainpass.hyperstate.core.Parameter;
import au.com.mountainpass.hyperstate.core.Relationship;

@JsonPropertyOrder({ "class", "properties", "entities", "actions", "links",
        "title" })
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class EntityWrapper<T> extends Entity {
    private static final int PAGE_SIZE = 10;

    private Map<String, Action<?>> actions = new HashMap<>();

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @JsonProperty("links")
    private Set<NavigationalRelationship> navigationalRelationships = new HashSet<>();

    private String path;

    T properties;

    private EntityRepository repository;

    private Set<EntityRelationship> entityRelationships = new HashSet<>();

    protected EntityWrapper(final EntityRepository repository,
            final String path, final T properties, final String title,
            final String... classes) {
        super(title, classes);
        this.repository = repository;
        this.properties = properties;
        this.path = path;
        add(new NavigationalRelationship(
                new Link(new JavaAddress(repository, this), title),
                Relationship.SELF));
        final Method[] methods = this.getClass().getMethods();
        for (final Method method : methods) {
            final HttpMethod httpMethod = JavaAction
                    .determineMethodNature(method);
            boolean formTypes = Parameter.hasFormTypes(method);
            if (formTypes && httpMethod != null) {
                switch (httpMethod) {
                case DELETE:
                    actions.put(method.getName(), new JavaAction<DeletedEntity>(
                            repository, this, method));
                    break;
                case POST:
                    actions.put(method.getName(), new JavaAction<CreatedEntity>(
                            repository, this, method));
                    break;
                case PUT:
                    actions.put(method.getName(), new JavaAction<UpdatedEntity>(
                            repository, this, method));
                    break;
                case GET:
                    actions.put(method.getName(),
                            new JavaAction<EntityWrapper<?>>(repository, this,
                                    method));
                    break;
                default:
                }
            }
        }
    }

    protected EntityWrapper(@JsonProperty("properties") final T properties) {
        this.properties = properties;
    }

    public void add(final NavigationalRelationship navigationalRelationship) {
        navigationalRelationships.add(navigationalRelationship);
    }

    public Action<?> getAction(final String name) {
        return actions.get(name);
    }

    @JsonProperty("actions")
    public ImmutableSet<Action<?>> getActions() {
        return ImmutableSet.copyOf(actions.values());
    }

    @JsonProperty("entities")
    public ImmutableSet<EntityRelationship> getEntities() {
        return ImmutableSet.copyOf(entityRelationships);
    }

    public Link getLink(final String rel) {
        final Optional<NavigationalRelationship> link = getLinks().stream()
                .filter(l -> l.hasRelationship(rel)).findAny();
        if (link.isPresent()) {
            return link.get().getLink();
        } else {
            return null;
        }
    }

    public ImmutableSet<NavigationalRelationship> getLinks() {
        return ImmutableSet.copyOf(navigationalRelationships);
    }

    public T getProperties() {
        return properties;
    }

    @Override
    public <K, L extends EntityWrapper<K>> L reload(final Class<L> type) {
        return (L) this;
    }

    @Override
    public <L extends EntityWrapper<?>> CompletableFuture<L> resolve(
            final Class<L> type) {
        return CompletableFuture.supplyAsync(() -> {
            return (L) this;
        });

    }

    @Override
    public <K, L extends EntityWrapper<K>> L resolve(
            final ParameterizedTypeReference<L> type) {
        return (L) this;
    }

    public void setActions(final Action<?>[] actions) {
        for (final Action<?> action : actions) {
            this.actions.put(action.getName(), action);
        }

    }

    public void setEntities(final EntityRelationship[] entities) {
        for (final EntityRelationship entity : entities) {
            this.entityRelationships.add(entity);
        }

    }

    public CompletableFuture<EntityWrapper<T>> addEntity(
            final EntityRelationship entityRelationship) {
        this.entityRelationships.add(entityRelationship);
        return repository.save(this);
    }

    @Override
    public LinkedEntity toLinkedEntity() {
        final LinkedEntity linkedEntity = new LinkedEntity(
                getLink(Relationship.SELF), getTitle(), getClasses());
        return linkedEntity;
    }

    @Override
    @JsonIgnore
    public Address getAddress() {
        return new JavaAddress(repository, this);
    }

    @JsonIgnore
    public String getPath() {
        return path;
    }

    @JsonIgnore
    public EntityRepository getRepository() {
        return this.repository;
    }

    @Override
    @JsonIgnore
    public String getId() {
        return path;
    }

}

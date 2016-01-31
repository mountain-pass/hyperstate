package au.com.windyroad.hyperstate.core.entities;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Identifiable;
import org.springframework.http.HttpMethod;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.common.collect.ImmutableSet;

import au.com.windyroad.hyperstate.core.Action;
import au.com.windyroad.hyperstate.core.EntityRelationship;
import au.com.windyroad.hyperstate.core.EntityRepository;
import au.com.windyroad.hyperstate.core.JavaAction;
import au.com.windyroad.hyperstate.core.JavaLink;
import au.com.windyroad.hyperstate.core.Link;
import au.com.windyroad.hyperstate.core.NavigationalRelationship;
import au.com.windyroad.hyperstate.core.Relationship;

@JsonPropertyOrder({ "class", "properties", "entities", "actions", "links",
        "title" })
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class EntityWrapper<T> extends Entity implements Identifiable<String> {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private static final int PAGE_SIZE = 10;

    private Map<String, Action<?>> actions = new HashMap<>();

    private Collection<EntityRelationship> entities = null;

    @JsonProperty("links")
    private Set<NavigationalRelationship> navigationalRelationships = new HashSet<>();

    T properties;

    private EntityRepository repository;

    private String path;

    protected EntityWrapper(@JsonProperty("properties") T properties) {
        this.properties = properties;
    }

    protected EntityWrapper(ApplicationContext context,
            EntityRepository repository, String path, T properties,
            String label) {
        super(label);
        this.properties = properties;
        this.repository = repository;
        this.path = path;
        add(new NavigationalRelationship(new JavaLink(this),
                Relationship.SELF));
        Method[] methods = this.getClass().getMethods();
        for (Method method : methods) {
            HttpMethod httpMethod = JavaAction.determineMethodNature(method);
            if (httpMethod != null) {
                switch (httpMethod) {
                case DELETE:
                    actions.put(method.getName(),
                            new JavaAction<Void>(this, method));
                    break;
                case POST:
                    actions.put(method.getName(),
                            new JavaAction<CreatedEntity>(this, method));
                    break;
                case PUT:
                    actions.put(method.getName(),
                            new JavaAction<UpdatedEntity>(this, method));
                case GET:
                    actions.put(method.getName(),
                            new JavaAction<EntityWrapper<?>>(this, method));
                default:
                }
            }
        }
        getNatures().add(this.getClass().getSimpleName());

        // Label titleAnnotation = properties.getClass()
        // .getAnnotation(Label.class);
        // if (titleAnnotation != null) {
        // setTitle(titleAnnotation.value(), args);
        // }
        repository.save(this);

    }

    public EntityWrapper(EntityWrapper<T> src) {
        super(src);
        this.properties = src.properties;
        this.repository = src.repository;
        this.path = src.path;
        this.actions = src.actions;
        this.entities = src.entities;
        this.navigationalRelationships = src.navigationalRelationships;
    }

    public void add(NavigationalRelationship navigationalRelationship) {
        navigationalRelationships.add(navigationalRelationship);
    }

    public Action<?> getAction(String identifier) {
        return actions.get(identifier);
    }

    @JsonProperty("actions")
    public ImmutableSet<Action<?>> getActions() {
        return ImmutableSet.copyOf(actions.values());
    }

    public CompletableFuture<Collection<EntityRelationship>> getEntities() {
        return getEntities(0);
    }

    @JsonProperty("entities")
    private Collection<EntityRelationship> getEntitiesAndJoin()
            throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, URISyntaxException {
        return getEntities().join();
    }

    public CompletableFuture<Collection<EntityRelationship>> getEntities(
            int page) {
        if (entities != null) {
            return CompletableFuture.supplyAsync(() -> entities);
        }
        if (repository != null) {
            return repository.findChildren(this).thenApplyAsync(results -> {
                return results.skip(page * PAGE_SIZE).limit(PAGE_SIZE)
                        .collect(Collectors.toList());
            });
        }
        final List<EntityRelationship> rval = new ArrayList<>();
        return CompletableFuture.supplyAsync(() -> rval);
    }

    public Link getLink(String rel) {
        Optional<NavigationalRelationship> link = getLinks().stream()
                .filter(l -> l.hasNature(rel)).findAny();
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
    public <K, L extends EntityWrapper<K>> L reload(Class<L> type) {
        return (L) this;
    }

    @Override
    public <K, L extends EntityWrapper<K>> L resolve(Class<L> type) {
        return (L) this;
    }

    @Override
    public <K, L extends EntityWrapper<K>> L resolve(
            ParameterizedTypeReference<L> type) {
        return (L) this;
    }

    public void setActions(Action<?>[] actions) {
        for (Action<?> action : actions) {
            this.actions.put(action.getIdentifier(), action);
        }

    }

    // @Autowired
    // public void setApplicationContext(ApplicationContext context) {
    // this.context = context;
    // AutowiredAnnotationBeanPostProcessor bpp = new
    // AutowiredAnnotationBeanPostProcessor();
    // bpp.setBeanFactory(context.getAutowireCapableBeanFactory());
    // bpp.processInjection(properties);
    // }

    public void setEntities(
            Collection<EntityRelationship> entityRelationships) {
        this.entities = entityRelationships;
    }

    @Override
    public LinkedEntity toLinkedEntity() {
        LinkedEntity linkedEntity = new LinkedEntity(getLink(Relationship.SELF),
                getLabel(), getNatures());
        return linkedEntity;
    }

    // public <L extends EntityWrapper<T>> L refresh() {
    // return (L) getLink(Relationship.SELF).resolve(this.getClass());
    // }

    @Override
    @JsonIgnore
    public URI getAddress() {
        return getLink(Relationship.SELF).getAddress();
    }

    @Override
    @JsonIgnore
    public String getId() {
        return this.path;
    }

}

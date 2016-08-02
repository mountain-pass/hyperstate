package au.com.mountainpass.hyperstate.server;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

import au.com.mountainpass.hyperstate.client.RepositoryResolver;
import au.com.mountainpass.hyperstate.core.Action;
import au.com.mountainpass.hyperstate.core.EntityRepository;
import au.com.mountainpass.hyperstate.core.JavaAddress;
import au.com.mountainpass.hyperstate.core.Link;
import au.com.mountainpass.hyperstate.core.MediaTypes;
import au.com.mountainpass.hyperstate.core.Titled;
import au.com.mountainpass.hyperstate.core.entities.Entity;
import au.com.mountainpass.hyperstate.core.entities.EntityWrapper;
import au.com.mountainpass.hyperstate.server.entities.HyperstateRootEntity;
import au.com.mountainpass.hyperstate.server.serialization.mixins.LinkSerialisationMixin;
import au.com.mountainpass.hyperstate.server.serialization.mixins.TitledSerialisationMixin;

public abstract class HyperstateController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EntityRepository repository;

    @Autowired
    private RepositoryResolver resolver;

    @Autowired
    ObjectMapper objectMapper;

    public HyperstateController() {
    }

    @PostConstruct
    public void postConstructed() {
        objectMapper.addMixIn(Link.class, LinkSerialisationMixin.class);
        objectMapper.addMixIn(Titled.class, TitledSerialisationMixin.class);

        final HyperstateRootEntity root = new HyperstateRootEntity(resolver,
                this.getClass());
        root.setRepository(repository);
        repository.save(root);

        onConstructed(root);
    }

    protected abstract void onConstructed(HyperstateRootEntity root);

    @RequestMapping(value = "**", method = RequestMethod.DELETE, produces = {
            "application/vnd.siren+json",
            "application/json" }, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    @Async
    public CompletableFuture<ResponseEntity<?>> delete(
            final HttpServletRequest request)
                    throws URISyntaxException, NoSuchMethodException,
                    SecurityException, ScriptException, IllegalAccessException,
                    IllegalArgumentException, InvocationTargetException,
                    InterruptedException, ExecutionException {
        final String url = (String) request.getAttribute(
                HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        return getEntity(url).thenApplyAsync(entity -> {
            if (entity == null) {
                return ResponseEntity.noContent().build();
            }
            final Optional<Action<?>> actionOptional = entity.getActions()
                    .stream()
                    .filter(e -> e.getNature().equals(HttpMethod.DELETE))
                    .findAny();

            if (!actionOptional.isPresent()) {
                deleteEntity(entity);
            } else {
                try {
                    final Action<?> action = actionOptional.get();
                    final CompletableFuture<?> invocationResult = action
                            .invoke(new HashMap<>());
                    invocationResult.join();
                } catch (final Exception e) {
                    LOGGER.error(e.getMessage(), e);
                    return ResponseEntity
                            .status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            }
            return ResponseEntity.noContent().build();
        });
    }

    protected CompletableFuture<Void> deleteEntity(
            final EntityWrapper<?> entity) {
        return repository.delete(entity);
    }

    @RequestMapping(value = "**", method = RequestMethod.GET, produces = {
            MediaTypes.SIREN_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @ResponseBody
    @Async
    public CompletableFuture<ResponseEntity<?>> get(
            @RequestParam final Map<String, Object> allRequestParams,
            final HttpServletRequest request) {
        CompletableFuture<EntityWrapper<?>> entityFuture = getEntity(
                allRequestParams, request);
        return entityFuture.thenApplyAsync(entity -> {
            RequestContextHolder.setRequestAttributes(
                    RequestContextHolder.getRequestAttributes());
            if (entity == null) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.ok(entity);
            }
        });
    }

    private CompletableFuture<EntityWrapper<?>> getEntity(
            final Map<String, Object> allRequestParams,
            final HttpServletRequest request) {
        String url = (String) request.getAttribute(
                HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        if (!allRequestParams.isEmpty()) {
            url += "?" + request.getQueryString();
        }
        CompletableFuture<EntityWrapper<?>> entityFuture = getEntity(url);
        return entityFuture;
    }

    protected CompletableFuture<EntityWrapper<?>> getEntity(
            final String identifier) {
        final RequestMapping requestMapping = AnnotationUtils
                .findAnnotation(this.getClass(), RequestMapping.class);
        return repository.findOne(identifier);
    }

    public CompletableFuture<EntityWrapper<?>> getRoot() {
        return getEntity(getRootPath());
    }

    private String getRootPath() {
        final RequestMapping requestMapping = AnnotationUtils
                .findAnnotation(this.getClass(), RequestMapping.class);

        return requestMapping.value()[0];
    }

    // TODO: Help wanted. Ideally we should be able to configure something
    // in spring (the dispatcher) so that if the request is for something
    // that isn't an entity, then it will try the resource handlers.
    // I can't figure out how to do that, so I effectively have a bespoke
    // resource handler here :(
    @RequestMapping(value = "**", method = RequestMethod.GET, produces = {
            MediaType.ALL_VALUE })
    @ResponseBody
    @Async
    public CompletableFuture<ResponseEntity<?>> getResource(
            @RequestParam final Map<String, Object> allRequestParams,
            final HttpServletRequest request) {
        final String path = (String) request.getAttribute(
                HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        CompletableFuture<EntityWrapper<?>> entityFuture = getEntity(
                allRequestParams, request);
        return entityFuture.thenApplyAsync(entity -> {
            RequestContextHolder.setRequestAttributes(
                    RequestContextHolder.getRequestAttributes());
            if (entity == null) {
                return getResource(path);
            } else {
                return getIndex();
            }
        });
    }

    private ResponseEntity<?> getIndex() {
        InputStream inputStream = this.getClass()
                .getResourceAsStream("/static/index.html");

        InputStreamResource inputStreamResource = new InputStreamResource(
                inputStream);
        return ResponseEntity.ok(inputStreamResource);
    }

    private ResponseEntity<?> getResource(String path) {

        InputStream inputStream = this.getClass()
                .getResourceAsStream("/static" + path.toString());
        if (inputStream == null) {
            String webjarPath = "/META-INF/resources" + path;
            inputStream = this.getClass().getResourceAsStream(webjarPath);
        }
        if (inputStream == null) {
            return ResponseEntity.notFound().build();
        } else {
            InputStreamResource inputStreamResource = new InputStreamResource(
                    inputStream);
            return ResponseEntity.ok(inputStreamResource);
        }

    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> onException(final Exception e) {
        LOGGER.error(e.getLocalizedMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @RequestMapping(value = "**", method = RequestMethod.POST, produces = {
            "application/vnd.siren+json",
            "application/json" }, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    public ResponseEntity<?> post(
            @RequestParam final MultiValueMap<String, Object> allRequestParams,
            final HttpServletRequest request)
                    throws URISyntaxException, NoSuchMethodException,
                    SecurityException, ScriptException, IllegalAccessException,
                    IllegalArgumentException, InvocationTargetException,
                    InterruptedException, ExecutionException {
        final String path = (String) request.getAttribute(
                HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        final EntityWrapper<?> entity = getEntity(path).get();
        if (entity == null) {
            return ResponseEntity.notFound().build();
        }

        final Object actionName = allRequestParams.getFirst("action");
        if (actionName == null) {
            // todo add body with classes indicating what is missing
            return ResponseEntity.badRequest().build();
        }
        final Action<?> action = entity.getAction(actionName.toString());
        if (action == null) {
            // todo add body with classes indicating what is missing
            return ResponseEntity.badRequest().build();
        }
        // todo: post actions should have a link return value
        // todo: automatically treat actions that return links as POST actions
        final Entity result = (Entity) action
                .invoke(allRequestParams.toSingleValueMap()).get();

        return ResponseEntity
                .created(new JavaAddress(resolver, entity).getHref())

        .build();
    }

    @RequestMapping(value = "**", method = RequestMethod.PUT, produces = {
            "application/vnd.siren+json",
            "application/json" }, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    public ResponseEntity<?> put(
            @RequestParam final MultiValueMap<String, Object> queryParams,

    final HttpServletRequest request) throws IllegalAccessException,
            IllegalArgumentException, InvocationTargetException,
            URISyntaxException, InterruptedException, ExecutionException {
        final String url = (String) request.getAttribute(
                HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        final EntityWrapper<?> entity = getEntity(url).get();
        if (entity == null) {
            return ResponseEntity.notFound().build();
        }
        final MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.putAll(queryParams);
        final String actionName = (String) queryParams.getFirst("action");
        if (actionName == null) {
            // todo add body with classes indicating what is missing
            return ResponseEntity.badRequest().build();
        }
        final au.com.mountainpass.hyperstate.core.Action<?> action = entity
                .getAction(actionName);
        if (action == null) {
            // todo add body with classes indicating what is missing
            return ResponseEntity.badRequest().build();
        }

        action.invoke(params.toSingleValueMap());
        // todo: automatically treat actions that return void as PUT actions
        return ResponseEntity.noContent()
                .location(new JavaAddress(resolver, entity).getHref()).build();
    }

}

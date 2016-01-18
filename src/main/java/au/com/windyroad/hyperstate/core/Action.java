package au.com.windyroad.hyperstate.core;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpMethod;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Predicates;
import com.google.common.collect.Maps;

/**
 * Actions represent available behaviours an entity exposes.
 * 
 * @param <T>
 *            the type returned by invoking the action
 */
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public abstract class Action<T> extends Labelled {

    private Resolver resolver;

    private String identifier;

    private List<Parameter> parameters = new ArrayList<>();

    private Link link;

    protected Action() {
    }

    public Action(Resolver resolver, String identifier, Link link,
            Parameter... parameters) {
        this.identifier = identifier;
        this.parameters.addAll(Arrays.asList(parameters));
        this.link = link;
        this.resolver = resolver;
    }

    /**
     * @return the identifier
     */
    @JsonProperty("name")
    public String getIdentifier() {
        return identifier;
    }

    /**
     * 
     * @param context
     *            name value pairs representing the parameters the action is
     *            being invoked with
     * @return a future representing the result from a successful invocation of
     *         the action. The invocation may be a network operation, so the
     *         caller should not make assumptions about the timeliness of the
     *         response.
     */
    public CompletableFuture<T> invoke(Map<String, Object> context) {
        Set<String> parameterKeys = getParameterKeys();
        Map<String, Object> filteredParameters = new HashMap<>(
                Maps.filterKeys(context, Predicates.in(parameterKeys)));
        String id = getIdentifier();
        filteredParameters.put("action", id);
        return doInvoke(resolver, filteredParameters);
    }

    abstract protected CompletableFuture<T> doInvoke(Resolver resolver,
            Map<String, Object> filteredParameters);

    /**
     * @return the nature
     */
    @JsonProperty("method")
    public abstract HttpMethod getNature();

    @JsonProperty("href")
    public URI getAddress() {
        if (getLink() != null) {
            return getLink().getAddress();
        } else {
            return null;
        }
    }

    /**
     * @return the parameters
     */
    @JsonProperty("fields")
    public List<Parameter> getParameters() {
        return parameters;
    }

    @JsonIgnore
    public Link getLink() {
        return this.link;
    }

    public Set<String> getParameterKeys() {
        Set<String> rval = new HashSet<>();
        for (Parameter param : getParameters()) {
            rval.add(param.getIdentifier());
        }
        return rval;
    }

}

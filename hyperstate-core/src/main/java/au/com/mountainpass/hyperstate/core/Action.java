package au.com.mountainpass.hyperstate.core;

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
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.google.common.base.Predicates;
import com.google.common.collect.Maps;

import au.com.mountainpass.hyperstate.core.entities.Entity;

/**
 * Actions represent available behaviours an entity exposes.
 *
 * @param <T>
 *            the type returned by invoking the action
 */
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public abstract class Action<T extends Entity> extends Titled {

    private String name;

    private Address address;

    private final List<Parameter> parameters = new ArrayList<>();

    protected Action() {
    }

    public Action(final String name, final Address address,
            final Parameter... parameters) {
        this.name = name;
        this.parameters.addAll(Arrays.asList(parameters));
        this.address = address;
    }

    abstract protected CompletableFuture<T> doInvoke(
            Map<String, Object> filteredParameters);

    /**
     * @return the identifier
     */
    public String getName() {
        return name;
    }

    @JsonUnwrapped
    public Address getAddress() {
        return this.address;
    }

    /**
     * @return the nature
     */
    @JsonProperty("method")
    public abstract HttpMethod getNature();

    @JsonIgnore
    public Set<String> getParameterKeys() {
        final Set<String> rval = new HashSet<>();
        for (final Parameter param : getParameters()) {
            rval.add(param.getIdentifier());
        }
        return rval;
    }

    /**
     * @return the parameters
     */
    @JsonProperty("fields")
    public List<Parameter> getParameters() {
        return parameters;
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
    public CompletableFuture<T> invoke(final Map<String, Object> context) {
        final Set<String> parameterKeys = getParameterKeys();
        final Map<String, Object> filteredParameters = new HashMap<>(
                Maps.filterKeys(context, Predicates.in(parameterKeys)));
        final String id = getName();
        filteredParameters.put("action", id);
        return doInvoke(filteredParameters);
    }

}

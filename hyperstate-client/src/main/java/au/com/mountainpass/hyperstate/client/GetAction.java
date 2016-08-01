package au.com.mountainpass.hyperstate.client;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpMethod;

import au.com.mountainpass.hyperstate.core.Action;
import au.com.mountainpass.hyperstate.core.Link;
import au.com.mountainpass.hyperstate.core.Parameter;
import au.com.mountainpass.hyperstate.core.Resolver;
import au.com.mountainpass.hyperstate.core.entities.EntityWrapper;

public class GetAction extends Action<EntityWrapper<?>> {

    public GetAction(final Resolver resolver, final String identifier,
            final Link link, final Parameter[] fields) {
        super(resolver, identifier, link, fields);
    }

    @Override
    public CompletableFuture<EntityWrapper<?>> doInvoke(final Resolver resolver,
            final Map<String, Object> filteredParameters) {
        return (CompletableFuture) resolver.get(getLink(), filteredParameters,
                EntityWrapper.class);
    }

    @Override
    public HttpMethod getNature() {
        return HttpMethod.GET;
    }

}

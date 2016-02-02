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

    public GetAction(Resolver resolver, String identifier, Link link,
            Parameter[] fields) {
        super(resolver, identifier, link, fields);
    }

    @Override
    public CompletableFuture<EntityWrapper<?>> doInvoke(Resolver resolver,
            Map<String, Object> filteredParameters) {
        return resolver.get(getLink(), filteredParameters);
    }

    @Override
    public HttpMethod getNature() {
        return HttpMethod.GET;
    }

}

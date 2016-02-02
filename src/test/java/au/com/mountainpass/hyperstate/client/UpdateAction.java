package au.com.mountainpass.hyperstate.client;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpMethod;

import au.com.mountainpass.hyperstate.core.Action;
import au.com.mountainpass.hyperstate.core.Link;
import au.com.mountainpass.hyperstate.core.Parameter;
import au.com.mountainpass.hyperstate.core.Resolver;
import au.com.mountainpass.hyperstate.core.entities.UpdatedEntity;

public class UpdateAction extends Action<UpdatedEntity> {

    public UpdateAction(Resolver resolver, String identifier, Link link,
            Parameter[] fields) {
        super(resolver, identifier, link, fields);
    }

    @Override
    public CompletableFuture<UpdatedEntity> doInvoke(Resolver resolver,
            Map<String, Object> filteredParameters) {
        return resolver.update(getLink(), filteredParameters);
    }

    /**
     * @return the nature
     */
    @Override
    public HttpMethod getNature() {
        return HttpMethod.PUT;
    }

}

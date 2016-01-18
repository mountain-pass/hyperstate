package au.com.windyroad.hyperstate.client;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpMethod;

import au.com.windyroad.hyperstate.core.Action;
import au.com.windyroad.hyperstate.core.Link;
import au.com.windyroad.hyperstate.core.Parameter;
import au.com.windyroad.hyperstate.core.Resolver;
import au.com.windyroad.hyperstate.core.entities.CreatedEntity;

public class CreateAction extends Action<CreatedEntity> {

    public CreateAction(Resolver resolver, String identifier, Link link,
            Parameter[] fields) {
        super(resolver, identifier, link, fields);
    }

    @Override
    public CompletableFuture<CreatedEntity> doInvoke(Resolver resolver,
            Map<String, Object> filteredParameters) {
        return resolver.create(getLink(), filteredParameters);
    }

    /**
     * @return the nature
     */
    @Override
    public HttpMethod getNature() {
        return HttpMethod.POST;
    }

}

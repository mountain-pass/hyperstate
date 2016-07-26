package au.com.mountainpass.hyperstate.core;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import au.com.mountainpass.hyperstate.core.entities.CreatedEntity;
import au.com.mountainpass.hyperstate.core.entities.EntityWrapper;
import au.com.mountainpass.hyperstate.core.entities.UpdatedEntity;

public interface Resolver {

    public CompletableFuture<CreatedEntity> create(Link link,
            Map<String, Object> filteredParameters);

    public CompletableFuture<Void> delete(Link link,
            Map<String, Object> filteredParameters);

    public CompletableFuture<EntityWrapper<?>> get(Link link,
            Map<String, Object> filteredParameters);

    public <E extends EntityWrapper<?>> CompletableFuture<E> get(String path,
            Class<E> type) throws URISyntaxException;

    public void setBaseUri(URI baseUri);

    public CompletableFuture<UpdatedEntity> update(Link link,
            Map<String, Object> filteredParameters);

}

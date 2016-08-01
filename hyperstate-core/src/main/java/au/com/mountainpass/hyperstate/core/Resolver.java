package au.com.mountainpass.hyperstate.core;

import java.util.concurrent.CompletableFuture;

import au.com.mountainpass.hyperstate.core.entities.EntityWrapper;

public interface Resolver {

    // public CompletableFuture<CreatedEntity> create(Link link,
    // Map<String, Object> parameters);
    //
    // public CompletableFuture<Void> delete(Link link,
    // Map<String, Object> parameters);
    //
    // public <T> CompletableFuture<T> get(Link link, Class<T> type);
    //
    // public <T> CompletableFuture<T> get(Link link,
    // ParameterizedTypeReference<T> type);
    //
    // public <T> CompletableFuture<T> get(Link link,
    // Map<String, Object> parameters, Class<T> type);
    //
    public <E extends EntityWrapper<?>> CompletableFuture<E> get(String path,
            Class<E> type);
    //
    // public CompletableFuture<UpdatedEntity> update(Link link,
    // Map<String, Object> parameters);

}

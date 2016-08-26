package au.com.mountainpass.hyperstate.core;

import java.util.concurrent.CompletableFuture;

import au.com.mountainpass.hyperstate.core.entities.EntityWrapper;

public interface Resolver {

    public <E extends EntityWrapper<?>> CompletableFuture<E> get(String path,
            Class<E> type);

}

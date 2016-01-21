package au.com.windyroad.hyperstate.core;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import org.springframework.scheduling.annotation.Async;

import au.com.windyroad.hyperstate.core.entities.EntityWrapper;

public interface EntityRepository {

    @Async
    public <S extends EntityWrapper<?>> CompletableFuture<Stream<EntityRelationship>> findChildren(
            S entityWrapper);

    @Async
    public <S extends EntityWrapper<?>> CompletableFuture<S> findOne(
            String path, Class<S> type);

    @Async
    public <S extends EntityWrapper<?>> CompletableFuture<S> save(S entity);

    @Async
    public CompletableFuture<EntityWrapper<?>> findOne(String identifier);

    @Async
    public CompletableFuture<Void> delete(EntityWrapper<?> entity);

}

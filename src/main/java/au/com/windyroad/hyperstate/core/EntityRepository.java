package au.com.windyroad.hyperstate.core;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import org.springframework.scheduling.annotation.Async;

import au.com.windyroad.hyperstate.core.entities.EntityWrapper;

public interface EntityRepository {

    @Async
    CompletableFuture<Stream<EntityRelationship>> findChildren(
            EntityWrapper<?> entityWrapper);

    @Async
    CompletableFuture<EntityWrapper<?>> findOne(String path);

}

package au.com.windyroad.hyperstate.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import au.com.windyroad.hyperstate.core.EntityRelationship;
import au.com.windyroad.hyperstate.core.EntityRepository;
import au.com.windyroad.hyperstate.core.entities.EntityWrapper;

@org.springframework.stereotype.Repository()
public class InMemoryRepository implements EntityRepository {

    Map<String, EntityWrapper<?>> entities = new HashMap<>();
    MultiValueMap<String, EntityRelationship> children = new LinkedMultiValueMap<>();

    Map<String, BiFunction<EntityRepository, EntityWrapper<?>, Stream<EntityRelationship>>> childrenQuery = new HashMap<>();

    @Override
    public <S extends EntityWrapper<?>> CompletableFuture<Stream<EntityRelationship>> findChildren(
            S entityWrapper) {
        BiFunction<EntityRepository, EntityWrapper<?>, Stream<EntityRelationship>> function = childrenQuery
                .get(entityWrapper.getId());
        if (function != null) {
            Stream<EntityRelationship> result = function.apply(this,
                    entityWrapper);
            return CompletableFuture.supplyAsync(() -> result);
        }
        final ArrayList<EntityRelationship> rval = new ArrayList<EntityRelationship>();
        return CompletableFuture.supplyAsync(() -> rval.stream());

    }

    @SuppressWarnings("unchecked")
    @Override
    public <S extends EntityWrapper<?>> CompletableFuture<S> findOne(
            String path, Class<S> type) {
        return (CompletableFuture<S>) findOne(path);
    }

    @Override
    public <S extends EntityWrapper<?>> CompletableFuture<S> save(S entity) {
        entities.put(entity.getId(), entity);
        return CompletableFuture.supplyAsync(() -> entity);
    }

    @Override
    public CompletableFuture<EntityWrapper<?>> findOne(String identifier) {
        return CompletableFuture.supplyAsync(() -> entities.get(identifier));
    }

    @Override
    public CompletableFuture<Void> delete(EntityWrapper<?> entity) {
        return delete(entity.getId());
    }

    @Override
    public CompletableFuture<Void> delete(String id) {
        return CompletableFuture.supplyAsync(() -> {
            entities.remove(id);
            return null;
        });
    }

    @Override
    public CompletableFuture<Boolean> exists(String id) {
        return CompletableFuture.supplyAsync(() -> {
            return entities.containsKey(id);
        });
    }

}

package au.com.mountainpass.hyperstate.server;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Repository;

import au.com.mountainpass.hyperstate.core.EntityRepository;
import au.com.mountainpass.hyperstate.core.entities.DeletedEntity;
import au.com.mountainpass.hyperstate.core.entities.EntityWrapper;

@Repository()
public class InMemoryEntityRepository implements EntityRepository {

    Map<String, EntityWrapper<?>> entities = new HashMap<>();

    @Override
    public CompletableFuture<DeletedEntity> delete(
            final EntityWrapper<?> entity) {
        return delete(entity.getId());
    }

    @Override
    public CompletableFuture<DeletedEntity> delete(final String id) {
        return CompletableFuture.supplyAsync(() -> {
            EntityWrapper<?> removed = entities.remove(id);
            return new DeletedEntity(removed);
        });
    }

    @Override
    public CompletableFuture<Boolean> exists(final String id) {
        return CompletableFuture.supplyAsync(() -> {
            return entities.containsKey(id);
        });
    }

    @Override
    public CompletableFuture<EntityWrapper<?>> findOne(
            final String identifier) {
        return CompletableFuture.supplyAsync(() -> entities.get(identifier));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <S extends EntityWrapper<?>> CompletableFuture<S> findOne(
            final String path, final Class<S> type) {
        return (CompletableFuture<S>) findOne(path);
    }

    @Override
    public <S extends EntityWrapper<?>> CompletableFuture<S> save(
            final S entity) {
        entities.put(entity.getId(), entity);
        return CompletableFuture.supplyAsync(() -> entity);
    }

    @Override
    public CompletableFuture<Void> deleteAll() {
        return CompletableFuture.runAsync(() -> {
            entities.clear();
        });
    }

}

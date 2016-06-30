package au.com.mountainpass.hyperstate.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import au.com.mountainpass.hyperstate.core.EntityRelationship;
import au.com.mountainpass.hyperstate.core.EntityRepository;
import au.com.mountainpass.hyperstate.core.entities.EntityWrapper;

@org.springframework.stereotype.Repository()
public class InMemoryRepository implements EntityRepository {

  MultiValueMap<String, EntityRelationship> children = new LinkedMultiValueMap<>();
  Map<String, BiFunction<EntityRepository, EntityWrapper<?>, Stream<EntityRelationship>>> childrenQuery = new HashMap<>();

  Map<String, EntityWrapper<?>> entities = new HashMap<>();

  @Override
  public CompletableFuture<Void> delete(final EntityWrapper<?> entity) {
    return delete(entity.getId());
  }

  @Override
  public CompletableFuture<Void> delete(final String id) {
    return CompletableFuture.supplyAsync(() -> {
      entities.remove(id);
      return null;
    });
  }

  @Override
  public CompletableFuture<Boolean> exists(final String id) {
    return CompletableFuture.supplyAsync(() -> {
      return entities.containsKey(id);
    });
  }

  @Override
  public <S extends EntityWrapper<?>> CompletableFuture<Stream<EntityRelationship>> findChildren(
      final S entityWrapper) {
    final BiFunction<EntityRepository, EntityWrapper<?>, Stream<EntityRelationship>> function = childrenQuery
        .get(entityWrapper.getId());
    if (function != null) {
      final Stream<EntityRelationship> result = function.apply(this, entityWrapper);
      return CompletableFuture.supplyAsync(() -> result);
    }
    final ArrayList<EntityRelationship> rval = new ArrayList<EntityRelationship>();
    return CompletableFuture.supplyAsync(() -> rval.stream());

  }

  @Override
  public CompletableFuture<EntityWrapper<?>> findOne(final String identifier) {
    return CompletableFuture.supplyAsync(() -> entities.get(identifier));
  }

  @SuppressWarnings("unchecked")
  @Override
  public <S extends EntityWrapper<?>> CompletableFuture<S> findOne(final String path,
      final Class<S> type) {
    return (CompletableFuture<S>) findOne(path);
  }

  @Override
  public <S extends EntityWrapper<?>> CompletableFuture<S> save(final S entity) {
    entities.put(entity.getId(), entity);
    return CompletableFuture.supplyAsync(() -> entity);
  }

}

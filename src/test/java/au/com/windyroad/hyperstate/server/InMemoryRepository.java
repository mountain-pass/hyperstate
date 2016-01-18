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
    public CompletableFuture<Stream<EntityRelationship>> findChildren(
            EntityWrapper<?> entityWrapper) {
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

    @Override
    public CompletableFuture<EntityWrapper<?>> findOne(String path) {
        return CompletableFuture.supplyAsync(() -> entities.get(path));
    }

}

package au.com.windyroad.hyperstate.client;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import au.com.windyroad.hyperstate.core.EntityRepository;
import au.com.windyroad.hyperstate.core.Link;
import au.com.windyroad.hyperstate.core.Resolver;
import au.com.windyroad.hyperstate.core.entities.CreatedEntity;
import au.com.windyroad.hyperstate.core.entities.EntityWrapper;
import au.com.windyroad.hyperstate.core.entities.UpdatedEntity;

@Component
public class RepositoryResolver implements Resolver {

    @Autowired
    EntityRepository repository;

    @Override
    public CompletableFuture<CreatedEntity> create(Link link,
            Map<String, Object> filteredParameters) {
        throw new NotImplementedException("todo");
    }

    @Override
    public CompletableFuture<Void> delete(Link link,
            Map<String, Object> filteredParameters) {
        throw new NotImplementedException("todo");

    }

    @Override
    public CompletableFuture<EntityWrapper<?>> get(Link link,
            Map<String, Object> filteredParameters) {
        throw new NotImplementedException("todo");
    }

    @Override
    public CompletableFuture<UpdatedEntity> update(Link link,
            Map<String, Object> filteredParameters) {
        throw new NotImplementedException("todo");
    }

    @Override
    public <E> CompletableFuture<E> get(String path, Class<E> type) {

        return repository.findOne(path).thenApply(entity -> {
            @SuppressWarnings("unchecked")
            E root = (E) entity;
            return root;
        });
    }

}

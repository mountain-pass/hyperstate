package au.com.mountainpass.hyperstate.client;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.core.ParameterizedTypeReference;

import au.com.mountainpass.hyperstate.core.EntityRepository;
import au.com.mountainpass.hyperstate.core.Link;
import au.com.mountainpass.hyperstate.core.Resolver;
import au.com.mountainpass.hyperstate.core.entities.CreatedEntity;
import au.com.mountainpass.hyperstate.core.entities.EntityWrapper;
import au.com.mountainpass.hyperstate.core.entities.UpdatedEntity;

public class RepositoryResolver implements Resolver {

    private EntityRepository repository;

    public RepositoryResolver(EntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public CompletableFuture<CreatedEntity> create(final Link link,
            final Map<String, Object> filteredParameters) {
        throw new NotImplementedException("todo");
    }

    @Override
    public CompletableFuture<Void> delete(final Link link,
            final Map<String, Object> filteredParameters) {
        throw new NotImplementedException("todo");

    }

    @Override
    public <T> CompletableFuture<T> get(final Link link,
            final Map<String, Object> filteredParameters, Class<T> type) {
        throw new NotImplementedException("todo");
    }

    @Override
    public <E extends EntityWrapper<?>> CompletableFuture<E> get(
            final String path, final Class<E> type) {

        return repository.findOne(path, type);
    }

    @Override
    public CompletableFuture<UpdatedEntity> update(final Link link,
            final Map<String, Object> filteredParameters) {
        throw new NotImplementedException("todo");
    }

    @Override
    public <T> CompletableFuture<T> get(Link link, Class<T> type) {
        return repository.findOne(link.getPath()).thenApply(entity -> {
            return (T) entity;
        });
    }

    @Override
    public <T> CompletableFuture<T> get(Link link,
            ParameterizedTypeReference<T> type) {
        return repository.findOne(link.getPath()).thenApply(entity -> {
            return (T) entity;
        });
    }

}

package au.com.mountainpass.hyperstate.client;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.core.ParameterizedTypeReference;

import au.com.mountainpass.hyperstate.core.EntityRepository;
import au.com.mountainpass.hyperstate.core.JavaAddress;
import au.com.mountainpass.hyperstate.core.Resolver;
import au.com.mountainpass.hyperstate.core.entities.CreatedEntity;
import au.com.mountainpass.hyperstate.core.entities.EntityWrapper;
import au.com.mountainpass.hyperstate.core.entities.UpdatedEntity;

public class RepositoryResolver implements Resolver {

    private EntityRepository repository;

    public RepositoryResolver(EntityRepository repository) {
        this.repository = repository;
    }

    public CompletableFuture<CreatedEntity> create(final JavaAddress address,
            final Map<String, Object> filteredParameters) {
        throw new NotImplementedException("todo");
    }

    public CompletableFuture<Void> delete(final JavaAddress address,
            final Map<String, Object> filteredParameters) {
        throw new NotImplementedException("todo");

    }

    public <T> CompletableFuture<T> get(final JavaAddress address,
            final Map<String, Object> filteredParameters, Class<T> type) {
        throw new NotImplementedException("todo");
    }

    public CompletableFuture<UpdatedEntity> update(final JavaAddress address,
            final Map<String, Object> filteredParameters) {
        throw new NotImplementedException("todo");
    }

    public <T> CompletableFuture<T> get(final JavaAddress address,
            Class<T> type) {
        return repository.findOne(address.getPath()).thenApply(entity -> {
            return (T) entity;
        });
    }

    public <T> CompletableFuture<T> get(final JavaAddress address,
            ParameterizedTypeReference<T> type) {
        return repository.findOne(address.getPath()).thenApply(entity -> {
            return (T) entity;
        });
    }

    public CompletableFuture<EntityWrapper<?>> get(JavaAddress address) {
        throw new NotImplementedException("todo");
    }

    public CompletableFuture<EntityWrapper<?>> get(JavaAddress address,
            Map<String, Object> filteredParameters) {
        throw new NotImplementedException("todo");
    }

    @Override
    public <E extends EntityWrapper<?>> CompletableFuture<E> get(String path,
            Class<E> type) {
        return repository.findOne(path).thenApply(entity -> {
            return (E) entity;
        });
    }

}

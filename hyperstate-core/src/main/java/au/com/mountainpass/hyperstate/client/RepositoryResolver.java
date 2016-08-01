package au.com.mountainpass.hyperstate.client;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.core.ParameterizedTypeReference;

import au.com.mountainpass.hyperstate.core.EntityRepository;
import au.com.mountainpass.hyperstate.core.JavaLink;
import au.com.mountainpass.hyperstate.core.Resolver;
import au.com.mountainpass.hyperstate.core.entities.CreatedEntity;
import au.com.mountainpass.hyperstate.core.entities.EntityWrapper;
import au.com.mountainpass.hyperstate.core.entities.UpdatedEntity;

public class RepositoryResolver implements Resolver {

    private EntityRepository repository;

    public RepositoryResolver(EntityRepository repository) {
        this.repository = repository;
    }

    public CompletableFuture<CreatedEntity> create(final JavaLink link,
            final Map<String, Object> filteredParameters) {
        throw new NotImplementedException("todo");
    }

    public CompletableFuture<Void> delete(final JavaLink link,
            final Map<String, Object> filteredParameters) {
        throw new NotImplementedException("todo");

    }

    public <T> CompletableFuture<T> get(final JavaLink link,
            final Map<String, Object> filteredParameters, Class<T> type) {
        throw new NotImplementedException("todo");
    }

    public CompletableFuture<UpdatedEntity> update(final JavaLink link,
            final Map<String, Object> filteredParameters) {
        throw new NotImplementedException("todo");
    }

    public <T> CompletableFuture<T> get(final JavaLink link, Class<T> type) {
        return repository.findOne(link.getPath()).thenApply(entity -> {
            return (T) entity;
        });
    }

    public <T> CompletableFuture<T> get(final JavaLink link,
            ParameterizedTypeReference<T> type) {
        return repository.findOne(link.getPath()).thenApply(entity -> {
            return (T) entity;
        });
    }

    public CompletableFuture<EntityWrapper<?>> get(JavaLink javaLink) {
        throw new NotImplementedException("todo");
    }

    public CompletableFuture<EntityWrapper<?>> get(JavaLink javaLink,
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

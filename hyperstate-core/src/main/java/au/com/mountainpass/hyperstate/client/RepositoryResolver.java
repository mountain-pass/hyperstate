package au.com.mountainpass.hyperstate.client;

import java.util.concurrent.CompletableFuture;

import org.springframework.core.ParameterizedTypeReference;

import au.com.mountainpass.hyperstate.core.EntityRepository;
import au.com.mountainpass.hyperstate.core.JavaAddress;
import au.com.mountainpass.hyperstate.core.Resolver;
import au.com.mountainpass.hyperstate.core.entities.DeletedEntity;
import au.com.mountainpass.hyperstate.core.entities.EntityWrapper;
import au.com.mountainpass.hyperstate.exceptions.EntityNotFoundException;

public class RepositoryResolver implements Resolver {

    private EntityRepository repository;

    public RepositoryResolver(EntityRepository repository) {
        this.repository = repository;
    }

    public <T> CompletableFuture<T> get(final JavaAddress address,
            Class<T> type) {
        return get(address).thenApply(entity -> {
            return (T) entity;
        });
    }

    public <T> CompletableFuture<T> get(final JavaAddress address,
            ParameterizedTypeReference<T> type) {
        return get(address).thenApply(entity -> {
            return (T) entity;
        });
    }

    public CompletableFuture<EntityWrapper<?>> get(JavaAddress address) {
        return repository.findOne(address.getPath()).thenApply(entity -> {
            if (entity == null) {
                throw new EntityNotFoundException();
            } else {
                return entity;
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E extends EntityWrapper<?>> CompletableFuture<E> get(String path,
            Class<E> type) {
        return repository.findOne(path).thenApply(entity -> {
            if (entity == null) {
                throw new EntityNotFoundException();
            } else {
                return (E) entity;
            }
        });
    }

    public EntityRepository getEntityRepository() {
        return repository;
    }

    public CompletableFuture<DeletedEntity> delete(EntityWrapper<?> entity) {
        return repository.delete(entity);
    }

}

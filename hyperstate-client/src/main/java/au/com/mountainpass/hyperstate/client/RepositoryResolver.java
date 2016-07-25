package au.com.mountainpass.hyperstate.client;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import au.com.mountainpass.hyperstate.core.EntityRepository;
import au.com.mountainpass.hyperstate.core.Link;
import au.com.mountainpass.hyperstate.core.Resolver;
import au.com.mountainpass.hyperstate.core.entities.CreatedEntity;
import au.com.mountainpass.hyperstate.core.entities.EntityWrapper;
import au.com.mountainpass.hyperstate.core.entities.UpdatedEntity;

@Component
public class RepositoryResolver implements Resolver {

    @Autowired
    EntityRepository repository;

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
    public CompletableFuture<EntityWrapper<?>> get(final Link link,
            final Map<String, Object> filteredParameters) {
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
    public void setBaseUri(URI baseUri) {
        // noop
    }

}

package au.com.mountainpass.hyperstate.client;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.core.ParameterizedTypeReference;

import au.com.mountainpass.hyperstate.core.Address;
import au.com.mountainpass.hyperstate.core.entities.CreatedEntity;
import au.com.mountainpass.hyperstate.core.entities.EntityWrapper;
import au.com.mountainpass.hyperstate.core.entities.UpdatedEntity;

public class RestAddress implements Address {

    private URI href;
    private RestTemplateResolver resolver;

    public RestAddress(RestTemplateResolver resolver, URI href) {
        this.resolver = resolver;
        this.href = href;
    }

    @Override
    public <T> CompletableFuture<T> resolve(Class<T> type) {
        throw new NotImplementedException("TODO");
    }

    @Override
    public <T> CompletableFuture<T> resolve(
            ParameterizedTypeReference<T> type) {
        throw new NotImplementedException("TODO");
    }

    @Override
    public String getPath() {
        return href.toString();
    }

    @Override
    public CompletableFuture<EntityWrapper<?>> get(
            Map<String, Object> filteredParameters) {
        throw new NotImplementedException("TODO");
    }

    @Override
    public CompletableFuture<Void> delete(
            Map<String, Object> filteredParameters) {
        return resolver.delete(this, filteredParameters);
    }

    @Override
    public CompletableFuture<CreatedEntity> create(
            Map<String, Object> filteredParameters) {
        throw new NotImplementedException("TODO");
    }

    @Override
    public CompletableFuture<UpdatedEntity> update(
            Map<String, Object> filteredParameters) {
        return resolver.update(this, filteredParameters);
    }

    @Override
    public CompletableFuture<EntityWrapper<?>> get() {
        return resolver.get(this);
    }

    @Override
    public <T extends EntityWrapper<?>> CompletableFuture<T> get(
            Class<T> type) {
        return resolver.get(this, type);
    }

    @Override
    public <T extends EntityWrapper<?>> CompletableFuture<T> get(
            ParameterizedTypeReference<T> type) {
        throw new NotImplementedException("TODO");
    }

    public URI getHref() {
        return href;
    }

}

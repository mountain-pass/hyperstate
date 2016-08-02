package au.com.mountainpass.hyperstate.client;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.core.ParameterizedTypeReference;

import au.com.mountainpass.hyperstate.core.Address;
import au.com.mountainpass.hyperstate.core.entities.CreatedEntity;
import au.com.mountainpass.hyperstate.core.entities.EntityWrapper;
import au.com.mountainpass.hyperstate.core.entities.UpdatedEntity;

public class RestAddress implements Address {

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
        throw new NotImplementedException("TODO");
    }

    @Override
    public CompletableFuture<EntityWrapper<?>> get(
            Map<String, Object> filteredParameters) {
        throw new NotImplementedException("TODO");
    }

    @Override
    public CompletableFuture<Void> delete(
            Map<String, Object> filteredParameters) {
        throw new NotImplementedException("TODO");
    }

    @Override
    public CompletableFuture<CreatedEntity> create(
            Map<String, Object> filteredParameters) {
        throw new NotImplementedException("TODO");
    }

    @Override
    public CompletableFuture<UpdatedEntity> update(
            Map<String, Object> filteredParameters) {
        throw new NotImplementedException("TODO");
    }

    @Override
    public CompletableFuture<EntityWrapper<?>> get() {
        throw new NotImplementedException("TODO");
    }

}

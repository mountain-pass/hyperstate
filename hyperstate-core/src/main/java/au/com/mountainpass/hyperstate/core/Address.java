package au.com.mountainpass.hyperstate.core;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.core.ParameterizedTypeReference;

import com.fasterxml.jackson.annotation.JsonIgnore;

import au.com.mountainpass.hyperstate.core.entities.CreatedEntity;
import au.com.mountainpass.hyperstate.core.entities.EntityWrapper;
import au.com.mountainpass.hyperstate.core.entities.UpdatedEntity;

public interface Address {

    public <T> CompletableFuture<T> resolve(Class<T> type);

    public <T> CompletableFuture<T> resolve(ParameterizedTypeReference<T> type);

    @JsonIgnore
    public String getPath();

    public CompletableFuture<EntityWrapper<?>> get(
            Map<String, Object> filteredParameters);

    public CompletableFuture<Void> delete(
            Map<String, Object> filteredParameters);

    public CompletableFuture<CreatedEntity> create(
            Map<String, Object> filteredParameters);

    public CompletableFuture<UpdatedEntity> update(
            Map<String, Object> filteredParameters);

    public CompletableFuture<EntityWrapper<?>> get();
}

package au.com.mountainpass.hyperstate.core;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.mvc.BasicLinkBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

import au.com.mountainpass.hyperstate.core.entities.CreatedEntity;
import au.com.mountainpass.hyperstate.core.entities.DeletedEntity;
import au.com.mountainpass.hyperstate.core.entities.EntityWrapper;
import au.com.mountainpass.hyperstate.core.entities.UpdatedEntity;
import au.com.mountainpass.hyperstate.exceptions.EntityNotFoundException;

public class JavaAddress implements Address {

    private EntityRepository repository;

    private String path;

    public JavaAddress(EntityRepository repository, EntityWrapper<?> entity) {
        this.repository = repository;
        this.path = entity.getId();
    }

    @Override
    @JsonProperty("href")
    public URI getHref() {
        if (path == null) {
            return null;
        }
        BasicLinkBuilder linkToCurrentMapping = BasicLinkBuilder
                .linkToCurrentMapping();
        URI uri = linkToCurrentMapping.toUri();
        if ("/".equals(path)) {
            return URI.create(uri.toString() + "/");
        } else {
            return uri.resolve(path);
        }
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
        return path;
    }

    @Override
    public CompletableFuture<EntityWrapper<?>> get(
            Map<String, Object> filteredParameters) {
        throw new NotImplementedException("TODO");
    }

    @Override
    public CompletableFuture<DeletedEntity> delete(
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
        return repository.findOne(this.getPath()).thenApply(entity -> {
            if (entity == null) {
                throw new EntityNotFoundException();
            } else {
                return entity;
            }
        });
    }

    @Override
    public <T extends EntityWrapper<?>> CompletableFuture<T> get(
            Class<T> type) {
        return repository.findOne(this.getPath()).thenApply(entity -> {
            if (entity == null) {
                throw new EntityNotFoundException();
            } else {
                return (T) entity;
            }
        });
    }

    @Override
    public <T extends EntityWrapper<?>> CompletableFuture<T> get(
            ParameterizedTypeReference<T> type) {
        throw new NotImplementedException("TODO");
    }

}

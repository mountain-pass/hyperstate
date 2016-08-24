package au.com.mountainpass.hyperstate.core;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.mvc.BasicLinkBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

import au.com.mountainpass.hyperstate.client.RepositoryResolver;
import au.com.mountainpass.hyperstate.core.entities.CreatedEntity;
import au.com.mountainpass.hyperstate.core.entities.DeletedEntity;
import au.com.mountainpass.hyperstate.core.entities.EntityWrapper;
import au.com.mountainpass.hyperstate.core.entities.UpdatedEntity;

public class JavaAddress implements Address {

    private RepositoryResolver resolver;

    // TODO: use an entity path, here rather than the entity itself
    private EntityWrapper<?> entity;

    public JavaAddress(RepositoryResolver resolver, EntityWrapper<?> entity) {
        this.resolver = resolver;
        this.entity = entity;
    }

    @Override
    @JsonProperty("href")
    public URI getHref() {
        if (entity == null) {
            return null;
        }
        BasicLinkBuilder linkToCurrentMapping = BasicLinkBuilder
                .linkToCurrentMapping();
        BasicLinkBuilder slash = linkToCurrentMapping.slash(entity);
        URI uri = slash.toUri();
        if ("/".equals(entity.getId())) {
            return URI.create(uri.toString() + "/");
        }
        return uri;
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
        return entity.getPath();
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

}

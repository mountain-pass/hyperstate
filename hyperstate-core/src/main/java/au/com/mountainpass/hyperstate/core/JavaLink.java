package au.com.mountainpass.hyperstate.core;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.mvc.BasicLinkBuilder;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;

import au.com.mountainpass.hyperstate.client.RepositoryResolver;
import au.com.mountainpass.hyperstate.core.entities.CreatedEntity;
import au.com.mountainpass.hyperstate.core.entities.EntityWrapper;
import au.com.mountainpass.hyperstate.core.entities.UpdatedEntity;

public class JavaLink extends Link {

    private final EntityWrapper<?> entity;

    private RepositoryResolver resolver;

    public JavaLink(RepositoryResolver resolver,
            final EntityWrapper<?> entity) {
        super();
        this.resolver = resolver;
        this.entity = entity;
        assert(entity != null);
    }

    public JavaLink(@JacksonInject RepositoryResolver resolver,
            final EntityWrapper<?> entity,
            @JsonProperty("title") final String label,
            @JsonProperty("class") final String... natures) {
        super(label, natures);
        this.resolver = resolver;
        this.entity = entity;
        assert(entity != null);
    }

    @JsonProperty("href")
    public URI getAddress() {
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
    public MediaType getRepresentationFormat() {
        return MediaTypes.SIREN_JSON;
    }

    @Override
    public String getPath() {
        return entity.getId();
    }

    @Override
    public <T> CompletableFuture<T> resolve(Class<T> type) {
        return resolver.get(this, type);
    }

    @Override
    public <T> CompletableFuture<T> resolve(
            ParameterizedTypeReference<T> type) {
        return resolver.get(this, type);
    }

    @Override
    public CompletableFuture<EntityWrapper<?>> get() {
        return resolver.get(this);
    }

    @Override
    public CompletableFuture<EntityWrapper<?>> get(
            Map<String, Object> filteredParameters) {
        return resolver.get(this, filteredParameters);
    }

    @Override
    public CompletableFuture<Void> delete(
            Map<String, Object> filteredParameters) {
        return resolver.delete(this, filteredParameters);
    }

    @Override
    public CompletableFuture<CreatedEntity> create(
            Map<String, Object> filteredParameters) {
        return resolver.create(this, filteredParameters);
    }

    @Override
    public CompletableFuture<UpdatedEntity> update(
            Map<String, Object> filteredParameters) {
        return resolver.update(this, filteredParameters);
    }
}

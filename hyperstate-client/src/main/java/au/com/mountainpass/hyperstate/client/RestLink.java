package au.com.mountainpass.hyperstate.client;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.NotImplementedException;
import org.eclipse.jdt.annotation.Nullable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import au.com.mountainpass.hyperstate.core.Link;
import au.com.mountainpass.hyperstate.core.MediaTypes;
import au.com.mountainpass.hyperstate.core.entities.CreatedEntity;
import au.com.mountainpass.hyperstate.core.entities.EntityWrapper;
import au.com.mountainpass.hyperstate.core.entities.UpdatedEntity;

public class RestLink extends Link {

    private RestTemplateResolver resolver;

    URI address;

    @Nullable
    MediaType representationFormat = MediaTypes.SIREN_JSON;

    @JsonCreator
    public RestLink(@JacksonInject RestTemplateResolver resolver,
            @JsonProperty("href") final URI address,
            @JsonProperty("title") final String title,
            @JsonProperty("class") final String[] natures) {
        super(title, natures);
        this.resolver = resolver;
        this.address = address;
    }

    @JsonProperty("href")
    public URI getAddress() {
        return address;
    }

    @Override
    public MediaType getRepresentationFormat() {
        return representationFormat == null ? MediaTypes.SIREN_JSON
                : representationFormat;
    }

    public void setAddress(final URI address) {
        this.address = address;
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
    public String getPath() {
        return address.toString();
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

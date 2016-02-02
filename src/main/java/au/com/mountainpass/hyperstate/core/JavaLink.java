package au.com.mountainpass.hyperstate.core;

import java.net.URI;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.mvc.BasicLinkBuilder;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.annotation.JsonProperty;

import au.com.mountainpass.hyperstate.core.entities.EntityWrapper;

public class JavaLink extends Link {

    private EntityWrapper<?> entity;

    protected JavaLink() {
    }

    public JavaLink(EntityWrapper<?> entity) {
        this.entity = entity;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T resolve(Class<T> type) {
        return (T) entity;
    }

    @Override
    public MediaType getRepresentationFormat() {
        return MediaTypes.SIREN_JSON;
    }

    @Override
    @JsonProperty("href")
    public URI getAddress() {
        return BasicLinkBuilder.linkToCurrentMapping().slash(entity).toUri();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T resolve(ParameterizedTypeReference<T> type) {
        return (T) entity;
    }

    @Override
    public String getPath() {
        return entity.getId();
    }
}

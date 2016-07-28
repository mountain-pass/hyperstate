package au.com.mountainpass.hyperstate.core;

import java.net.URI;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.mvc.BasicLinkBuilder;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.annotation.JsonProperty;

import au.com.mountainpass.hyperstate.core.entities.EntityWrapper;

public class JavaLink extends Link {

    final private EntityWrapper<?> entity;

    public JavaLink(final EntityWrapper<?> entity) {
        this.entity = entity;
        assert(entity != null);
    }

    @Override
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
    public String getPath() {
        return entity.getId();
    }

    @Override
    public MediaType getRepresentationFormat() {
        return MediaTypes.SIREN_JSON;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T resolve(final Class<T> type) {
        return (T) entity;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T resolve(final ParameterizedTypeReference<T> type) {
        return (T) entity;
    }
}

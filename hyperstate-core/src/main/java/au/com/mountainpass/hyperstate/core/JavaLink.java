package au.com.mountainpass.hyperstate.core;

import java.net.URI;

import org.springframework.hateoas.mvc.BasicLinkBuilder;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;

import au.com.mountainpass.hyperstate.core.entities.EntityWrapper;

public class JavaLink extends Link {

    private final EntityWrapper<?> entity;

    public JavaLink(@JacksonInject Resolver resolver,
            final EntityWrapper<?> entity) {
        super(resolver, entity.getId());
        this.entity = entity;
        assert(entity != null);
    }

    public JavaLink(@JacksonInject Resolver resolver,
            final EntityWrapper<?> entity,
            @JsonProperty("title") final String label,
            @JsonProperty("class") final String... natures) {
        super(resolver, entity.getId(), label, natures);
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

    // @SuppressWarnings("unchecked")
    // @Override
    // public <T> T resolve(final Class<T> type) {
    // return (T) entity;
    // }
    //
    // @SuppressWarnings("unchecked")
    // @Override
    // public <T> T resolve(final ParameterizedTypeReference<T> type) {
    // return (T) entity;
    // }
}

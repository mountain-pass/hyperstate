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

  public JavaLink(final EntityWrapper<?> entity) {
    this.entity = entity;
  }

  @Override
  @JsonProperty("href")
  public URI getAddress() {
    return BasicLinkBuilder.linkToCurrentMapping().slash(entity).toUri();
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

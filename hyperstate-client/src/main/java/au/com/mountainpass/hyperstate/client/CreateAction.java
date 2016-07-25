package au.com.mountainpass.hyperstate.client;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpMethod;

import au.com.mountainpass.hyperstate.core.Action;
import au.com.mountainpass.hyperstate.core.Link;
import au.com.mountainpass.hyperstate.core.Parameter;
import au.com.mountainpass.hyperstate.core.Resolver;
import au.com.mountainpass.hyperstate.core.entities.CreatedEntity;

public class CreateAction extends Action<CreatedEntity> {

  public CreateAction(final Resolver resolver, final String identifier, final Link link,
      final Parameter[] fields) {
    super(resolver, identifier, link, fields);
  }

  @Override
  public CompletableFuture<CreatedEntity> doInvoke(final Resolver resolver,
      final Map<String, Object> filteredParameters) {
    return resolver.create(getLink(), filteredParameters);
  }

  /**
   * @return the nature
   */
  @Override
  public HttpMethod getNature() {
    return HttpMethod.POST;
  }

}

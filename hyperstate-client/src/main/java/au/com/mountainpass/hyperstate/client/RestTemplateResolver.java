package au.com.mountainpass.hyperstate.client;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;

import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;

import au.com.mountainpass.hyperstate.client.deserialisation.mixins.ActionMixin;
import au.com.mountainpass.hyperstate.client.deserialisation.mixins.EntityRelationshipMixin;
import au.com.mountainpass.hyperstate.client.deserialisation.mixins.LinkMixin;
import au.com.mountainpass.hyperstate.client.deserialisation.mixins.NavigationalRelationshipMixin;
import au.com.mountainpass.hyperstate.core.Action;
import au.com.mountainpass.hyperstate.core.EntityRelationship;
import au.com.mountainpass.hyperstate.core.FutureConverter;
import au.com.mountainpass.hyperstate.core.Address;
import au.com.mountainpass.hyperstate.core.MediaTypes;
import au.com.mountainpass.hyperstate.core.NavigationalRelationship;
import au.com.mountainpass.hyperstate.core.Resolver;
import au.com.mountainpass.hyperstate.core.entities.CreatedEntity;
import au.com.mountainpass.hyperstate.core.entities.EntityWrapper;
import au.com.mountainpass.hyperstate.core.entities.UpdatedEntity;

public class RestTemplateResolver implements Resolver {

    private URI baseUri;

    private AsyncRestTemplate asyncRestTemplate;

    public RestTemplateResolver(URI baseUri, ObjectMapper om,
            AsyncRestTemplate asyncRestTemplate) {
        this.baseUri = baseUri;
        this.asyncRestTemplate = asyncRestTemplate;
        om.addMixIn(Action.class, ActionMixin.class);
        om.addMixIn(Address.class, LinkMixin.class);
        om.addMixIn(EntityRelationship.class, EntityRelationshipMixin.class);
        om.addMixIn(NavigationalRelationship.class,
                NavigationalRelationshipMixin.class);
        om.setInjectableValues(new InjectableValues.Std()
                .addValue(RestTemplateResolver.class, this));
    }

    public CompletableFuture<CreatedEntity> create(final RestLink link,
            final Map<String, Object> filteredParameters) {
        final MultiValueMap<String, Object> body = new LinkedMultiValueMap<>(
                filteredParameters.size());
        for (final Entry<String, Object> entry : filteredParameters
                .entrySet()) {
            body.add(entry.getKey(), entry.getValue());
        }
        final RequestEntity<?> request = RequestEntity.post(link.getAddress())
                .accept(MediaTypes.SIREN_JSON)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED).body(body);
        final ListenableFuture<URI> locationFuture = asyncRestTemplate
                .postForLocation(link.getAddress(), request);
        return FutureConverter.convert(locationFuture).thenApplyAsync(uri -> {
            final CreatedEntity linkedEntity = new CreatedEntity(
                    new RestLink(this, uri, null, null));

            return linkedEntity;
        });
    }

    public CompletableFuture<Void> delete(final RestLink link,
            final Map<String, Object> filteredParameters) {
        final MultiValueMap<String, Object> body = new LinkedMultiValueMap<>(
                filteredParameters.size());
        for (final Entry<String, Object> entry : filteredParameters
                .entrySet()) {
            body.add(entry.getKey(), entry.getValue());
        }
        final RequestEntity<?> request = RequestEntity.put(link.getAddress())
                .accept(MediaTypes.SIREN_JSON)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED).body(body);

        final ListenableFuture<ResponseEntity<Void>> responseFuture = asyncRestTemplate
                .exchange(link.getAddress(), HttpMethod.DELETE, request,
                        Void.class);
        return FutureConverter.convert(responseFuture)
                .thenApplyAsync(response -> {
                    return response.getBody();
                });
    }

    public <T> CompletableFuture<T> get(final RestLink link,
            final Map<String, Object> filteredParameters, Class<T> type) {
        final MultiValueMap<String, Object> body = new LinkedMultiValueMap<>(
                filteredParameters.size());
        for (final Entry<String, Object> entry : filteredParameters
                .entrySet()) {
            body.add(entry.getKey(), entry.getValue());
        }
        final RequestEntity<?> request = RequestEntity.put(link.getAddress())
                .accept(MediaTypes.SIREN_JSON)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED).body(body);

        @SuppressWarnings("rawtypes")
        final ListenableFuture<ResponseEntity<EntityWrapper>> responseFuture = asyncRestTemplate
                .exchange(link.getAddress(), HttpMethod.GET, request,
                        EntityWrapper.class);
        return (CompletableFuture<T>) FutureConverter.convert(responseFuture)
                .thenApplyAsync(response -> {
                    return response.getBody();
                });

    }

    public <T> CompletableFuture<T> get(RestLink link, Class<T> type) {
        Map<String, Object> filteredParameters = new HashMap<>();
        return get(link, filteredParameters, type);
    }

    @Override
    public <E extends EntityWrapper<?>> CompletableFuture<E> get(
            final String path, final Class<E> type) {
        final URI rootUrl = getBaseUri().resolve(path);

        return FutureConverter.convert(
                asyncRestTemplate.exchange(rootUrl, HttpMethod.GET, null, type))
                .thenApply(r -> {
                    return r.getBody();
                });
    }

    private URI getBaseUri() {
        return baseUri;
    }

    public CompletableFuture<UpdatedEntity> update(final RestLink link,
            final Map<String, Object> filteredParameters) {
        final MultiValueMap<String, Object> body = new LinkedMultiValueMap<>(
                filteredParameters.size());
        for (final Entry<String, Object> entry : filteredParameters
                .entrySet()) {
            body.add(entry.getKey(), entry.getValue());
        }
        final RequestEntity<?> request = RequestEntity.put(link.getAddress())
                .accept(MediaTypes.SIREN_JSON)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED).body(body);

        final ListenableFuture<ResponseEntity<Void>> responseFuture = asyncRestTemplate
                .exchange(link.getAddress(), HttpMethod.PUT, request,
                        Void.class);
        return FutureConverter.convert(responseFuture)
                .thenApplyAsync(response -> new UpdatedEntity(new RestLink(this,
                        response.getHeaders().getLocation(), null, null)));
    }

    public <T> CompletableFuture<T> get(RestLink link,
            ParameterizedTypeReference<T> type) {
        throw new NotImplementedException("TODO");
    }

}

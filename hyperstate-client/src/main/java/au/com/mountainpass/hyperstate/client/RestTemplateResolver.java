package au.com.mountainpass.hyperstate.client;

import java.net.URI;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import au.com.mountainpass.hyperstate.client.deserialisation.ObjectMapperDeserialisationUpdater;
import au.com.mountainpass.hyperstate.core.FutureConverter;
import au.com.mountainpass.hyperstate.core.Link;
import au.com.mountainpass.hyperstate.core.MediaTypes;
import au.com.mountainpass.hyperstate.core.Resolver;
import au.com.mountainpass.hyperstate.core.entities.CreatedEntity;
import au.com.mountainpass.hyperstate.core.entities.EntityWrapper;
import au.com.mountainpass.hyperstate.core.entities.UpdatedEntity;

@Component()
@Profile(value = "integration")
@Primary
public class RestTemplateResolver implements Resolver {

    @Autowired
    private ApplicationContext applicationContext;

    private URI baseUri;

    @Autowired
    private AsyncRestTemplate restTemplate;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ObjectMapperDeserialisationUpdater objectMapperDeserialisationUpdater;

    @PostConstruct
    public void postConstruct() {
        objectMapperDeserialisationUpdater.addMixins(om);
        objectMapperDeserialisationUpdater.addResolver(om, this);
    }

    @Override
    public CompletableFuture<CreatedEntity> create(final Link link,
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
        final ListenableFuture<URI> locationFuture = restTemplate
                .postForLocation(link.getAddress(), request);
        return FutureConverter.convert(locationFuture).thenApplyAsync(uri -> {
            final CreatedEntity linkedEntity = new CreatedEntity(
                    new RestLink(uri));
            final AutowiredAnnotationBeanPostProcessor bpp = new AutowiredAnnotationBeanPostProcessor();
            bpp.setBeanFactory(
                    applicationContext.getAutowireCapableBeanFactory());
            bpp.processInjection(linkedEntity);

            return linkedEntity;
        });
    }

    @Override
    public CompletableFuture<Void> delete(final Link link,
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

        final ListenableFuture<ResponseEntity<Void>> responseFuture = restTemplate
                .exchange(link.getAddress(), HttpMethod.DELETE, request,
                        Void.class);
        return FutureConverter.convert(responseFuture)
                .thenApplyAsync(response -> {
                    return response.getBody();
                });
    }

    @Override
    public CompletableFuture<EntityWrapper<?>> get(final Link link,
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

        @SuppressWarnings("rawtypes")
        final ListenableFuture<ResponseEntity<EntityWrapper>> responseFuture = restTemplate
                .exchange(link.getAddress(), HttpMethod.GET, request,
                        EntityWrapper.class);
        return FutureConverter.convert(responseFuture)
                .thenApplyAsync(response -> {
                    return response.getBody();
                });

    }

    @Override
    public <E extends EntityWrapper<?>> CompletableFuture<E> get(
            final String path, final Class<E> type) {
        final URI rootUrl = getBaseUri().resolve(path);

        return FutureConverter.convert(
                restTemplate.exchange(rootUrl, HttpMethod.GET, null, type))
                .thenApply(r -> {
                    return r.getBody();
                });

    }

    private URI getBaseUri() {
        return baseUri;
    }

    /**
     * @param baseUri
     *            the baseUri to set
     */
    @Override
    public void setBaseUri(URI baseUri) {
        this.baseUri = baseUri;
    }

    @Override
    public CompletableFuture<UpdatedEntity> update(final Link link,
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

        final ListenableFuture<ResponseEntity<Void>> responseFuture = restTemplate
                .exchange(link.getAddress(), HttpMethod.PUT, request,
                        Void.class);
        return FutureConverter.convert(responseFuture)
                .thenApplyAsync(response -> {
                    final UpdatedEntity linkedEntity = new UpdatedEntity(
                            new RestLink(response.getHeaders().getLocation()));
                    final AutowiredAnnotationBeanPostProcessor bpp = new AutowiredAnnotationBeanPostProcessor();
                    bpp.setBeanFactory(
                            applicationContext.getAutowireCapableBeanFactory());
                    bpp.processInjection(linkedEntity);

                    return linkedEntity;
                });
    }

}

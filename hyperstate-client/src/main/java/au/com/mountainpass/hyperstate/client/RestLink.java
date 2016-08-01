package au.com.mountainpass.hyperstate.client;

import java.net.URI;

import org.eclipse.jdt.annotation.Nullable;
import org.springframework.http.MediaType;
import org.springframework.web.client.AsyncRestTemplate;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import au.com.mountainpass.hyperstate.core.Link;
import au.com.mountainpass.hyperstate.core.MediaTypes;
import au.com.mountainpass.hyperstate.core.Resolver;

public class RestLink extends Link {

    private Resolver resolver;

    private AsyncRestTemplate asyncRestTemplate;

    URI address;

    @Nullable
    MediaType representationFormat = MediaTypes.SIREN_JSON;

    @JsonCreator
    public RestLink(@JacksonInject Resolver resolver,
            @JacksonInject AsyncRestTemplate asyncRestTemplate,
            @JsonProperty("href") final URI address,
            @JsonProperty("title") final String label,
            @JsonProperty("class") final String[] natures) {
        super(resolver, address.toString(), label, natures);
        this.resolver = resolver;
        this.asyncRestTemplate = asyncRestTemplate;
        this.address = address;
    }

    @Override
    @JsonProperty("href")
    public URI getAddress() {
        return address;
    }

    @Override
    @JsonIgnore
    public String getPath() {
        return address.toString();
    }

    @Override
    public MediaType getRepresentationFormat() {
        return representationFormat == null ? MediaTypes.SIREN_JSON
                : representationFormat;
    }

    // @Override
    // public <T> T resolve(final Class<T> type)
    // throws InterruptedException, ExecutionException {
    // Map<String, Object> filteredParameters = new HashMap<>();
    // if (this.representationFormat == MediaTypes.SIREN_JSON) {
    // return (T) resolver.get(this, filteredParameters).get();
    // } else {
    // final RequestEntity<Void> request = RequestEntity.get(address)
    // .accept(getRepresentationFormat()).build();
    // ListenableFuture<ResponseEntity<T>> responseFuture = asyncRestTemplate
    // .exchange(address, HttpMethod.GET, request, type);
    // return FutureConverter.convert(responseFuture)
    // .thenApply(response -> {
    // return response.getBody();
    // }).get();
    // }
    // }
    //
    // @Override
    // public <T> T resolve(final ParameterizedTypeReference<T> type)
    // throws InterruptedException, ExecutionException {
    // if (this.representationFormat == MediaTypes.SIREN_JSON) {
    // return (T) resolver.get(this).get();
    // } else {
    // final RequestEntity<Void> request = RequestEntity.get(address)
    // .accept(getRepresentationFormat()).build();
    // ListenableFuture<ResponseEntity<T>> responseFuture = asyncRestTemplate
    // .exchange(address, HttpMethod.GET, request, type);
    // return FutureConverter.convert(responseFuture)
    // .thenApply(response -> {
    // return response.getBody();
    // }).get();
    // }
    // }

    public void setAddress(final URI address) {
        this.address = address;
    }

    // @Autowired
    // public void setRestTemplate(final RestTemplate restTemplate) {
    // this.restTemplate = restTemplate;
    // }

}

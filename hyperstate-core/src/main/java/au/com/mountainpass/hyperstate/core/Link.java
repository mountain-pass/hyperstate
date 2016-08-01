package au.com.mountainpass.hyperstate.core;

import java.net.URI;
import java.util.concurrent.ExecutionException;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
abstract public class Link extends Labelled {

    @JacksonInject
    private Resolver resolver;

    public Link(@JacksonInject Resolver resolver,
            @JsonProperty("href") String path) {
        super();
        this.resolver = resolver;
    }

    public Link(@JacksonInject Resolver resolver,
            @JsonProperty("href") String path,
            @JsonProperty("title") final String label,
            @JsonProperty("class") final String... natures) {
        super(label, natures);
        this.resolver = resolver;
    }

    @JsonProperty("href")
    public abstract URI getAddress();

    @JsonIgnore
    public abstract String getPath();

    @JsonProperty("type")
    public abstract MediaType getRepresentationFormat();

    public <T> T resolve(Class<T> type)
            throws InterruptedException, ExecutionException {
        return (T) resolver.get(this).get();
    }

    public <T> T resolve(ParameterizedTypeReference<T> type)
            throws InterruptedException, ExecutionException {
        return (T) resolver.get(this).get();
    }
}

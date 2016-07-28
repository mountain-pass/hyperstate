package au.com.mountainpass.hyperstate.core;

import java.net.URI;
import java.util.Set;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
abstract public class Link extends Labelled {

    public Link() {
    }

    public Link(final String label) {
        super(label);
    }

    public Link(final String label, final Set<String> natures) {
        super(label, natures);
    }

    @JsonProperty("href")
    public abstract URI getAddress();

    @JsonIgnore
    public abstract String getPath();

    @JsonProperty("type")
    public abstract MediaType getRepresentationFormat();

    public abstract <T> T resolve(Class<T> type);

    public abstract <T> T resolve(ParameterizedTypeReference<T> type);
}

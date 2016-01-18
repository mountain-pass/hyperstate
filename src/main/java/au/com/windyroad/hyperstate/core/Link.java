package au.com.windyroad.hyperstate.core;

import java.net.URI;
import java.util.Set;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
abstract public class Link extends Labelled {

    public Link() {
    }

    public Link(String label, Set<String> natures) {
        super(label, natures);
    }

    public Link(String label) {
        super(label);
    }

    public abstract <T> T resolve(Class<T> type);

    public abstract <T> T resolve(ParameterizedTypeReference<T> type);

    @JsonProperty("type")
    public abstract MediaType getRepresentationFormat();

    @JsonProperty("href")
    public abstract URI getAddress();
}

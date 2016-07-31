package au.com.mountainpass.hyperstate.client.builder;

import java.net.URI;

import org.springframework.web.client.AsyncRestTemplate;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;

import au.com.mountainpass.hyperstate.client.RestLink;
import au.com.mountainpass.hyperstate.core.NavigationalRelationship;
import au.com.mountainpass.hyperstate.core.Resolver;

public class NavigationalRelationshipBuilder {

    private URI address;
    private String label;
    private String[] natures;

    public NavigationalRelationship build() {
        return new NavigationalRelationship(new RestLink(resolver,
                asyncRestTemplte, address, label, natures), natures);
    }

    @JacksonInject
    private Resolver resolver;

    @JacksonInject
    private AsyncRestTemplate asyncRestTemplte;

    @JsonProperty("href")
    public NavigationalRelationshipBuilder setAddress(final URI address) {
        this.address = address;
        return this;
    }

    @JsonProperty("rel")
    public NavigationalRelationshipBuilder setHref(final String[] natures) {
        this.natures = natures;
        return this;
    }

    @JsonProperty("title")
    public NavigationalRelationshipBuilder setLabel(final String label) {
        this.label = label;
        return this;
    }
}

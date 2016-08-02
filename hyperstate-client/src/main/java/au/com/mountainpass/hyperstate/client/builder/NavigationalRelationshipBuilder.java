package au.com.mountainpass.hyperstate.client.builder;

import java.net.URI;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;

import au.com.mountainpass.hyperstate.client.RestLink;
import au.com.mountainpass.hyperstate.client.RestTemplateResolver;
import au.com.mountainpass.hyperstate.core.NavigationalRelationship;

public class NavigationalRelationshipBuilder {

    private URI address;
    private String title;
    private String[] natures;

    public NavigationalRelationship build() {
        return new NavigationalRelationship(
                new RestLink(resolver, address, title, natures), natures);
    }

    @JacksonInject
    private RestTemplateResolver resolver;

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
    public NavigationalRelationshipBuilder setTitle(final String title) {
        this.title = title;
        return this;
    }
}

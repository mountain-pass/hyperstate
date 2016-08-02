package au.com.mountainpass.hyperstate.client.builder;

import java.net.URI;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;

import au.com.mountainpass.hyperstate.client.RestAddress;
import au.com.mountainpass.hyperstate.client.RestTemplateResolver;
import au.com.mountainpass.hyperstate.core.Link;
import au.com.mountainpass.hyperstate.core.NavigationalRelationship;

public class NavigationalRelationshipBuilder {

    private URI href;
    private String title;
    private String[] rels = new String[0];
    private String[] classes = new String[0];

    public NavigationalRelationship build() {
        return new NavigationalRelationship(
                new Link(new RestAddress(resolver, href), title, classes),
                rels);
    }

    @JacksonInject
    private RestTemplateResolver resolver;

    @JsonProperty("href")
    public NavigationalRelationshipBuilder setHref(final URI href) {
        this.href = href;
        return this;
    }

    @JsonProperty("rel")
    public NavigationalRelationshipBuilder setRels(final String[] rels) {
        this.rels = rels;
        return this;
    }

    @JsonProperty("class")
    public NavigationalRelationshipBuilder setClasses(final String[] classes) {
        this.classes = classes;
        return this;
    }

    @JsonProperty("title")
    public NavigationalRelationshipBuilder setTitle(final String title) {
        this.title = title;
        return this;
    }

}

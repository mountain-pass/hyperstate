package au.com.mountainpass.hyperstate.client.builder;

import java.net.URI;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;

import au.com.mountainpass.hyperstate.client.RestAddress;
import au.com.mountainpass.hyperstate.client.RestTemplateResolver;
import au.com.mountainpass.hyperstate.core.EntityRelationship;
import au.com.mountainpass.hyperstate.core.Link;
import au.com.mountainpass.hyperstate.core.entities.LinkedEntity;

public class EntityRelationshipBuilder {

    private URI address;
    private String[] entityClasses;
    private String title;
    private String[] rels;

    @JacksonInject
    private RestTemplateResolver resolver;
    private String type;

    public EntityRelationship build() {
        final LinkedEntity entity = new LinkedEntity(
                new Link(new RestAddress(resolver, address), title), title,
                entityClasses);
        return new EntityRelationship(entity, rels);
    }

    @JsonProperty("href")
    public EntityRelationshipBuilder setAddress(final URI address) {
        this.address = address;
        return this;
    }

    @JsonProperty("class")
    public EntityRelationshipBuilder setClass(final String[] natures) {
        this.entityClasses = natures;
        return this;
    }

    @JsonProperty("title")
    public EntityRelationshipBuilder setTitle(final String title) {
        this.title = title;
        return this;
    }

    @JsonProperty("rel")
    public EntityRelationshipBuilder setRel(final String[] natures) {
        this.rels = natures;
        return this;
    }

    @JsonProperty("type")
    public EntityRelationshipBuilder setType(final String type) {
        this.type = type;
        return this;
    }
}

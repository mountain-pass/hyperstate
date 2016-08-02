package au.com.mountainpass.hyperstate.client.builder;

import java.net.URI;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;

import au.com.mountainpass.hyperstate.client.RestLink;
import au.com.mountainpass.hyperstate.client.RestTemplateResolver;
import au.com.mountainpass.hyperstate.core.EntityRelationship;
import au.com.mountainpass.hyperstate.core.entities.LinkedEntity;

public class EntityRelationshipBuilder {

    private URI address;
    private String[] entityNatures;
    private String title;
    private String[] relationshipNatures;

    @JacksonInject
    private RestTemplateResolver resolver;
    private String type;

    public EntityRelationship build() {
        final LinkedEntity entity = new LinkedEntity(
                new RestLink(resolver, address, title, null), title,
                entityNatures);
        return new EntityRelationship(entity, relationshipNatures);
    }

    @JsonProperty("href")
    public EntityRelationshipBuilder setAddress(final URI address) {
        this.address = address;
        return this;
    }

    @JsonProperty("class")
    public EntityRelationshipBuilder setClass(final String[] natures) {
        this.entityNatures = natures;
        return this;
    }

    @JsonProperty("title")
    public EntityRelationshipBuilder setTitle(final String title) {
        this.title = title;
        return this;
    }

    @JsonProperty("rel")
    public EntityRelationshipBuilder setRel(final String[] natures) {
        this.relationshipNatures = natures;
        return this;
    }

    @JsonProperty("type")
    public EntityRelationshipBuilder setType(final String type) {
        this.type = type;
        return this;
    }
}

package au.com.windyroad.hyperstate.client.builder;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonProperty;

import au.com.windyroad.hyperstate.client.RestLink;
import au.com.windyroad.hyperstate.core.EntityRelationship;

public class EntityRelationshipBuilder {

    private URI address;
    private String label;
    private String[] natures;

    @JsonProperty("href")
    public EntityRelationshipBuilder setAddress(URI address) {
        this.address = address;
        return this;
    }

    @JsonProperty("title")
    public EntityRelationshipBuilder setLabel(String label) {
        this.label = label;
        return this;
    }

    @JsonProperty("rel")
    public EntityRelationshipBuilder setHref(String[] natures) {
        this.natures = natures;
        return this;
    }

    public EntityRelationship build() {
        return new EntityRelationship(new RestLink(address), label, natures);
    }
}

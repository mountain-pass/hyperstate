package au.com.mountainpass.hyperstate.core;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import au.com.mountainpass.hyperstate.core.entities.EntityWrapper;

public class NavigationalRelationship extends Relationship {

    private Link link;

    private NavigationalRelationship() {
    }

    public NavigationalRelationship(final EntityWrapper<?> entity,
            final String... rels) {
        super(rels);
        this.link = entity.toLinkedEntity().getLink();
    }

    public NavigationalRelationship(final Link link, final String... rels) {
        super(rels);
        this.link = link;
    }

    @JsonUnwrapped
    public Link getLink() {
        return this.link;
    }

    public void setLink(final Link link) {
        this.link = link;
    }

}

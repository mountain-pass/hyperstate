package au.com.mountainpass.hyperstate.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import au.com.mountainpass.hyperstate.core.entities.Entity;
import au.com.mountainpass.hyperstate.core.entities.LinkedEntity;

public class EntityRelationship extends Relationship {

    private Entity entity;

    protected EntityRelationship() {
    }

    public EntityRelationship(final Entity entity, final String... rels) {
        super(rels);
        this.entity = entity;
    }

    /**
     * @return the entity
     */
    @JsonIgnore
    public Entity getEntity() {
        return entity;
    }

    @JsonUnwrapped
    public LinkedEntity getEntityLink() {
        return entity.toLinkedEntity();
    }

}

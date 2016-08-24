package au.com.mountainpass.hyperstate.core;

import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import au.com.mountainpass.hyperstate.core.entities.Entity;
import au.com.mountainpass.hyperstate.core.entities.EntityWrapper;
import au.com.mountainpass.hyperstate.core.entities.LinkedEntity;

public class EntityRelationship extends Relationship {

    private Entity toEntity;

    protected EntityRelationship() {
    }

    public EntityRelationship(final Entity toEntity, final String... rels) {
        super(rels);
        this.toEntity = toEntity;
    }

    /**
     * @return the entity
     */
    @JsonIgnore
    public Entity getEntity() {
        return toEntity;
    }

    @JsonUnwrapped
    public LinkedEntity getEntityLink() {
        return toEntity.toLinkedEntity();
    }

    public <K, T extends EntityWrapper<K>> CompletableFuture<T> resolve(
            Class<T> type) {
        return toEntity.resolve(type);
    }

}

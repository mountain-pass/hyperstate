package au.com.windyroad.hyperstate.client.mixins;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import au.com.windyroad.hyperstate.client.builder.EntityRelationshipBuilder;

@JsonDeserialize(builder = EntityRelationshipBuilder.class)
public abstract class EntityRelationshipMixin {

}

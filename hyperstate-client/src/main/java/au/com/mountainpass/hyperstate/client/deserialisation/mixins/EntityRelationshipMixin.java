package au.com.mountainpass.hyperstate.client.deserialisation.mixins;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import au.com.mountainpass.hyperstate.client.builder.EntityRelationshipBuilder;

@JsonDeserialize(builder = EntityRelationshipBuilder.class)
public abstract class EntityRelationshipMixin {

}

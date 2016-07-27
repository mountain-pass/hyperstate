package au.com.mountainpass.hyperstate.client.deserialisation.mixins;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import au.com.mountainpass.hyperstate.client.builder.NavigationalRelationshipBuilder;

@JsonDeserialize(builder = NavigationalRelationshipBuilder.class)
public abstract class NavigationalRelationshipMixin {

}

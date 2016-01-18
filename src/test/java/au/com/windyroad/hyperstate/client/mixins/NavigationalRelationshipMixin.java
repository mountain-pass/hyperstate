package au.com.windyroad.hyperstate.client.mixins;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import au.com.windyroad.hyperstate.client.builder.NavigationalRelationshipBuilder;

@JsonDeserialize(builder = NavigationalRelationshipBuilder.class)
public abstract class NavigationalRelationshipMixin {

}

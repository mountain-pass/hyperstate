package au.com.windyroad.hyperstate.client.mixins;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import au.com.windyroad.hyperstate.client.builder.RestActionBuilder;

@JsonDeserialize(builder = RestActionBuilder.class)
public abstract class ActionMixin {

}

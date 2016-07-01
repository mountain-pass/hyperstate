package au.com.mountainpass.hyperstate.client.mixins;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import au.com.mountainpass.hyperstate.client.builder.RestActionBuilder;

@JsonDeserialize(builder = RestActionBuilder.class)
public abstract class ActionMixin {

}

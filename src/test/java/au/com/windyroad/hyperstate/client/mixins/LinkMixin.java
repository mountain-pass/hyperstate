package au.com.windyroad.hyperstate.client.mixins;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import au.com.windyroad.hyperstate.client.RestLink;

@JsonDeserialize(as = RestLink.class)
public abstract class LinkMixin {

}

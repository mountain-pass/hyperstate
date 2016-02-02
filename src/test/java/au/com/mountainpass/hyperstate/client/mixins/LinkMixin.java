package au.com.mountainpass.hyperstate.client.mixins;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import au.com.mountainpass.hyperstate.client.RestLink;

@JsonDeserialize(as = RestLink.class)
public abstract class LinkMixin {

}

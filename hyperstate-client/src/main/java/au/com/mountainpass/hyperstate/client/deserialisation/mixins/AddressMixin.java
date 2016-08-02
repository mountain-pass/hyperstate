package au.com.mountainpass.hyperstate.client.deserialisation.mixins;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import au.com.mountainpass.hyperstate.client.RestAddress;

@JsonDeserialize(as = RestAddress.class)
public abstract class AddressMixin {

}

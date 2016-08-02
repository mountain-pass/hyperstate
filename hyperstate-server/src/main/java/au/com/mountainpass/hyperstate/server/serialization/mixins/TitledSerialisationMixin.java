package au.com.mountainpass.hyperstate.server.serialization.mixins;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import au.com.mountainpass.hyperstate.server.serialization.MessageSourceAwareSerializer;

public abstract class TitledSerialisationMixin {

    @JsonSerialize(using = MessageSourceAwareSerializer.class)
    @JsonProperty("title")
    public abstract String getTitle();

}

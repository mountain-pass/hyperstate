package au.com.mountainpass.hyperstate.server.serialization.mixins;

import org.springframework.http.MediaType;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import au.com.mountainpass.hyperstate.server.serialization.MediaTypeSerializer;

public abstract class LinkMixin {

    @JsonSerialize(using = MediaTypeSerializer.class)
    @JsonProperty("type")
    public abstract MediaType getRepresentationFormat();

}

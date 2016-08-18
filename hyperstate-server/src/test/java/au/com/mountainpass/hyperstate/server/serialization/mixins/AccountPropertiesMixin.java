package au.com.mountainpass.hyperstate.server.serialization.mixins;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import au.com.mountainpass.hyperstate.server.serialization.LocalDateTimeSerializer;

public abstract class AccountPropertiesMixin {

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    public abstract LocalDateTime getCreationDate();

}

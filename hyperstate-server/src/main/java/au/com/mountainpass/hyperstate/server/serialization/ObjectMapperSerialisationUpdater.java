package au.com.mountainpass.hyperstate.server.serialization;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import au.com.mountainpass.hyperstate.core.Titled;
import au.com.mountainpass.hyperstate.server.serialization.mixins.TitledSerialisationMixin;

@Component
public class ObjectMapperSerialisationUpdater {

    public void addMixins(ObjectMapper om) {
        om.addMixIn(Titled.class, TitledSerialisationMixin.class);
    }

}

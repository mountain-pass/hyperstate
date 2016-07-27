package au.com.mountainpass.hyperstate.server.serialization;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import au.com.mountainpass.hyperstate.core.Labelled;
import au.com.mountainpass.hyperstate.server.mixins.LabelledMixin;

@Component
public class ObjectMapperSerialisationUpdater {

    public void addMixins(ObjectMapper om) {
        om.addMixIn(Labelled.class, LabelledMixin.class);
    }

}

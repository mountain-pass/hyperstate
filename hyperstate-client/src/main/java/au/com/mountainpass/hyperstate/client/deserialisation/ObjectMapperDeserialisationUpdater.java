package au.com.mountainpass.hyperstate.client.deserialisation;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;

import au.com.mountainpass.hyperstate.client.deserialisation.mixins.ActionMixin;
import au.com.mountainpass.hyperstate.client.deserialisation.mixins.EntityRelationshipMixin;
import au.com.mountainpass.hyperstate.client.deserialisation.mixins.LinkMixin;
import au.com.mountainpass.hyperstate.client.deserialisation.mixins.NavigationalRelationshipMixin;
import au.com.mountainpass.hyperstate.core.Action;
import au.com.mountainpass.hyperstate.core.EntityRelationship;
import au.com.mountainpass.hyperstate.core.Link;
import au.com.mountainpass.hyperstate.core.NavigationalRelationship;
import au.com.mountainpass.hyperstate.core.Resolver;

@Component
public class ObjectMapperDeserialisationUpdater {

    public void addMixins(ObjectMapper om) {
        om.addMixIn(Action.class, ActionMixin.class);
        om.addMixIn(Link.class, LinkMixin.class);
        om.addMixIn(EntityRelationship.class, EntityRelationshipMixin.class);
        om.addMixIn(NavigationalRelationship.class,
                NavigationalRelationshipMixin.class);
    }

    public void addResolver(ObjectMapper om, Resolver resolver) {
        om.setInjectableValues(
                new InjectableValues.Std().addValue(Resolver.class, resolver));
    }

}

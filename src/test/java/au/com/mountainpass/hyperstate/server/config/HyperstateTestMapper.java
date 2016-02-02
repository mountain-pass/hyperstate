package au.com.mountainpass.hyperstate.server.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;

import au.com.mountainpass.hyperstate.client.RestTemplateResolver;
import au.com.mountainpass.hyperstate.client.mixins.ActionMixin;
import au.com.mountainpass.hyperstate.client.mixins.EntityRelationshipMixin;
import au.com.mountainpass.hyperstate.client.mixins.LinkMixin;
import au.com.mountainpass.hyperstate.client.mixins.NavigationalRelationshipMixin;
import au.com.mountainpass.hyperstate.core.Action;
import au.com.mountainpass.hyperstate.core.EntityRelationship;
import au.com.mountainpass.hyperstate.core.Link;
import au.com.mountainpass.hyperstate.core.NavigationalRelationship;
import au.com.mountainpass.hyperstate.core.Resolver;

@Configuration
@Profile(value = "integration")
public class HyperstateTestMapper {

    @Autowired
    RestTemplateResolver restTemplateResolver;

    @Autowired
    ApplicationContext context;

    @PostConstruct
    public void fixOm() {
        ObjectMapper om = context.getBean(ObjectMapper.class);
        om.addMixIn(Action.class, ActionMixin.class);
        om.addMixIn(Link.class, LinkMixin.class);
        om.addMixIn(EntityRelationship.class, EntityRelationshipMixin.class);
        om.addMixIn(NavigationalRelationship.class,
                NavigationalRelationshipMixin.class);
        om.setInjectableValues(new InjectableValues.Std()
                .addValue(Resolver.class, restTemplateResolver));
    }
}

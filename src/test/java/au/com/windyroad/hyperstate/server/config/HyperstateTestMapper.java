package au.com.windyroad.hyperstate.server.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;

import au.com.windyroad.hyperstate.client.RestTemplateResolver;
import au.com.windyroad.hyperstate.client.mixins.ActionMixin;
import au.com.windyroad.hyperstate.client.mixins.EntityRelationshipMixin;
import au.com.windyroad.hyperstate.client.mixins.LinkMixin;
import au.com.windyroad.hyperstate.client.mixins.NavigationalRelationshipMixin;
import au.com.windyroad.hyperstate.core.Action;
import au.com.windyroad.hyperstate.core.EntityRelationship;
import au.com.windyroad.hyperstate.core.Link;
import au.com.windyroad.hyperstate.core.NavigationalRelationship;
import au.com.windyroad.hyperstate.core.Resolver;

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

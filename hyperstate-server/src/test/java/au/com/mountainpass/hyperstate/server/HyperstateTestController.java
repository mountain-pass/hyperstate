package au.com.mountainpass.hyperstate.server;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

import au.com.mountainpass.hyperstate.client.RepositoryResolver;
import au.com.mountainpass.hyperstate.core.EntityRepository;
import au.com.mountainpass.hyperstate.core.JavaLink;
import au.com.mountainpass.hyperstate.core.Titled;
import au.com.mountainpass.hyperstate.core.Link;
import au.com.mountainpass.hyperstate.core.MediaTypes;
import au.com.mountainpass.hyperstate.core.NavigationalRelationship;
import au.com.mountainpass.hyperstate.core.entities.EntityWrapper;
import au.com.mountainpass.hyperstate.core.entities.VanillaEntity;
import au.com.mountainpass.hyperstate.server.entities.HyperstateRootEntity;
import au.com.mountainpass.hyperstate.server.serialization.mixins.LinkMixin;

@Controller
@RequestMapping(value = "/", produces = { MediaTypes.SIREN_JSON_VALUE,
        MediaType.APPLICATION_JSON_VALUE })
public class HyperstateTestController extends HyperstateController {

    @Autowired
    EntityRepository repository;

    @Autowired
    RepositoryResolver resolver;

    @Autowired
    ObjectMapper objectMapper;

    @PostConstruct
    public void onConstructed() {
        objectMapper.addMixIn(JavaLink.class, LinkMixin.class);
        objectMapper.addMixIn(Link.class, LinkMixin.class);
        objectMapper.addMixIn(Titled.class, Titled.class);

        final EntityWrapper<?> root = new HyperstateRootEntity(resolver,
                this.getClass());
        root.setRepository(repository);
        repository.save(root);

        final VanillaEntity accounts = new VanillaEntity(resolver,
                root.getId() + "accounts", "Accounts", "Accounts");
        repository.save(accounts);
        accounts.setRepository(repository);

        root.add(new NavigationalRelationship(accounts, "accounts"));

    }

}

package au.com.mountainpass.hyperstate.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import au.com.mountainpass.hyperstate.client.RepositoryResolver;
import au.com.mountainpass.hyperstate.core.EntityRepository;
import au.com.mountainpass.hyperstate.core.MediaTypes;
import au.com.mountainpass.hyperstate.core.NavigationalRelationship;
import au.com.mountainpass.hyperstate.core.entities.EntityWrapper;
import au.com.mountainpass.hyperstate.core.entities.VanillaEntity;

@Controller
@RequestMapping(value = "/", produces = { MediaTypes.SIREN_JSON_VALUE,
        MediaType.APPLICATION_JSON_VALUE })
public class HyperstateTestController extends HyperstateController {

    @Autowired
    EntityRepository repository;

    @Autowired
    RepositoryResolver resolver;

    @Override
    protected void onConstructed(EntityWrapper<?> root) {
        final VanillaEntity accounts = new VanillaEntity(resolver,
                root.getId() + "accounts", "Accounts", "Accounts");
        repository.save(accounts);
        accounts.setRepository(repository);

        root.add(new NavigationalRelationship(accounts, "accounts"));
    }

}

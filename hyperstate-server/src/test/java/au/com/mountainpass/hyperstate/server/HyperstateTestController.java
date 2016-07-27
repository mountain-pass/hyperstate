package au.com.mountainpass.hyperstate.server;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import au.com.mountainpass.hyperstate.core.EntityRepository;
import au.com.mountainpass.hyperstate.core.NavigationalRelationship;
import au.com.mountainpass.hyperstate.core.entities.EntityWrapper;
import au.com.mountainpass.hyperstate.core.entities.VanillaEntity;
import au.com.mountainpass.hyperstate.server.entities.HyperstateRootEntity;

@Controller
@RequestMapping(value = "/hyperstateTest")
public class HyperstateTestController extends HyperstateController {

    @Autowired
    EntityRepository repository;

    @PostConstruct
    public void onConstructed() {
        final EntityWrapper<?> root = new HyperstateRootEntity(this.getClass());
        root.setRepository(repository);
        repository.save(root);

        final VanillaEntity accounts = new VanillaEntity(
                root.getId() + "/accounts", "Accounts", "Accounts");
        repository.save(accounts);
        accounts.setRepository(repository);

        root.add(new NavigationalRelationship(accounts, "accounts"));

    }
}

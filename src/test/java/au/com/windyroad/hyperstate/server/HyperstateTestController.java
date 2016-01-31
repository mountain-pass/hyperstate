package au.com.windyroad.hyperstate.server;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import au.com.windyroad.hyperstate.core.EntityRepository;
import au.com.windyroad.hyperstate.core.NavigationalRelationship;
import au.com.windyroad.hyperstate.core.entities.EntityWrapper;
import au.com.windyroad.hyperstate.core.entities.VanillaEntity;

@Controller
@RequestMapping(value = "/hyperstateTest")
public class HyperstateTestController extends HyperstateController {

    @Autowired
    ApplicationContext context;

    @Autowired
    EntityRepository repository;

    @PostConstruct
    public void onConstructed() {
        EntityWrapper<?> root = getRoot().join();
        VanillaEntity accounts = new VanillaEntity(context,
                root.getId() + "/accounts", "Accounts", "Accounts");
        repository.save(accounts);
        accounts.setRepository(repository);

        root.add(new NavigationalRelationship(accounts, "accounts"));

    }
}

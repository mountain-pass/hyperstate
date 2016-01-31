package au.com.windyroad.hyperstate.server;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import au.com.windyroad.hyperstate.core.NavigationalRelationship;
import au.com.windyroad.hyperstate.core.entities.EntityWrapper;
import au.com.windyroad.hyperstate.core.entities.VanillaEntity;

@Controller
@RequestMapping(value = "/hyperstateTest")
public class HyperstateTestController extends HyperstateController {

    @PostConstruct
    public void onConstructed() {
        EntityWrapper<?> root = getRoot().join();
        VanillaEntity accounts = new VanillaEntity(context, repository,
                root.getId() + "/accounts", "Accounts", "Accounts");

        root.add(new NavigationalRelationship(accounts, "accounts"));

    }
}

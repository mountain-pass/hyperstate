package au.com.windyroad.hyperstate.server.entities;

import org.springframework.context.ApplicationContext;

import au.com.windyroad.hyperstate.core.EntityRepository;
import au.com.windyroad.hyperstate.core.entities.VanillaEntity;
import au.com.windyroad.hyperstate.server.HyperstateController;

public class HyperstateTestRoot extends HyperstateRootEntity {

    public HyperstateTestRoot(ApplicationContext context,
            EntityRepository repository,
            Class<? extends HyperstateController> controllerClass) {
        super(context, repository, controllerClass);

        VanillaEntity accounts = new VanillaEntity(context, repository,
                this.getId() + "/accounts", "Accounts");
    }

}

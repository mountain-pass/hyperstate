package au.com.mountainpass.hyperstate.server;

import au.com.mountainpass.hyperstate.core.EntityRepository;
import au.com.mountainpass.hyperstate.core.NavigationalRelationship;
import au.com.mountainpass.hyperstate.server.entities.Accounts;
import au.com.mountainpass.hyperstate.server.entities.HyperstateRootEntity;

public class HyperstateTestRootEntity extends HyperstateRootEntity {

    protected HyperstateTestRootEntity() {
    }

    public HyperstateTestRootEntity(EntityRepository repository,
            Class<? extends HyperstateController> controllerClass) {
        super(repository, controllerClass);

        repository.save(this).thenCompose(root -> {
            final Accounts accounts = new Accounts(repository,
                    root.getId() + "accounts");
            return repository.save(accounts);
        }).thenCompose(accounts -> {
            this.add(new NavigationalRelationship(accounts, "accounts"));
            return repository.save(this);
        }).join();
    }

}

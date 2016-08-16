package au.com.mountainpass.hyperstate.server;

import au.com.mountainpass.hyperstate.client.RepositoryResolver;
import au.com.mountainpass.hyperstate.core.NavigationalRelationship;
import au.com.mountainpass.hyperstate.server.entities.Accounts;
import au.com.mountainpass.hyperstate.server.entities.HyperstateRootEntity;

public class HyperstateTestRootEntity extends HyperstateRootEntity {

    protected HyperstateTestRootEntity() {
    }

    public HyperstateTestRootEntity(RepositoryResolver resolver,
            Class<? extends HyperstateController> controllerClass) {
        super(resolver, controllerClass);
        resolver.getRepository().save(this);

        final Accounts accounts = new Accounts(resolver,
                this.getId() + "accounts");
        resolver.getRepository().save(accounts);
        this.add(new NavigationalRelationship(accounts, "accounts"));
    }

}

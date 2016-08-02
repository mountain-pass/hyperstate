package au.com.mountainpass.hyperstate.server;

import au.com.mountainpass.hyperstate.client.RepositoryResolver;
import au.com.mountainpass.hyperstate.core.NavigationalRelationship;
import au.com.mountainpass.hyperstate.core.entities.VanillaEntity;
import au.com.mountainpass.hyperstate.server.entities.HyperstateRootEntity;

public class HyperstateTestRootEntity extends HyperstateRootEntity {

    protected HyperstateTestRootEntity() {
    }

    public HyperstateTestRootEntity(RepositoryResolver resolver,
            Class<? extends HyperstateController> controllerClass) {
        super(resolver, controllerClass);
        resolver.getRepository().save(this);

        final VanillaEntity accounts = new VanillaEntity(resolver,
                this.getId() + "accounts", "Accounts", "Accounts");
        resolver.getRepository().save(accounts);
        this.add(new NavigationalRelationship(accounts, "accounts"));
    }

}

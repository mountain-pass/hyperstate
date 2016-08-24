package au.com.mountainpass.hyperstate.server;

import java.util.concurrent.CompletableFuture;

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
        CompletableFuture<HyperstateTestRootEntity> rootFuture = resolver
                .getEntityRepository().save(this);

        final Accounts accounts = new Accounts(resolver,
                this.getId() + "accounts");
        CompletableFuture<Accounts> accountsFuture = resolver.getEntityRepository()
                .save(accounts);
        rootFuture.thenCombine(accountsFuture, (root, accountsContainer) -> {
            return null;
        }).join();

        this.add(new NavigationalRelationship(accounts, "accounts"));
    }

}

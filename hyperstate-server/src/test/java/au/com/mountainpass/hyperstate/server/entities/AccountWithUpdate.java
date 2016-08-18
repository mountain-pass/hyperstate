package au.com.mountainpass.hyperstate.server.entities;

import java.util.concurrent.CompletableFuture;

import au.com.mountainpass.hyperstate.client.RepositoryResolver;
import au.com.mountainpass.hyperstate.core.entities.UpdatedEntity;

public class AccountWithUpdate extends Account {

    protected AccountWithUpdate() {
    }

    public AccountWithUpdate(final RepositoryResolver resolver,
            final AccountProperties properties, final String path,
            final String title) {
        super(resolver, properties, path, title);
    }

    public CompletableFuture<UpdatedEntity> update(String username) {
        return CompletableFuture.supplyAsync(() -> {
            this.getProperties().setUsername(username);
            getResolver().getRepository().save(this);
            return new UpdatedEntity(this);
        });
    }
}

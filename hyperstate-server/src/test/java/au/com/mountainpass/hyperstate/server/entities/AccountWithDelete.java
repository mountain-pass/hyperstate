package au.com.mountainpass.hyperstate.server.entities;

import java.util.concurrent.CompletableFuture;

import au.com.mountainpass.hyperstate.client.RepositoryResolver;
import au.com.mountainpass.hyperstate.core.entities.DeletedEntity;

public class AccountWithDelete extends Account {

    protected AccountWithDelete() {
    }

    public AccountWithDelete(final RepositoryResolver resolver,
            final AccountProperties properties, final String path,
            final String title) {
        super(resolver, properties, path, title);
    }

    public CompletableFuture<DeletedEntity> delete() {
        return getResolver().delete(this);
    }
}

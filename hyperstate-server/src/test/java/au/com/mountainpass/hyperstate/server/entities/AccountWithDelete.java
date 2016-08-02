package au.com.mountainpass.hyperstate.server.entities;

import java.util.concurrent.CompletableFuture;

import au.com.mountainpass.hyperstate.client.RepositoryResolver;

public class AccountWithDelete extends Account {

    protected AccountWithDelete() {
    }

    protected AccountWithDelete(final AccountWithDelete src) {
        super(src);
    }

    public AccountWithDelete(final RepositoryResolver resolver,
            final AccountProperties properties, final String path,
            final String title) {
        super(resolver, properties, path, title);
    }

    public CompletableFuture<Void> delete() {
        return getResolver().delete(this);
    }
}

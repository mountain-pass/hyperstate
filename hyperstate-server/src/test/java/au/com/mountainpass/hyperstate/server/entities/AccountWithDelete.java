package au.com.mountainpass.hyperstate.server.entities;

import java.util.concurrent.CompletableFuture;

import au.com.mountainpass.hyperstate.core.EntityRepository;
import au.com.mountainpass.hyperstate.core.entities.DeletedEntity;

public class AccountWithDelete extends Account {

    protected AccountWithDelete() {
    }

    public AccountWithDelete(final EntityRepository repository,
            final AccountProperties properties, final String path,
            final String title) {
        super(repository, properties, path, title);
    }

    public CompletableFuture<DeletedEntity> delete() {
        return getRepository().delete(this);
    }
}

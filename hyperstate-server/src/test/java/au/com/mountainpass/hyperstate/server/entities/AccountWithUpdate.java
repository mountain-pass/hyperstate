package au.com.mountainpass.hyperstate.server.entities;

import java.util.concurrent.CompletableFuture;

import au.com.mountainpass.hyperstate.core.EntityRepository;
import au.com.mountainpass.hyperstate.core.entities.UpdatedEntity;

public class AccountWithUpdate extends Account {

    protected AccountWithUpdate() {
    }

    public AccountWithUpdate(final EntityRepository repository,
            final AccountProperties properties, final String path,
            final String title) {
        super(repository, properties, path, title);
    }

    public CompletableFuture<UpdatedEntity> update(String username) {
        this.getProperties().setUsername(username);
        return getRepository().save(this).thenApply(entity -> {
            return new UpdatedEntity(entity);
        });
    }
}

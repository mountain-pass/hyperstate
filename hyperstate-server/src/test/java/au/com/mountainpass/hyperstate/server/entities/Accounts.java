package au.com.mountainpass.hyperstate.server.entities;

import java.util.concurrent.CompletableFuture;

import au.com.mountainpass.hyperstate.client.RepositoryResolver;
import au.com.mountainpass.hyperstate.core.entities.CreatedEntity;
import au.com.mountainpass.hyperstate.core.entities.EntityWrapper;

public class Accounts extends EntityWrapper<Void> {

    protected Accounts() {
        super((Void) null);
    }

    public Accounts(final RepositoryResolver resolver, final String path) {
        super(resolver, path, (Void) null, "Accounts", "Accounts");
    }

    public CompletableFuture<CreatedEntity> createAccount(String username) {
        return CompletableFuture.supplyAsync(() -> {
            Account account = new Account(getResolver(), getId(), username);
            getResolver().getRepository().save(account);
            return new CreatedEntity(account);
        });
    }

}

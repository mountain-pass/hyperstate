package au.com.mountainpass.hyperstate.server.entities;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import au.com.mountainpass.hyperstate.client.RepositoryResolver;
import au.com.mountainpass.hyperstate.core.EntityRepository;

public class AccountBuilder {

    /**
     * 
     */
    private String creationDate;
    private String username;
    private boolean deletable = false;
    private boolean updateable = false;

    public AccountBuilder() {
        // TODO Auto-generated constructor stub
    }

    public CompletableFuture<Account> build(final RepositoryResolver resolver,
            final EntityRepository repository, final String path)
                    throws InterruptedException, ExecutionException {
        AccountProperties properties = new AccountProperties(username,
                creationDate);

        Account entity;
        if (deletable) {
            entity = new AccountWithDelete(resolver, properties, path,
                    "The Account");
        } else if (updateable) {
            entity = new AccountWithUpdate(resolver, properties, path,
                    "The Account");
        } else {
            entity = new Account(resolver, properties, path, "The Account");
        }
        return repository.save(entity);
    }

    public AccountBuilder creationDate(String creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public AccountBuilder userName(String username) {
        this.username = username;
        return this;
    }

    public void isDeletable(boolean b) {
        this.deletable = true;
    }

    public void isUpdatable(boolean b) {
        this.updateable = true;
    }

}
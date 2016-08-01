package au.com.mountainpass.hyperstate.server.entities;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import au.com.mountainpass.hyperstate.core.EntityRepository;
import au.com.mountainpass.hyperstate.core.Resolver;

public class AccountBuilder {

    /**
     * 
     */
    private String creationDate;
    private String username;

    public AccountBuilder() {
        // TODO Auto-generated constructor stub
    }

    public CompletableFuture<Account> build(final Resolver resolver,
            final EntityRepository repository, final String path)
                    throws InterruptedException, ExecutionException {
        AccountProperties properties = new AccountProperties(username,
                creationDate);
        final Account entity = new Account(resolver, properties, path,
                "The Account");
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

}
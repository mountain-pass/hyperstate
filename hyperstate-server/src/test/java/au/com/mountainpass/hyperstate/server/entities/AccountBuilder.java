package au.com.mountainpass.hyperstate.server.entities;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import au.com.mountainpass.hyperstate.core.EntityRepository;

public class AccountBuilder {

    /**
     * 
     */
    private String creationDate;
    private String username;

    public AccountBuilder() {
        // TODO Auto-generated constructor stub
    }

    public CompletableFuture<Account> build(final String path,
            final EntityRepository repository)
                    throws InterruptedException, ExecutionException {
        AccountProperties properties = new AccountProperties(username,
                creationDate);
        final Account entity = new Account(properties, path, "The Account");
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
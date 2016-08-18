package au.com.mountainpass.hyperstate.server.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import au.com.mountainpass.hyperstate.client.RepositoryResolver;
import au.com.mountainpass.hyperstate.core.EntityRepository;
import au.com.mountainpass.hyperstate.core.entities.EntityWrapper;

public class Account extends EntityWrapper<AccountProperties> {

    public static AccountBuilder builder() {
        return new AccountBuilder();
    }

    @Autowired
    private ApplicationContext context;

    @Autowired
    private EntityRepository repository;

    protected Account() {
        super(new AccountProperties());
    }

    public Account(final RepositoryResolver resolver,
            final AccountProperties properties, final String path,
            final String title) {
        super(resolver, path, properties, title, "Account");
    }

    public Account(RepositoryResolver resolver, String parentPath,
            String username) {
        super(resolver, parentPath + "/" + UUID.randomUUID(),
                createProperties(username), username, "Account");
    }

    private static AccountProperties createProperties(String username) {
        return new AccountProperties(username, LocalDateTime.now());
    }

}

package au.com.mountainpass.hyperstate.server.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import au.com.mountainpass.hyperstate.core.EntityRepository;
import au.com.mountainpass.hyperstate.core.Resolver;
import au.com.mountainpass.hyperstate.core.entities.EntityWrapper;

public class Account extends EntityWrapper<AccountProperties> {

    public static AccountBuilder builder() {
        return new AccountBuilder();
    }

    @Autowired
    ApplicationContext context;

    @Autowired
    EntityRepository repository;

    protected Account() {
        super(new AccountProperties());
    }

    protected Account(final Account src) {
        super(src);
        this.context = src.context;
        this.repository = src.repository;
    }

    public Account(final Resolver resolver, final AccountProperties properties,
            final String path, final String title) {
        super(resolver, path, properties, title, "Account");
    }

}

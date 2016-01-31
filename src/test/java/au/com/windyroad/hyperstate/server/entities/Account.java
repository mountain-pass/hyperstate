package au.com.windyroad.hyperstate.server.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import au.com.windyroad.hyperstate.core.EntityRepository;
import au.com.windyroad.hyperstate.core.entities.EntityWrapper;

public class Account extends EntityWrapper<AccountProperties> {

    @Autowired
    ApplicationContext context;

    @Autowired
    EntityRepository repository;

    protected Account() {
        super(new AccountProperties());
    }

    protected Account(Account src) {
        super(src);
        this.context = src.context;
        this.repository = src.repository;
    }

    public Account(ApplicationContext context, String path,
            AccountProperties properties, String title) {
        super(context, path, properties, title, "Account");
    }

}

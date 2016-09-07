package au.com.mountainpass.hyperstate.server.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import au.com.mountainpass.hyperstate.core.EntityRepository;
import au.com.mountainpass.hyperstate.core.entities.EntityWrapper;

public class Account extends EntityWrapper<AccountProperties> {

    public static AccountBuilder builder() {
        return new AccountBuilder();
    }

    protected Account() {
        super(new AccountProperties());
    }

    public Account(final EntityRepository repository,
            final AccountProperties properties, final String path,
            final String title) {
        super(repository, path, properties, title, "Account");
    }

    public Account(EntityRepository repository, String parentPath,
            String username) {
        super(repository, parentPath + "/" + UUID.randomUUID(),
                createProperties(username), username, "Account");
    }

    public String localMethod() {
        return "don't try to call me remotely";
    }

    private static AccountProperties createProperties(String username) {
        return new AccountProperties(username, LocalDateTime.now());
    }

}

package au.com.mountainpass.hyperstate.server.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import au.com.mountainpass.hyperstate.core.EntityRepository;
import au.com.mountainpass.hyperstate.core.entities.EntityWrapper;

public class Account extends EntityWrapper<AccountProperties> {

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

  public Account(final String path, final AccountProperties properties, final String title) {
    super(path, properties, title, "Account");
  }

}

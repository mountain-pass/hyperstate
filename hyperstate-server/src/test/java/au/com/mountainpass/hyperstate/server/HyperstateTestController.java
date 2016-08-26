package au.com.mountainpass.hyperstate.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import au.com.mountainpass.hyperstate.client.RepositoryResolver;
import au.com.mountainpass.hyperstate.core.EntityRepository;
import au.com.mountainpass.hyperstate.server.entities.AccountProperties;
import au.com.mountainpass.hyperstate.server.serialization.mixins.AccountPropertiesMixin;

@Controller
public class HyperstateTestController extends HyperstateController {

    @Autowired
    private EntityRepository repository;

    @Autowired
    private RepositoryResolver resolver;

    @Override
    protected void onConstructed() {
        init();
    }

    public void init() {
        super.getObjectMapper().addMixIn(AccountProperties.class,
                AccountPropertiesMixin.class);

        final HyperstateTestRootEntity root = new HyperstateTestRootEntity(
                resolver, this.getClass());
    }
}

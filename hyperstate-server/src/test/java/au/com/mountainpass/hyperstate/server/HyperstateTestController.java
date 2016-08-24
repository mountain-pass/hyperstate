package au.com.mountainpass.hyperstate.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import au.com.mountainpass.hyperstate.client.RepositoryResolver;
import au.com.mountainpass.hyperstate.core.EntityRepository;
import au.com.mountainpass.hyperstate.core.MediaTypes;
import au.com.mountainpass.hyperstate.server.entities.AccountProperties;
import au.com.mountainpass.hyperstate.server.serialization.mixins.AccountPropertiesMixin;

@Controller
@RequestMapping(value = "/", produces = { MediaTypes.SIREN_JSON_VALUE,
        MediaType.APPLICATION_JSON_VALUE })
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

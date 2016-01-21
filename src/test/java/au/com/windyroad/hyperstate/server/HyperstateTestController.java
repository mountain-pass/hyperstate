package au.com.windyroad.hyperstate.server;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import au.com.windyroad.hyperstate.core.EntityRepository;
import au.com.windyroad.hyperstate.core.entities.EntityWrapper;

@Controller
@RequestMapping(value = "/**")
public class HyperstateTestController extends HyperstateController {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    EntityRepository repository;

    @Autowired
    ApplicationContext context;

    @Override
    protected CompletableFuture<EntityWrapper<?>> getEntity(String identifier) {
        return repository.findOne(identifier);
    }

    @Override
    protected CompletableFuture<Void> deleteEntity(EntityWrapper<?> entity) {
        return repository.delete(entity);
    }
}

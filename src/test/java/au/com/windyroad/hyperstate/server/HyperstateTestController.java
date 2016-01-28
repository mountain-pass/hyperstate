package au.com.windyroad.hyperstate.server;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import au.com.windyroad.hyperstate.core.EntityRepository;
import au.com.windyroad.hyperstate.core.entities.EntityWrapper;

@Controller
@RequestMapping(value = "/hyperstateTest")
public class HyperstateTestController extends HyperstateController {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private EntityRepository repository;

    private ApplicationContext context;

    private HyperstateTestRoot hyperstateTestRoot;

    @Autowired
    public HyperstateTestController(ApplicationContext context,
            EntityRepository repository) {
        this.context = context;
        this.repository = repository;
        hyperstateTestRoot = new HyperstateTestRoot(context, repository, this,
                "HyperstateTestRoot");
    }

    @Override
    protected CompletableFuture<EntityWrapper<?>> getEntity(String identifier) {
        RequestMapping requestMapping = AnnotationUtils
                .findAnnotation(this.getClass(), RequestMapping.class);
        if (Arrays.asList(requestMapping.value()).contains(identifier)) {
            return CompletableFuture.supplyAsync(() -> hyperstateTestRoot);
        } else {
            return repository.findOne(identifier);
        }
    }

    @Override
    protected CompletableFuture<Void> deleteEntity(EntityWrapper<?> entity) {
        return repository.delete(entity);
    }

    @Override
    public EntityWrapper<?> getRoot() {
        return hyperstateTestRoot;
    }
}

package au.com.windyroad.hyperstate.server;

import java.util.Properties;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import au.com.windyroad.hyperstate.core.EntityRepository;
import au.com.windyroad.hyperstate.core.Relationship;
import au.com.windyroad.hyperstate.core.entities.CreatedEntity;
import au.com.windyroad.hyperstate.core.entities.EntityWrapper;

//@Component
public class HyperstateApplication extends EntityWrapper<Properties> {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public HyperstateApplication() {
        super(new Properties());
    }

    @Autowired
    ApplicationContext context;

    @Autowired
    EntityRepository repository;

    @Autowired
    public HyperstateApplication(ApplicationContext context,
            EntityRepository repository) {
        super(context, "/", System.getProperties(), "Hyperstate Application");
        LOGGER.debug(this.getLink(Relationship.SELF).getNatures().toString());
    }

    public CompletableFuture<CreatedEntity> addApplication(String name,
            String className) throws BeansException, IllegalStateException,
                    ClassNotFoundException {
        Class<?> type = Class.forName(className);
        if (EntityWrapper.class.isAssignableFrom(type)) {
            EntityWrapper<?> entity = (EntityWrapper<?>) context
                    .getAutowireCapableBeanFactory().createBean(type);
            return repository.save(entity).thenApply(e -> new CreatedEntity(e));
        } else {
            throw new NotImplementedException("TODO: bad request - " + className
                    + " is not an instance of "
                    + EntityWrapper.class.getCanonicalName());
        }
    }

}

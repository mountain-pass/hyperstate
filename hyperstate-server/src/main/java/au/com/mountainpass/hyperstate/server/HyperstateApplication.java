package au.com.mountainpass.hyperstate.server;

import java.util.Properties;
import java.util.concurrent.CompletableFuture;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import au.com.mountainpass.hyperstate.core.EntityRepository;
import au.com.mountainpass.hyperstate.core.Relationship;
import au.com.mountainpass.hyperstate.core.entities.CreatedEntity;
import au.com.mountainpass.hyperstate.core.entities.EntityWrapper;
import au.com.mountainpass.hyperstate.server.serialization.ObjectMapperSerialisationUpdater;

//@Component
public class HyperstateApplication extends EntityWrapper<Properties> {
    @Autowired
    ApplicationContext context;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    EntityRepository repository;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ObjectMapperSerialisationUpdater objectMapperSerialisationUpdater;

    @PostConstruct
    public void postConstruct() {
        objectMapperSerialisationUpdater.addMixins(om);
    }

    public HyperstateApplication() {
        super(new Properties());
    }

    @Autowired
    public HyperstateApplication(final EntityRepository repository) {
        super("/", System.getProperties(), "Hyperstate Application");
        LOGGER.debug(this.getLink(Relationship.SELF).getNatures().toString());
    }

    public CompletableFuture<CreatedEntity> addApplication(final String name,
            final String className) throws BeansException,
                    IllegalStateException, ClassNotFoundException {
        final Class<?> type = Class.forName(className);
        if (EntityWrapper.class.isAssignableFrom(type)) {
            final EntityWrapper<?> entity = (EntityWrapper<?>) context
                    .getAutowireCapableBeanFactory().createBean(type);
            return repository.save(entity).thenApply(e -> new CreatedEntity(e));
        } else {
            throw new NotImplementedException("TODO: bad request - " + className
                    + " is not an instance of "
                    + EntityWrapper.class.getCanonicalName());
        }
    }

}

package au.com.windyroad.hyperstate.client;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.context.ApplicationContext;

import au.com.windyroad.hyperstate.core.EntityRelationship;
import au.com.windyroad.hyperstate.core.EntityRepository;
import au.com.windyroad.hyperstate.core.entities.EntityWrapper;

public class RestEntity<T> extends EntityWrapper<T> {

    private Collection<EntityRelationship> entities = new ArrayList<>();

    public RestEntity(T properties) {
        super(properties);
        throw new NotImplementedException("dead?");
    }

    public RestEntity(ApplicationContext context, EntityRepository repository,
            String path, T properties, String title) {
        super(context, repository, path, properties, title);
    }

    /*
     * (non-Javadoc)
     * 
     * @see au.com.windyroad.hateoas.core.ResolvedEntity#getEntities(int)
     */
    @Override
    public CompletableFuture<Collection<EntityRelationship>> getEntities(
            int page) throws IllegalAccessException, IllegalArgumentException,
                    InvocationTargetException {
        return CompletableFuture.supplyAsync(() -> this.entities);
    }

    /*
     * (non-Javadoc)
     * 
     * @see au.com.windyroad.hateoas.core.ResolvedEntity#setEntities(java.util.
     * Collection)
     */
    // @Override
    // public void setEntities(Collection<EntityRelationship>
    // entityRelationships)
    // throws IllegalAccessException, IllegalArgumentException,
    // InvocationTargetException {
    // this.entities.addAll(entityRelationships);
    // }

}

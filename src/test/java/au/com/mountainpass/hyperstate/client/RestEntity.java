package au.com.mountainpass.hyperstate.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.context.ApplicationContext;

import au.com.mountainpass.hyperstate.core.EntityRelationship;
import au.com.mountainpass.hyperstate.core.entities.EntityWrapper;

public class RestEntity<T> extends EntityWrapper<T> {

    private Collection<EntityRelationship> entities = new ArrayList<>();

    public RestEntity(T properties) {
        super(properties);
        throw new NotImplementedException("dead?");
    }

    public RestEntity(ApplicationContext context, String path, T properties,
            String title) {
        super(path, properties, title);
    }

    /*
     * (non-Javadoc)
     * 
     * @see au.com.mountainpass.hateoas.core.ResolvedEntity#getEntities(int)
     */
    @Override
    public CompletableFuture<Collection<EntityRelationship>> getEntities(
            int page) {
        return CompletableFuture.supplyAsync(() -> this.entities);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * au.com.mountainpass.hateoas.core.ResolvedEntity#setEntities(java.util.
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

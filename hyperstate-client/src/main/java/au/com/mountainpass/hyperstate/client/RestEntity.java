package au.com.mountainpass.hyperstate.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.NotImplementedException;

import au.com.mountainpass.hyperstate.core.EntityRelationship;
import au.com.mountainpass.hyperstate.core.Resolver;
import au.com.mountainpass.hyperstate.core.entities.EntityWrapper;

public class RestEntity<T> extends EntityWrapper<T> {

    private final Collection<EntityRelationship> entities = new ArrayList<>();

    public RestEntity(final Resolver resolver, final String path,
            final T properties, final String title) {
        super(null, path, properties, title);
        throw new NotImplementedException("dead?");
    }

    public RestEntity(final T properties) {
        super(properties);
        throw new NotImplementedException("dead?");
    }

    /*
     * (non-Javadoc)
     * 
     * @see au.com.mountainpass.hateoas.core.ResolvedEntity#getEntities(int)
     */
    @Override
    public CompletableFuture<Collection<EntityRelationship>> getEntities(
            final int page) {
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

package au.com.mountainpass.hyperstate.server.entities;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonInclude;

import au.com.mountainpass.hyperstate.core.EntityRelationship;
import au.com.mountainpass.hyperstate.core.EntityRepository;
import au.com.mountainpass.hyperstate.core.Relationship;
import au.com.mountainpass.hyperstate.core.entities.CreatedEntity;
import au.com.mountainpass.hyperstate.core.entities.EntityWrapper;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Accounts extends EntityWrapper<Void> {

    protected Accounts() {
        super((Void) null);
    }

    public Accounts(final EntityRepository repository, final String path) {
        super(repository, path, (Void) null, "Accounts", "Accounts");
    }

    public CompletableFuture<CreatedEntity> createAccount(String username) {
        return CompletableFuture.supplyAsync(() -> {
            Account account = new Account(getRepository(), getId(), username);
            this.addEntity(new EntityRelationship(account, Relationship.ITEM));

            getRepository().save(account).thenCompose(saved -> {
                return getRepository().save(this);
            });

            // note: returns before save is complete
            // should use the future for returning the created entity, in case
            // the save throws an exception.
            return new CreatedEntity(account);
        });
    }

    // TODO get action should be able to return futures with sub-types of entity
    // wrapper
    public CompletableFuture<EntityWrapper<?>> get(String username) {
        // yes this is very inefficient. The plan is to get it working and then
        // get it right

        Stream<EntityRelationship> children;
        children = getEntities().stream();

        Stream<EntityRelationship> filtered = children.filter(child -> {
            String childUsername = child.resolve(Account.class).join()
                    .getProperties().getUsername();
            return childUsername == null && username == null
                    || childUsername.equals(username);
        });

        EntityRelationship entityRelationship = filtered.findAny().get();
        return (CompletableFuture) entityRelationship.resolve(Account.class);
    }

}

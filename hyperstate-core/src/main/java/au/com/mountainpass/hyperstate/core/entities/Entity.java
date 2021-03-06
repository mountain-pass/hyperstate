package au.com.mountainpass.hyperstate.core.entities;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Identifiable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import au.com.mountainpass.hyperstate.core.Address;
import au.com.mountainpass.hyperstate.core.Titled;

public abstract class Entity extends Titled implements Identifiable<String> {

    public Entity() {
    }

    public Entity(final String title, final String... natures) {
        super(title, natures);
    }

    public abstract <K, T extends EntityWrapper<K>> T reload(Class<T> type);

    public abstract <T extends EntityWrapper<?>> CompletableFuture<T> resolve(
            Class<T> type);

    public abstract <K, T extends EntityWrapper<K>> T resolve(
            ParameterizedTypeReference<T> type)
                    throws InterruptedException, ExecutionException;

    @JsonIgnore
    public abstract LinkedEntity toLinkedEntity();

    public abstract Address getAddress();

}

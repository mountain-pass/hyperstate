package au.com.mountainpass.hyperstate.core.entities;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.core.ParameterizedTypeReference;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import au.com.mountainpass.hyperstate.core.Address;
import au.com.mountainpass.hyperstate.core.Link;

public class LinkedEntity extends Entity {
    private final Link link;

    public LinkedEntity(final Link link) {
        this.link = link;
    }

    public LinkedEntity(final Link link, final String title,
            final Set<String> classes) {
        this.link = link;
        setClasses(classes);
        setTitle(title);
    }

    @JsonUnwrapped
    public Link getLink() {
        return link;
    }

    @Override
    public <K, T extends EntityWrapper<K>> T reload(final Class<T> type) {
        throw new NotImplementedException("TODO");
    }

    @Override
    public <T extends EntityWrapper<?>> CompletableFuture<T> resolve(
            final Class<T> type) {
        return link.resolve(type);
    }

    @Override
    public <K, T extends EntityWrapper<K>> T resolve(
            final ParameterizedTypeReference<T> type)
                    throws InterruptedException, ExecutionException {
        return link.resolve(type).get();
    }

    @Override
    public LinkedEntity toLinkedEntity() {
        return this;
    }

    @Override
    public Address getAddress() {
        return link.getAddress();
    }

    @Override
    public String getId() {
        return link.getPath();
    }
}

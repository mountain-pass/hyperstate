package au.com.mountainpass.hyperstate.core.entities;

import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.core.ParameterizedTypeReference;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import au.com.mountainpass.hyperstate.core.Link;

public class LinkedEntity extends Entity {
    private final Link link;

    public LinkedEntity(final Link link) {
        this.link = link;
    }

    public LinkedEntity(final Link link, final String label,
            final Set<String> natures) {
        this.link = link;
        setNatures(natures);
        setTitle(label);
    }

    public LinkedEntity(final Link link, final String label,
            final String... natures) {
        this.link = link;
        setNatures(new HashSet<String>(Arrays.asList(natures)));
        setTitle(label);
    }

    @Override
    @JsonProperty("href")
    public URI getAddress() {
        return link.getAddress();
    }

    @JsonIgnore
    public Link getLink() {
        return link;
    }

    @Override
    public <K, T extends EntityWrapper<K>> T reload(final Class<T> type) {
        throw new NotImplementedException("TODO");
    }

    @Override
    public <K, T extends EntityWrapper<K>> T resolve(final Class<T> type)
            throws InterruptedException, ExecutionException {
        return link.resolve(type).get();
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
}

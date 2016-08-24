package au.com.mountainpass.hyperstate.core;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.eclipse.jdt.annotation.Nullable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import au.com.mountainpass.hyperstate.core.entities.CreatedEntity;
import au.com.mountainpass.hyperstate.core.entities.DeletedEntity;
import au.com.mountainpass.hyperstate.core.entities.EntityWrapper;
import au.com.mountainpass.hyperstate.core.entities.UpdatedEntity;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Link extends Titled {

    @Nullable
    private MediaType representationFormat = MediaTypes.SIREN_JSON;
    private Address address;

    public Link(@JsonProperty("href") Address address,
            @JsonProperty("title") final String title,
            @JsonProperty("class") final String... classes) {
        super(title, classes);
        this.address = address;
    }

    public Link() {
    }

    public Link(Address address) {
        this.address = address;
    }

    @JsonProperty("type")
    public MediaType getRepresentationFormat() {
        return representationFormat == null ? MediaTypes.SIREN_JSON
                : representationFormat;
    }

    public <T extends EntityWrapper<?>> CompletableFuture<T> resolve(
            Class<T> type) {
        return address.get(type);
    }

    public <T extends EntityWrapper<?>> CompletableFuture<T> resolve(
            ParameterizedTypeReference<T> type) {
        return address.get(type);
    }

    @JsonIgnore
    public String getPath() {
        return address.getPath();
    }

    public CompletableFuture<EntityWrapper<?>> get(
            Map<String, Object> filteredParameters) {
        return address.get(filteredParameters);
    }

    public CompletableFuture<DeletedEntity> delete(
            Map<String, Object> filteredParameters) {
        return address.delete(filteredParameters);

    }

    public CompletableFuture<CreatedEntity> create(
            Map<String, Object> filteredParameters) {
        return address.create(filteredParameters);
    }

    public CompletableFuture<UpdatedEntity> update(
            Map<String, Object> filteredParameters) {
        return address.update(filteredParameters);
    }

    public CompletableFuture<EntityWrapper<?>> get() {
        return address.get();
    }

    @JsonIgnore
    public <T extends EntityWrapper<?>> CompletableFuture<T> get(
            Class<T> type) {
        return address.get(type);
    }

    /**
     * @return the address
     */
    @JsonUnwrapped
    public Address getAddress() {
        return address;
    }

    /**
     * @param address
     *            the address to set
     */
    public void setAddress(Address address) {
        this.address = address;
    }

}

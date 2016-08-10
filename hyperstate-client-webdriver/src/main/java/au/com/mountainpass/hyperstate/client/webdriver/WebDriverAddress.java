package au.com.mountainpass.hyperstate.client.webdriver;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebElement;
import org.springframework.core.ParameterizedTypeReference;

import au.com.mountainpass.hyperstate.core.Address;
import au.com.mountainpass.hyperstate.core.entities.CreatedEntity;
import au.com.mountainpass.hyperstate.core.entities.EntityWrapper;
import au.com.mountainpass.hyperstate.core.entities.UpdatedEntity;

public class WebDriverAddress implements Address {

    private WebElement element;
    private WebDriverResolver resolver;

    public WebDriverAddress(WebDriverResolver resolver, WebElement element) {
        this.resolver = resolver;
        this.element = element;
    }

    @Override
    public <T> CompletableFuture<T> resolve(Class<T> type) {
        throw new NotImplementedException("TODO");
    }

    @Override
    public <T> CompletableFuture<T> resolve(
            ParameterizedTypeReference<T> type) {
        throw new NotImplementedException("TODO");
    }

    @Override
    public String getPath() {
        return element.getAttribute("href");
    }

    @Override
    public CompletableFuture<EntityWrapper<?>> get(
            Map<String, Object> filteredParameters) {
        throw new NotImplementedException("TODO");
    }

    @Override
    public CompletableFuture<Void> delete(
            Map<String, Object> filteredParameters) {
        return resolver.delete(this, filteredParameters);
    }

    @Override
    public CompletableFuture<CreatedEntity> create(
            Map<String, Object> filteredParameters) {
        throw new NotImplementedException("TODO");
    }

    @Override
    public CompletableFuture<UpdatedEntity> update(
            Map<String, Object> parameters) {
        return resolver.update(this, parameters);
    }

    @Override
    public CompletableFuture<EntityWrapper<?>> get() {
        return resolver.get(this);
    }

    @Override
    public <T extends EntityWrapper<?>> CompletableFuture<T> get(
            Class<T> type) {
        return resolver.get(this, type);
    }

    @Override
    public <T extends EntityWrapper<?>> CompletableFuture<T> get(
            ParameterizedTypeReference<T> type) {
        throw new NotImplementedException("TODO");
    }

    public WebElement getWebElement() {
        return this.element;
    }

}

package au.com.mountainpass.hyperstate.client.webdriver;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebElement;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;

import au.com.mountainpass.hyperstate.core.Link;
import au.com.mountainpass.hyperstate.core.entities.CreatedEntity;
import au.com.mountainpass.hyperstate.core.entities.EntityWrapper;
import au.com.mountainpass.hyperstate.core.entities.UpdatedEntity;

public class WebDriverLink extends Link {

    private WebDriverResolver resolver;
    private final WebElement webElement;

    public WebDriverLink(final WebDriverResolver resolver,
            final WebElement webElement) {
        this.webElement = webElement;
        this.resolver = resolver;
    }

    @Override
    public MediaType getRepresentationFormat() {
        throw new NotImplementedException("TODO");
    }

    public WebElement getWebElement() {
        return this.webElement;
    }

    @Override
    public <T> CompletableFuture<T> resolve(final Class<T> type) {
        return resolver.get(this, type);
    }

    @Override
    public <T> CompletableFuture<T> resolve(
            final ParameterizedTypeReference<T> type) {
        return resolver.get(this, type);
    }

    @Override
    public String getPath() {
        return webElement.getAttribute("href");
    }

    @Override
    public CompletableFuture<EntityWrapper<?>> get(
            Map<String, Object> filteredParameters) {
        throw new NotImplementedException("TODO");
    }

    @Override
    public CompletableFuture<Void> delete(
            Map<String, Object> filteredParameters) {
        throw new NotImplementedException("TODO");
    }

    @Override
    public CompletableFuture<CreatedEntity> create(
            Map<String, Object> filteredParameters) {
        throw new NotImplementedException("TODO");
    }

    @Override
    public CompletableFuture<UpdatedEntity> update(
            Map<String, Object> filteredParameters) {
        throw new NotImplementedException("TODO");
    }

    @Override
    public CompletableFuture<EntityWrapper<?>> get() {
        throw new NotImplementedException("TODO");
    }

}

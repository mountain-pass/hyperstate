package au.com.mountainpass.hyperstate.client.webdriver;

import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebElement;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;

import au.com.mountainpass.hyperstate.core.Link;
import au.com.mountainpass.hyperstate.core.Resolver;

public class WebDriverLink extends Link {

    private Resolver resolver;
    private final WebElement webElement;
    private URI address;

    public WebDriverLink(final Resolver resolver, final WebElement webElement) {
        super(resolver, webElement.getAttribute("href"));
        this.webElement = webElement;
        this.resolver = resolver;
        this.address = URI.create(webElement.getAttribute("href"));
    }

    @Override
    public MediaType getRepresentationFormat() {
        throw new NotImplementedException("TODO");
    }

    public WebElement getWebElement() {
        return this.webElement;
    }

    @Override
    public <T> CompletableFuture<T> resolve(final Class<T> type)
            throws InterruptedException, ExecutionException {
        return resolver.get(this, type);
    }

    @Override
    public <T> CompletableFuture<T> resolve(
            final ParameterizedTypeReference<T> type)
                    throws InterruptedException, ExecutionException {
        return resolver.get(this, type);
    }

    @Override
    public URI getAddress() {
        return address;
    }

    @Override
    public String getPath() {
        return address.toString();
    }

}

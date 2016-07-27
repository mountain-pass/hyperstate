package au.com.mountainpass.hyperstate.client.webdriver;

import java.net.URI;

import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebElement;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;

import au.com.mountainpass.hyperstate.core.Link;

public class WebDriverLink extends Link {

    private WebDriverResolver resolver;
    private final WebElement webElement;

    public WebDriverLink(final WebDriverResolver resolver,
            final WebElement webElement) {
        this.webElement = webElement;
        this.resolver = resolver;
    }

    public WebDriverLink(final WebElement webElement) {
        this.webElement = webElement;
    }

    @Override
    public URI getAddress() {
        return URI.create(this.resolver.getUrl());
    }

    @Override
    public String getPath() {
        return this.resolver.getUrl();
    }

    @Override
    public MediaType getRepresentationFormat() {
        throw new NotImplementedException("TODO");
    }

    public WebElement getWebElement() {
        return this.webElement;
    }

    @Override
    public <T> T resolve(final Class<T> type) {
        webElement.click();
        return resolver.createProxy(type);
    }

    @Override
    public <T> T resolve(final ParameterizedTypeReference<T> type) {
        throw new NotImplementedException("TODO");
    }

}

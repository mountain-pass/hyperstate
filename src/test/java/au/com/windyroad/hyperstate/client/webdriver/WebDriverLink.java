package au.com.windyroad.hyperstate.client.webdriver;

import java.net.URI;

import org.openqa.selenium.WebElement;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;

import au.com.windyroad.hyperstate.core.Link;
import cucumber.api.PendingException;

public class WebDriverLink extends Link {

    private WebElement webElement;
    private WebDriverResolver resolver;

    public WebDriverLink(WebElement webElement) {
        this.webElement = webElement;
    }

    public WebDriverLink(WebDriverResolver resolver, WebElement webElement) {
        this.webElement = webElement;
        this.resolver = resolver;
    }

    @Override
    public <T> T resolve(Class<T> type) {
        webElement.click();
        return resolver.createProxy(type);
    }

    @Override
    public <T> T resolve(ParameterizedTypeReference<T> type) {
        // TODO Auto-generated method stub
        throw new PendingException();
    }

    @Override
    public MediaType getRepresentationFormat() {
        throw new PendingException();
    }

    @Override
    public URI getAddress() {
        return URI.create(this.resolver.getUrl());
    }

    public WebElement getWebElement() {
        return this.webElement;
    }

    @Override
    public String getPath() {
        return this.resolver.getUrl();
    }

}

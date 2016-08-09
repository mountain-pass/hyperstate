package au.com.mountainpass.hyperstate.client.webdriver;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.core.ParameterizedTypeReference;

import au.com.mountainpass.hyperstate.core.Address;
import au.com.mountainpass.hyperstate.core.Link;
import au.com.mountainpass.hyperstate.core.Resolver;
import au.com.mountainpass.hyperstate.core.entities.CreatedEntity;
import au.com.mountainpass.hyperstate.core.entities.EntityWrapper;
import au.com.mountainpass.hyperstate.core.entities.UpdatedEntity;
import au.com.mountainpass.hyperstate.exceptions.EntityNotFoundException;

public class WebDriverResolver implements Resolver {

    public static ExpectedCondition<Boolean> angularHasFinishedProcessing() {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(final WebDriver driver) {
                final String hasAngularFinishedScript = "var callback = arguments[arguments.length - 1];\n"
                        + "var el = document.querySelector('html');\n"
                        + "if (!window.angular) {\n" + "    callback('false')\n"
                        + "}\n" + "if (angular.getTestability) {\n"
                        + "    angular.getTestability(el).whenStable(function(){callback('true')});\n"
                        + "} else {\n"
                        + "    if (!angular.element(el).injector()) {\n"
                        + "        callback('false')\n" + "    }\n"
                        + "    var browser = angular.element(el).injector().get('$browser');\n"
                        + "    browser.notifyWhenNoOutstandingRequests(function(){callback('true')});\n"
                        + "}";

                final JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
                final String isProcessingFinished = javascriptExecutor
                        .executeAsyncScript(hasAngularFinishedScript)
                        .toString();

                return Boolean.valueOf(isProcessingFinished);
            }
        };
    }

    private URI baseUri;

    private final WebDriver webDriver;

    public WebDriverResolver(final URI baseUri, final WebDriver webDriver) {
        this.baseUri = baseUri;
        this.webDriver = webDriver;
    }

    public CompletableFuture<CreatedEntity> create(
            final WebDriverAddress address,
            final Map<String, Object> filteredParameters) {
        return CompletableFuture.supplyAsync(create(filteredParameters));
    }

    private Supplier<CreatedEntity> create(
            final Map<String, Object> filteredParameters) {
        return () -> {
            final WebElement form = (new WebDriverWait(getWebDriver(), 5))
                    .until(ExpectedConditions.presenceOfElementLocated(By
                            .name((String) filteredParameters.get("action"))));

            final List<WebElement> inputs = form
                    .findElements(By.tagName("input"));
            for (final WebElement input : inputs) {
                final String inputName = input.getAttribute("name");
                if (inputName != null) {
                    final Object value = filteredParameters.get(inputName);
                    if (value != null) {
                        input.sendKeys(value.toString());
                    }
                }
            }
            form.findElement(By.cssSelector("button[type='submit']")).click();
            Address newAddress = new WebDriverAddress(this,
                    getWebDriver().findElement(By.tagName("html")));
            final CreatedEntity linkedEntity = new CreatedEntity(
                    new Link(newAddress, null));
            return linkedEntity;
        };
    }

    <E> E createProxy(final Class<E> klass) {
        final Enhancer e = initEnhancer(klass);
        @SuppressWarnings("unchecked")
        final E proxy = (E) e.create(new Class[] {}, new Object[] {});
        return proxy;
    }

    public CompletableFuture<Void> delete(final WebDriverAddress address,
            final Map<String, Object> filteredParameters) {
        return CompletableFuture.runAsync(() -> {
            submitForm(address, filteredParameters);
        });
    }

    public <T> CompletableFuture<T> get(final WebDriverAddress address,
            final Map<String, Object> filteredParameters, Class<T> type) {
        return CompletableFuture.supplyAsync(() -> {
            submitForm(address, filteredParameters);
            return createProxy(type);
        });
    }

    private void submitForm(final WebDriverAddress address,
            final Map<String, Object> filteredParameters) {
        final WebElement form = address.getWebElement();
        if ("form".equals(form.getTagName())) {
            for (final WebElement input : form.findElements(By.name("input"))) {
                final Object value = filteredParameters
                        .get(input.getAttribute("name"));
                if (value != null) {
                    input.sendKeys(value.toString());
                }
            }
            form.findElement(By.cssSelector("button[type='submit']")).click();
        } else {
            form.click();
        }
    }

    @Override
    public <E extends EntityWrapper<?>> CompletableFuture<E> get(
            final String path, final Class<E> type) {
        return get(getBaseUri().resolve(path), type);
    }

    public <T> CompletableFuture<T> get(final URI uri, final Class<T> klass) {

        return CompletableFuture.supplyAsync(() -> {
            getWebDriver().get(uri.toString());
            if (getWebDriver().findElements(By.className("status404"))
                    .isEmpty()) {
                return createProxy(klass);
            } else {
                throw new EntityNotFoundException(new WebDriverAddress(this,
                        getWebDriver().findElement(By.tagName("html"))));
            }
        });
    }

    private URI getBaseUri() {
        return baseUri;
    }

    public String getUrl() {
        return getWebDriver().getCurrentUrl();
    }

    <E> Enhancer initEnhancer(final Class<E> klass) {
        final Enhancer e = new Enhancer();

        final WebDriverResolver resolver = this;

        e.setClassLoader(this.getClass().getClassLoader());
        e.setSuperclass(klass);
        e.setCallback(new WebDriverEntityInterceptor<E>(resolver));
        return e;
    }

    public CompletableFuture<UpdatedEntity> update(
            final WebDriverAddress address,
            final Map<String, Object> filteredParameters) {
        throw new NotImplementedException("TODO");
    }

    public <T> CompletableFuture<T> get(WebDriverAddress address,
            Class<T> type) {
        Map<String, Object> parameters = new HashMap<>();
        return get(address, parameters, type);
    }

    public <T> CompletableFuture<T> get(WebDriverAddress address,
            ParameterizedTypeReference<T> type) {
        throw new NotImplementedException("TODO");
    }

    WebDriver getWebDriver() {
        return webDriver;
    }

    public CompletableFuture<EntityWrapper<?>> get(WebDriverAddress address) {
        ParameterizedTypeReference<EntityWrapper<?>> type = new ParameterizedTypeReference<EntityWrapper<?>>() {
        };
        Map<String, Object> parameters = new HashMap<>();
        return get(address, parameters, type);
    }

    private CompletableFuture<EntityWrapper<?>> get(WebDriverAddress address,
            Map<String, Object> parameters,
            ParameterizedTypeReference<EntityWrapper<?>> type) {
        return CompletableFuture.supplyAsync(() -> {
            submitForm(address, parameters);
            return createProxy(type);
        });

    }

    private EntityWrapper<?> createProxy(
            ParameterizedTypeReference<EntityWrapper<?>> type) {
        final Enhancer e = initEnhancer(EntityWrapper.class);
        final EntityWrapper<?> proxy = (EntityWrapper<?>) e
                .create(new Class[] {}, new Object[] {});
        return proxy;

    }

}

package au.com.mountainpass.hyperstate.client.webdriver;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.core.ParameterizedTypeReference;

import com.google.common.collect.ImmutableSet;

import au.com.mountainpass.hyperstate.client.CreateAction;
import au.com.mountainpass.hyperstate.client.GetAction;
import au.com.mountainpass.hyperstate.core.Action;
import au.com.mountainpass.hyperstate.core.Address;
import au.com.mountainpass.hyperstate.core.EntityRelationship;
import au.com.mountainpass.hyperstate.core.Link;
import au.com.mountainpass.hyperstate.core.NavigationalRelationship;
import au.com.mountainpass.hyperstate.core.Resolver;
import au.com.mountainpass.hyperstate.core.entities.CreatedEntity;
import au.com.mountainpass.hyperstate.core.entities.Entity;
import au.com.mountainpass.hyperstate.core.entities.EntityWrapper;
import au.com.mountainpass.hyperstate.core.entities.LinkedEntity;
import au.com.mountainpass.hyperstate.core.entities.UpdatedEntity;

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

    public CompletableFuture<CreatedEntity> create(final WebDriverLink link,
            final Map<String, Object> filteredParameters) {
        return CompletableFuture.supplyAsync(() -> {
            final WebElement form = (new WebDriverWait(webDriver, 5))
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
            final CreatedEntity linkedEntity = new CreatedEntity(
                    new WebDriverLink(this,
                            webDriver.findElement(By.tagName("html"))));
            return linkedEntity;
        });
    }

    <E> E createProxy(final Class<E> klass) {
        final Enhancer e = initEnhancer(klass);
        @SuppressWarnings("unchecked")
        final E proxy = (E) e.create(new Class[] {}, new Object[] {});
        return proxy;
    }

    public CompletableFuture<Void> delete(final Address link,
            final Map<String, Object> filteredParameters) {
        throw new NotImplementedException("TODO");
    }

    public <T> CompletableFuture<T> get(final WebDriverLink link,
            final Map<String, Object> filteredParameters, Class<T> type) {
        if (link instanceof WebDriverLink) {
            final WebDriverLink wdl = link;
            final WebElement form = wdl.getWebElement();
            if ("form".equals(form.getTagName())) {
                for (final WebElement input : form
                        .findElements(By.name("input"))) {
                    final Object value = filteredParameters
                            .get(input.getAttribute("name"));
                    if (value != null) {
                        input.sendKeys(value.toString());
                    }
                }
                form.findElement(By.cssSelector("button[type='submit']"))
                        .click();
            } else {
                form.click();
            }
            return CompletableFuture.supplyAsync(() -> {
                return createProxy(type);
            });
        } else {
            throw new NotImplementedException("TODO");
        }
    }

    @Override
    public <E extends EntityWrapper<?>> CompletableFuture<E> get(
            final String path, final Class<E> type) {
        return get(getBaseUri().resolve(path), type);
    }

    public <T> CompletableFuture<T> get(final URI uri, final Class<T> klass) {

        return CompletableFuture.supplyAsync(() -> {
            webDriver.get(uri.toString());
            final T proxy = createProxy(klass);
            return proxy;
        });
    }

    private URI getBaseUri() {
        return baseUri;
    }

    public String getUrl() {
        return webDriver.getCurrentUrl();
    }

    <E> Enhancer initEnhancer(final Class<E> klass) {
        final Enhancer e = new Enhancer();

        final WebDriverResolver resolver = this;

        e.setClassLoader(this.getClass().getClassLoader());
        e.setSuperclass(klass);
        e.setCallback(new MethodInterceptor() {
            private Action<?> getAction(final WebDriverResolver resolver,
                    final WebElement form) {
                final List<WebElement> inputs = form
                        .findElements(By.tagName("input"));

                final au.com.mountainpass.hyperstate.core.Parameter[] fields = new au.com.mountainpass.hyperstate.core.Parameter[inputs
                        .size()];
                for (int i = 0; i < inputs.size(); ++i) {
                    final String type = inputs.get(i).getAttribute("type");
                    final String value = inputs.get(i).getAttribute("value");
                    final String name = inputs.get(i).getAttribute("name");

                    fields[i] = new au.com.mountainpass.hyperstate.core.Parameter(
                            name, type, value);
                }

                switch (form.getAttribute("method")) {
                case "get":
                    return new GetAction(form.getAttribute("name"),
                            new WebDriverAddress(resolver, form), fields);
                case "post":
                    return new CreateAction(form.getAttribute("name"),
                            new WebDriverAddress(resolver, form), fields);
                default:

                    throw new NotImplementedException("unimplemented method: "
                            + form.getAttribute("method"));
                }
            }

            private Object getActions(final WebDriverResolver resolver) {
                waitTillLoaded(5);

                return ImmutableSet
                        .copyOf(webDriver.findElement(By.id("actions"))
                                .findElements(By.tagName("form")).stream()
                                .map(we -> getAction(resolver, we))
                                .collect(Collectors.toSet()));
            }

            private Link getLink(final WebDriverResolver resolver,
                    final WebElement a) {
                return new WebDriverLink(resolver, a);
            }

            private ImmutableSet<NavigationalRelationship> getLinks(
                    final WebDriverResolver resolver) {
                waitTillLoaded(5);

                return ImmutableSet.copyOf(webDriver.findElement(By.id("links"))
                        .findElements(By.tagName("a")).stream()
                        .map(we -> getNavigationalRelationship(resolver, we))
                        .collect(Collectors.toSet()));
            }

            private Object getClasses() {
                waitTillLoaded(5);

                final HashSet<String> rval = new HashSet<String>(
                        Arrays.asList(webDriver.findElement(By.tagName("html"))
                                .getAttribute("class").split("\\s+")));
                return rval;
            }

            private NavigationalRelationship getNavigationalRelationship(
                    final WebDriverResolver resolver, final WebElement a) {
                return new NavigationalRelationship(getLink(resolver, a),
                        a.getAttribute("rel").split("\\s+"));
            }

            @Override
            public Object intercept(final Object obj, final Method method,
                    final Object[] args, final MethodProxy methodProxy)
                            throws Throwable {

                if (method.getName().equals("getAction")) {
                    waitTillLoaded(5);

                    final String actionName = args[0].toString();
                    return getAction(resolver,
                            webDriver.findElement(By.name(actionName)));

                } else if (method.getName().equals("reload")) {
                    webDriver.get(webDriver.getCurrentUrl());
                    return createProxy((Class<E>) args[0]);
                } else if (method.getName().equals("getEntities")) {
                    final List<WebElement> entities = webDriver.findElements(
                            By.cssSelector("#entities > div.row > a"));
                    final Collection<EntityRelationship> rval = new ArrayList<EntityRelationship>();
                    for (final WebElement entity : entities) {
                        final String[] classes = entity.getAttribute("class")
                                .split("\\s");
                        final String title = entity.getText();
                        final String[] entityRelationships = new String[0];
                        // TODO read entity relationships from page
                        rval.add(new EntityRelationship(new LinkedEntity(
                                new WebDriverLink(resolver, entity), title,
                                entityRelationships), classes));
                    }
                    return rval;
                } else if (method.getName().equals("getProperties")) {
                    final Object instance = methodProxy.invokeSuper(obj,
                            new Object[] {});
                    final Enhancer propertiesEnhancer = new Enhancer();
                    propertiesEnhancer.setClassLoader(
                            instance.getClass().getClassLoader());
                    propertiesEnhancer.setSuperclass(instance.getClass());
                    propertiesEnhancer.setCallback(new MethodInterceptor() {

                        @Override
                        public Object intercept(final Object properties,
                                final Method propertiesMethod,
                                final Object[] propertiesMethodArgs,
                                final MethodProxy propertiesMethodProxy)
                                        throws Throwable {
                            String key = propertiesMethod.getName()
                                    .replaceFirst("^get", "")
                                    .replaceFirst("^is", "");
                            key = "property:"
                                    + key.substring(0, 1).toLowerCase()
                                    + key.substring(1);
                            final String value = webDriver
                                    .findElement(By.id(key)).getText();
                            if (propertiesMethod.getReturnType()
                                    .isAssignableFrom(String.class)) {
                                return value;
                            } else if (propertiesMethod.getReturnType()
                                    .isAssignableFrom(boolean.class)) {
                                return Boolean.parseBoolean(value);
                            } else {
                                throw new NotImplementedException(
                                        "conversion not implemented for "
                                                + propertiesMethod
                                                        .getReturnType());
                            }
                        }
                    });

                    return propertiesEnhancer.create(new Class[] {},
                            new Object[] {});

                } else if (method.getName().equals("getNatures")) {

                    return getClasses();

                } else if (method.getName().equals("getActions")) {

                    return getActions(resolver);

                } else if (method.getName().equals("getLinks")) {

                    return getLinks(resolver);

                } else if (method.getName().equals("getLink")) {
                    final Optional<NavigationalRelationship> link = getLinks(
                            resolver)
                                    .stream()
                                    .filter(l -> l
                                            .hasRelationship((String) args[0]))
                                    .findAny();
                    if (link.isPresent()) {
                        return link.get().getLink();
                    } else {
                        return null;
                    }
                } else if (method.getName().equals("toLinkedEntity")
                        || method.getName().equals("getProperties")
                        || method.getName().equals("getTitle")
                        || method.getName().equals("getLink")) {
                    throw new NotImplementedException(
                            "method: " + method.getName());
                } else {
                    final Map<String, Object> context = new HashMap<>();

                    final Parameter[] params = method.getParameters();
                    for (int i = 0; i < params.length; ++i) {
                        context.put(params[i].getName(), args[i]);
                    }

                    final Action<?> action = ((EntityWrapper<?>) obj)
                            .getAction(method.getName());
                    if (action == null) {
                        throw new RuntimeException(
                                "The method `" + method.getName()
                                        + "` cannot be executed remotely");
                    } else {
                        @SuppressWarnings("unchecked")
                        final CompletableFuture<Entity> result = (CompletableFuture<Entity>) action
                                .invoke(context);
                        return result;
                    }
                }

                // WebElement form = (new WebDriverWait(webDriver, 5))
                // .until(ExpectedConditions.presenceOfElementLocated(
                // By.name(method.getName())));
                //
                // Parameter[] params = method.getParameters();
                // for (int i = 0; i < params.length; ++i) {
                // WebElement input = form
                // .findElement(By.name(params[i].getName()));
                // input.sendKeys(args[i].toString());
                // }
                // return CompletableFuture.supplyAsync(() -> {
                // form.findElement(By.cssSelector("button[type='submit']"))
                // .click();
                // Entity result = null;
                // return result;
                // });
            }

            private void waitTillLoaded(final long timeoutInSeconds) {
                (new WebDriverWait(webDriver, timeoutInSeconds))
                        .until(ExpectedConditions.invisibilityOfElementLocated(
                                By.id("loading")));
                (new WebDriverWait(webDriver, timeoutInSeconds))
                        .until(ExpectedConditions
                                .visibilityOfElementLocated(By.id("loaded")));
                (new WebDriverWait(webDriver, timeoutInSeconds)).until(
                        WebDriverResolver.angularHasFinishedProcessing());
            }

        });
        return e;
    }

    public CompletableFuture<UpdatedEntity> update(final WebDriverLink link,
            final Map<String, Object> filteredParameters) {
        throw new NotImplementedException("TODO");
    }

    public <T> CompletableFuture<T> get(WebDriverLink link, Class<T> type) {
        Map<String, Object> parameters = new HashMap<>();
        return get(link, parameters, type);
    }

    public <T> CompletableFuture<T> get(WebDriverLink link,
            ParameterizedTypeReference<T> type) {
        throw new NotImplementedException("TODO");
    }

}

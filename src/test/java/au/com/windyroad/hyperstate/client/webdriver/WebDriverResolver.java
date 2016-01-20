package au.com.windyroad.hyperstate.client.webdriver;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.AsyncRestTemplate;

import au.com.windyroad.hyperstate.client.CreateAction;
import au.com.windyroad.hyperstate.client.GetAction;
import au.com.windyroad.hyperstate.core.Action;
import au.com.windyroad.hyperstate.core.EntityRelationship;
import au.com.windyroad.hyperstate.core.Link;
import au.com.windyroad.hyperstate.core.Resolver;
import au.com.windyroad.hyperstate.core.entities.CreatedEntity;
import au.com.windyroad.hyperstate.core.entities.Entity;
import au.com.windyroad.hyperstate.core.entities.EntityWrapper;
import au.com.windyroad.hyperstate.core.entities.UpdatedEntity;
import au.com.windyroad.hyperstate.server.HyperstateTestConfiguration;
import cucumber.api.PendingException;

@Component
@Profile("ui-integration")
@Primary
public class WebDriverResolver implements Resolver {

    @Autowired
    private AsyncRestTemplate restTemplate;

    @Autowired
    private WebDriver webDriver;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    HyperstateTestConfiguration config;

    @Override
    public CompletableFuture<CreatedEntity> create(Link link,
            Map<String, Object> filteredParameters) {
        return CompletableFuture.supplyAsync(() -> {
            WebElement form = (new WebDriverWait(webDriver, 5))
                    .until(ExpectedConditions.presenceOfElementLocated(By
                            .name((String) filteredParameters.get("action"))));

            List<WebElement> inputs = form.findElements(By.tagName("input"));
            for (WebElement input : inputs) {
                String inputName = input.getAttribute("name");
                if (inputName != null) {
                    Object value = filteredParameters.get(inputName);
                    if (value != null) {
                        input.sendKeys(value.toString());
                    }
                }
            }
            form.findElement(By.cssSelector("button[type='submit']")).click();
            CreatedEntity linkedEntity = new CreatedEntity(new WebDriverLink(
                    this, webDriver.findElement(By.tagName("html"))));
            return linkedEntity;
        });
    }

    @Override
    public CompletableFuture<Void> delete(Link link,
            Map<String, Object> filteredParameters) {
        throw new PendingException();
    }

    @Override
    public CompletableFuture<EntityWrapper<?>> get(Link link,
            Map<String, Object> filteredParameters) {
        if (link instanceof WebDriverLink) {
            WebDriverLink wdl = (WebDriverLink) link;
            WebElement form = wdl.getWebElement();
            for (WebElement input : form.findElements(By.name("input"))) {
                Object value = filteredParameters
                        .get(input.getAttribute("name"));
                if (value != null) {
                    input.sendKeys(value.toString());
                }
            }
            form.findElement(By.cssSelector("button[type='submit']")).click();
            return CompletableFuture.supplyAsync(() -> {
                EntityWrapper<?> wrapper = createProxy(EntityWrapper.class);
                return wrapper;
            });

        } else {
            throw new PendingException();
        }
    }

    @Override
    public CompletableFuture<UpdatedEntity> update(Link link,
            Map<String, Object> filteredParameters) {
        throw new PendingException();
    }

    public <T> CompletableFuture<T> get(URI uri, Class<T> klass) {

        return CompletableFuture.supplyAsync(() -> {
            webDriver.get(uri.toString());
            T proxy = createProxy(klass);
            return proxy;
        });
    }

    @Override
    public <E extends EntityWrapper<?>> CompletableFuture<E> get(String path,
            Class<E> type) {
        return get(config.getBaseUri().resolve(path), type);
    }

    <E> E createProxy(Class<E> klass) {
        Enhancer e = initEnhancer(klass);
        @SuppressWarnings("unchecked")
        E proxy = (E) e.create(new Class[] {}, new Object[] {});
        return proxy;
    }

    <E> Enhancer initEnhancer(Class<E> klass) {
        Enhancer e = new Enhancer();

        WebDriverResolver resolver = this;

        e.setClassLoader(this.getClass().getClassLoader());
        e.setSuperclass(klass);
        e.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object obj, Method method, Object[] args,
                    MethodProxy methodProxy) throws Throwable {

                if (method.getName().equals("getAction")) {
                    String actionName = args[0].toString();
                    WebElement form = (new WebDriverWait(webDriver, 5))
                            .until(ExpectedConditions.presenceOfElementLocated(
                                    By.name(actionName)));

                    List<WebElement> inputs = form
                            .findElements(By.tagName("input"));

                    au.com.windyroad.hyperstate.core.Parameter[] fields = new au.com.windyroad.hyperstate.core.Parameter[inputs
                            .size()];
                    for (int i = 0; i < inputs.size(); ++i) {
                        String type = inputs.get(i).getAttribute("type");
                        String value = inputs.get(i).getAttribute("value");
                        String name = inputs.get(i).getAttribute("name");

                        fields[i] = new au.com.windyroad.hyperstate.core.Parameter(
                                name, type, value);
                    }

                    switch (form.getAttribute("method")) {
                    case "get":
                        return new GetAction(resolver, actionName,
                                new WebDriverLink(form), fields);
                    case "post":
                        return new CreateAction(resolver, actionName,
                                new WebDriverLink(form), fields);
                    default:
                        throw new PendingException("unimplemented method: "
                                + form.getAttribute("method"));
                    }

                } else if (method.getName().equals("reload")) {
                    webDriver.get(webDriver.getCurrentUrl());
                    return createProxy((Class<E>) args[0]);
                } else if (method.getName().equals("getEntities")) {
                    List<WebElement> entities = webDriver.findElements(
                            By.cssSelector("#entities > div.row > a"));
                    Collection<EntityRelationship> rval = new ArrayList<EntityRelationship>();
                    for (WebElement entity : entities) {
                        String[] classes = entity.getAttribute("class")
                                .split("\\s");
                        String title = entity.getText();
                        rval.add(new EntityRelationship(
                                new WebDriverLink(resolver, entity), title,
                                classes));
                    }
                    return rval;
                } else if (method.getName().equals("getProperties")) {
                    Object instance = methodProxy.invokeSuper(obj,
                            new Object[] {});
                    Enhancer propertiesEnhancer = new Enhancer();
                    propertiesEnhancer.setClassLoader(
                            instance.getClass().getClassLoader());
                    propertiesEnhancer.setSuperclass(instance.getClass());
                    propertiesEnhancer.setCallback(new MethodInterceptor() {

                        @Override
                        public Object intercept(Object properties,
                                Method propertiesMethod,
                                Object[] propertiesMethodArgs,
                                MethodProxy propertiesMethodProxy)
                                        throws Throwable {
                            String key = propertiesMethod.getName()
                                    .toLowerCase()
                                    .replaceFirst("^get", "property:")
                                    .replaceFirst("^is", "property:");
                            String value = webDriver.findElement(By.id(key))
                                    .getText();
                            if (propertiesMethod.getReturnType()
                                    .isAssignableFrom(String.class)) {
                                return value;
                            } else if (propertiesMethod.getReturnType()
                                    .isAssignableFrom(boolean.class)) {
                                return Boolean.parseBoolean(value);
                            } else {
                                throw new PendingException(
                                        "conversion not implemented for "
                                                + propertiesMethod
                                                        .getReturnType());
                            }
                        }
                    });

                    return propertiesEnhancer.create(new Class[] {},
                            new Object[] {});

                } else if (method.getName().equals("toLinkedEntity")
                        || method.getName().equals("getProperties")
                        || method.getName().equals("getTitle    ")
                        || method.getName().equals("getNatures")
                        || method.getName().equals("getLinks")
                        || method.getName().equals("getLink")) {
                    throw new PendingException(method.getName());
                } else {
                    Map<String, Object> context = new HashMap<>();

                    Parameter[] params = method.getParameters();
                    for (int i = 0; i < params.length; ++i) {
                        context.put(params[i].getName(), args[i]);
                    }

                    Action<?> action = ((EntityWrapper<?>) obj)
                            .getAction(method.getName());
                    if (action == null) {
                        throw new RuntimeException(
                                "The method `" + method.getName()
                                        + "` cannot be executed remotely");
                    } else {
                        @SuppressWarnings("unchecked")
                        CompletableFuture<Entity> result = (CompletableFuture<Entity>) action
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
        });
        return e;
    }

    public String getUrl() {
        return webDriver.getCurrentUrl();
    }

}

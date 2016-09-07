package au.com.mountainpass.hyperstate.client.webdriver;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import com.google.common.collect.ImmutableSet;

import au.com.mountainpass.hyperstate.client.CreateAction;
import au.com.mountainpass.hyperstate.client.DeleteAction;
import au.com.mountainpass.hyperstate.client.GetAction;
import au.com.mountainpass.hyperstate.client.UpdateAction;
import au.com.mountainpass.hyperstate.core.Action;
import au.com.mountainpass.hyperstate.core.EntityRelationship;
import au.com.mountainpass.hyperstate.core.Link;
import au.com.mountainpass.hyperstate.core.NavigationalRelationship;
import au.com.mountainpass.hyperstate.core.entities.Entity;
import au.com.mountainpass.hyperstate.core.entities.EntityWrapper;
import au.com.mountainpass.hyperstate.core.entities.LinkedEntity;

class WebDriverEntityInterceptor<E> implements MethodInterceptor {
    private final WebDriverResolver resolver;

    WebDriverEntityInterceptor(WebDriverResolver resolver) {
        this.resolver = resolver;
    }

    private Action<?> getAction(final WebDriverResolver resolver,
            final WebElement form) {
        final List<WebElement> inputs = form.findElements(By.tagName("input"));

        final au.com.mountainpass.hyperstate.core.Parameter[] fields = new au.com.mountainpass.hyperstate.core.Parameter[inputs
                .size()];
        for (int i = 0; i < inputs.size(); ++i) {
            final String type = inputs.get(i).getAttribute("type");
            final String value = inputs.get(i).getAttribute("value");
            final String name = inputs.get(i).getAttribute("name");

            fields[i] = new au.com.mountainpass.hyperstate.core.Parameter(name,
                    type, value);
        }
        // form.getAttribute("method") is returning 'get' event if the method is
        // 'DELETE' :/
        // so we do it this way instead.
        if (!form.findElements(By.xpath(".[@method='GET']")).isEmpty()) {
            return new GetAction(form.getAttribute("name"),
                    new WebDriverAddress(resolver, form), fields);
        } else
            if (!form.findElements(By.xpath(".[@method='POST']")).isEmpty()) {
            return new CreateAction(form.getAttribute("name"),
                    new WebDriverAddress(resolver, form), fields);
        } else if (!form.findElements(By.xpath(".[@method='PUT']")).isEmpty()) {
            return new UpdateAction(form.getAttribute("name"),
                    new WebDriverAddress(resolver, form), fields);
        } else
            if (!form.findElements(By.xpath(".[@method='DELETE']")).isEmpty()) {
            return new DeleteAction(form.getAttribute("name"),
                    new WebDriverAddress(resolver, form), fields);
        } else {
            throw new NotImplementedException("unimplemented method for from: "
                    + form.getAttribute("name"));
        }
    }

    private Object getActions(final WebDriverResolver resolver) {
        waitTillLoaded(5);

        Set<?> acctions = resolver.getWebDriver().findElement(By.id("actions"))
                .findElements(By.tagName("form")).stream()
                .map(we -> getAction(resolver, we)).collect(Collectors.toSet());
        return ImmutableSet.copyOf(acctions);
    }

    private Link getLink(final WebDriverResolver resolver, final WebElement a) {
        return new Link(new WebDriverAddress(resolver, a), a.getText(),
                a.getAttribute("class").split("\\s"));
    }

    private ImmutableSet<NavigationalRelationship> getLinks(
            final WebDriverResolver resolver) {
        waitTillLoaded(5);

        return ImmutableSet.copyOf(resolver.getWebDriver()
                .findElement(By.id("links")).findElements(By.tagName("a"))
                .stream().map(we -> getNavigationalRelationship(resolver, we))
                .collect(Collectors.toSet()));
    }

    private ImmutableSet<String> getClasses() {
        waitTillLoaded(5);

        final HashSet<String> rval = new HashSet<String>(Arrays
                .asList(resolver.getWebDriver().findElement(By.tagName("html"))
                        .getAttribute("class").split("\\s+")));
        // TODO prefix "status" to avoid name conflicts
        return ImmutableSet.copyOf(
                rval.stream().filter(entry -> !entry.startsWith("status"))
                        .collect(Collectors.toSet()));
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
            List<WebElement> actions = resolver.getWebDriver()
                    .findElements(By.name(actionName));
            if (actions.isEmpty()) {
                return null;
            }
            return getAction(resolver, actions.get(0));

        } else if (method.getName().equals("reload")) {
            resolver.getWebDriver()
                    .get(resolver.getWebDriver().getCurrentUrl());
            return resolver.createProxy((Class<E>) args[0]);
        } else if (method.getName().equals("resolve")) {
            return CompletableFuture.supplyAsync(() -> {
                return resolver.createProxy((Class<?>) args[0]);
            });
        } else if (method.getName().equals("getEntities")) {
            final List<WebElement> entities = resolver.getWebDriver()
                    .findElements(By.cssSelector("#entities > div.row > a"));
            final Collection<EntityRelationship> rval = new ArrayList<EntityRelationship>();
            for (final WebElement entity : entities) {
                final Set<String> classes = new HashSet<>(Arrays
                        .asList(entity.getAttribute("class").split("\\s")));
                final String title = entity.getText();
                final String[] rels = new String[0];
                // TODO read entity relationships from page
                WebDriverAddress webDriverAddress = new WebDriverAddress(
                        resolver, entity);
                Link link = new Link(webDriverAddress, title);
                LinkedEntity linkedEntity = new LinkedEntity(link, title,
                        classes);
                rval.add(new EntityRelationship(linkedEntity, rels));
            }
            return rval;
        } else if (method.getName().equals("getProperties")) {
            final Object instance = methodProxy.invokeSuper(obj,
                    new Object[] {});
            final Enhancer propertiesEnhancer = new Enhancer();
            propertiesEnhancer
                    .setClassLoader(instance.getClass().getClassLoader());
            propertiesEnhancer.setSuperclass(instance.getClass());
            propertiesEnhancer.setCallback(new MethodInterceptor() {

                // todo: use an object mapper of similar here to perform the
                // conversion from string to desired type.
                @Override
                public Object intercept(final Object properties,
                        final Method propertiesMethod,
                        final Object[] propertiesMethodArgs,
                        final MethodProxy propertiesMethodProxy)
                                throws Throwable {
                    String key = propertiesMethod.getName();
                    if ("get".equals(key)) {
                        key = propertiesMethodArgs[0].toString();
                    } else {
                        key = key.replaceFirst("^get", "").replaceFirst("^is",
                                "");
                        key = key.substring(0, 1).toLowerCase()
                                + key.substring(1);
                    }
                    key = "property:" + key;

                    final String value = WebDriverEntityInterceptor.this.resolver
                            .getWebDriver().findElement(By.id(key)).getText();
                    if (propertiesMethod.getReturnType()
                            .isAssignableFrom(String.class)) {
                        return value;
                    } else if (propertiesMethod.getReturnType()
                            .isAssignableFrom(boolean.class)) {
                        return Boolean.parseBoolean(value);
                    } else if (propertiesMethod.getReturnType()
                            .isAssignableFrom(LocalDateTime.class)) {
                        return LocalDateTime.parse(value);
                    } else {
                        throw new NotImplementedException(
                                "conversion not implemented for "
                                        + propertiesMethod.getReturnType());
                    }
                }
            });

            return propertiesEnhancer.create(new Class[] {}, new Object[] {});

        } else if (method.getName().equals("getProperty")) {
            String key = "property:" + (String) args[0];
            String value = resolver.getWebDriver().findElement(By.id(key))
                    .getText();
            return value;

        } else if (method.getName().equals("getClasses")) {

            return getClasses();

        } else if (method.getName().equals("getActions")) {

            return getActions(resolver);

        } else if (method.getName().equals("getLinks")) {

            return getLinks(resolver);

        } else if (method.getName().equals("getLink")) {
            final Optional<NavigationalRelationship> link = getLinks(resolver)
                    .stream().filter(l -> l.hasRelationship((String) args[0]))
                    .findAny();
            if (link.isPresent()) {
                return link.get().getLink();
            } else {
                return null;
            }
        } else if (method.getName().equals("toLinkedEntity")
                || method.getName().equals("getTitle")) {
            throw new NotImplementedException("method: " + method.getName());
        } else if (method.getName().equals("toString")
                || method.getName().equals("hashCode")) {
            return methodProxy.invokeSuper(obj, new Object[] {});
        } else {
            final Map<String, Object> context = new HashMap<>();

            final Parameter[] params = method.getParameters();
            for (int i = 0; i < params.length; ++i) {
                context.put(params[i].getName(), args[i]);
            }

            final Action<?> action = ((EntityWrapper<?>) obj)
                    .getAction(method.getName());
            if (action == null) {
                throw new IllegalAccessException("The method `"
                        + method.getName() + "` cannot be executed remotely");
            } else {
                @SuppressWarnings("unchecked")
                final CompletableFuture<Entity> result = (CompletableFuture<Entity>) action
                        .invoke(context);
                return result;
            }
        }
    }

    private void waitTillLoaded(final long timeoutInSeconds) {
        (new WebDriverWait(resolver.getWebDriver(), timeoutInSeconds))
                .until(ExpectedConditions
                        .invisibilityOfElementLocated(By.id("loading")));
        (new WebDriverWait(resolver.getWebDriver(), timeoutInSeconds)).until(
                ExpectedConditions.visibilityOfElementLocated(By.id("loaded")));
        (new WebDriverWait(resolver.getWebDriver(), timeoutInSeconds))
                .until(WebDriverResolver.angularHasFinishedProcessing());
    }
}
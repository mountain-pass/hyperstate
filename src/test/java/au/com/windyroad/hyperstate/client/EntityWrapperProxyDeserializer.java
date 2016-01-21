package au.com.windyroad.hyperstate.client;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.context.ApplicationContext;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.DelegatingDeserializer;

import au.com.windyroad.hyperstate.core.Action;
import au.com.windyroad.hyperstate.core.entities.Entity;
import au.com.windyroad.hyperstate.core.entities.EntityWrapper;

public class EntityWrapperProxyDeserializer extends DelegatingDeserializer {

    /**
     * 
     */
    private static final long serialVersionUID = 5929494436284359667L;

    ApplicationContext context;

    public EntityWrapperProxyDeserializer(ApplicationContext context,
            JsonDeserializer<?> delegatee) {
        super(delegatee);
        this.context = context;
    }

    @Override
    public Object deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        Object result = super.deserialize(jp, ctxt);

        if (EntityWrapper.class.isAssignableFrom(result.getClass())) {

            Enhancer e = new Enhancer();
            e.setClassLoader(this.getClass().getClassLoader());
            e.setSuperclass(result.getClass());
            e.setCallback(new MethodInterceptor() {
                @Override
                public Object intercept(Object obj, Method method,
                        Object[] args, MethodProxy proxy) throws Throwable {

                    if (method.getName().equals("getAction")
                            || method.getName().equals("getActions")
                            || method.getName().equals("toLinkedEntity")
                            || method.getName().equals("getEntities")
                            || method.getName().equals("getProperties")
                            || method.getName().equals("getTitle")
                            || method.getName().equals("getNatures")
                            || method.getName().equals("getLinks")
                            || method.getName().equals("getLink")) {
                        return proxy.invokeSuper(obj, args);
                    } else if (method.getName().equals("reload")) {
                        return ((EntityWrapper<?>) obj).getLink("self")
                                .resolve((Class<?>) args[0]);
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
                }
            });
            EntityWrapper<?> myProxy = (EntityWrapper<?>) e.create(
                    new Class[] { result.getClass() }, new Object[] { result });
            // AutowiredAnnotationBeanPostProcessor bpp = new
            // AutowiredAnnotationBeanPostProcessor();
            // bpp.setBeanFactory(context.getAutowireCapableBeanFactory());
            // bpp.processInjection(myProxy);
            //
            return myProxy;
        }

        return result;

    }

    @Override
    protected JsonDeserializer<?> newDelegatingInstance(
            JsonDeserializer<?> newDelegatee) {
        return this;
    }

}
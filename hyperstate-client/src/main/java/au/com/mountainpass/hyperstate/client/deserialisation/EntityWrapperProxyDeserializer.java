package au.com.mountainpass.hyperstate.client.deserialisation;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.context.ApplicationContext;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.DelegatingDeserializer;

import au.com.mountainpass.hyperstate.core.Action;
import au.com.mountainpass.hyperstate.core.entities.EntityWrapper;

public class EntityWrapperProxyDeserializer extends DelegatingDeserializer {

    /**
     * 
     */
    private static final long serialVersionUID = 5929494436284359667L;

    ApplicationContext context;

    public EntityWrapperProxyDeserializer(final ApplicationContext context,
            final JsonDeserializer<?> delegatee) {
        super(delegatee);
        this.context = context;
    }

    @Override
    public Object deserialize(final JsonParser jp,
            final DeserializationContext ctxt)
                    throws IOException, JsonProcessingException {
        final Object result = super.deserialize(jp, ctxt);

        if (EntityWrapper.class.isAssignableFrom(result.getClass())) {

            final Enhancer e = new Enhancer();
            e.setClassLoader(this.getClass().getClassLoader());
            e.setSuperclass(result.getClass());
            e.setCallback(new MethodInterceptor() {
                @SuppressWarnings("unchecked")
                @Override
                public Object intercept(final Object obj, final Method method,
                        final Object[] args, final MethodProxy proxy)
                                throws Throwable {

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
                        return ((EntityWrapper<?>) obj).getLink("self").get();
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
                            return action.invoke(context);
                        }
                    }
                }
            });
            return e.create(new Class[] { result.getClass() },
                    new Object[] { result });
        }

        return result;

    }

    @Override
    protected JsonDeserializer<?> newDelegatingInstance(
            final JsonDeserializer<?> newDelegatee) {
        return this;
    }

}
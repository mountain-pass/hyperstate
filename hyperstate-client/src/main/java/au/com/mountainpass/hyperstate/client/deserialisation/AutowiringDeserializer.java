package au.com.mountainpass.hyperstate.client.deserialisation;

import java.io.IOException;

import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.context.ApplicationContext;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.DelegatingDeserializer;

public class AutowiringDeserializer extends DelegatingDeserializer {
    /**
     *
     *
     */
    private static final long serialVersionUID = 5929494436284359667L;

    private ApplicationContext context;

    public AutowiringDeserializer(final ApplicationContext context,
            final JsonDeserializer<?> delegatee) {
        super(delegatee);
        this.context = context;
    }

    @Override
    public Object deserialize(final JsonParser jp,
            final DeserializationContext ctxt) throws IOException {
        final Object result = super.deserialize(jp, ctxt);

        final AutowiredAnnotationBeanPostProcessor bpp = new AutowiredAnnotationBeanPostProcessor();
        bpp.setBeanFactory(context.getAutowireCapableBeanFactory());
        bpp.processInjection(result);

        return result;
    }

    @Override
    protected JsonDeserializer<?> newDelegatingInstance(
            final JsonDeserializer<?> newDelegatee) {
        return this;
    }

}
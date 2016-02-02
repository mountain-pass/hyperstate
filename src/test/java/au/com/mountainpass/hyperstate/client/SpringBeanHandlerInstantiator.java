package au.com.mountainpass.hyperstate.client;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;

@Component
public class SpringBeanHandlerInstantiator extends HandlerInstantiator {

    private ApplicationContext applicationContext;

    @Autowired
    public SpringBeanHandlerInstantiator(
            ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    private Object findInstance(Class<?> type) {

        return BeanUtils.instantiateClass(type);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.fasterxml.jackson.databind.cfg.HandlerInstantiator#
     * deserializerInstance(com.fasterxml.jackson.databind.
     * DeserializationConfig,
     * com.fasterxml.jackson.databind.introspect.Annotated, java.lang.Class)
     */
    @Override
    public JsonDeserializer<?> deserializerInstance(
            DeserializationConfig config, Annotated annotated,
            Class<?> deserClass) {
        return (JsonDeserializer<?>) findInstance(deserClass);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.fasterxml.jackson.databind.cfg.HandlerInstantiator#
     * keyDeserializerInstance(com.fasterxml.jackson.databind.
     * DeserializationConfig,
     * com.fasterxml.jackson.databind.introspect.Annotated, java.lang.Class)
     */
    @Override
    public KeyDeserializer keyDeserializerInstance(DeserializationConfig config,
            Annotated annotated, Class<?> keyDeserClass) {
        return (KeyDeserializer) findInstance(keyDeserClass);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.fasterxml.jackson.databind.cfg.HandlerInstantiator#serializerInstance
     * (com.fasterxml.jackson.databind.SerializationConfig,
     * com.fasterxml.jackson.databind.introspect.Annotated, java.lang.Class)
     */
    @Override
    public JsonSerializer<?> serializerInstance(SerializationConfig config,
            Annotated annotated, Class<?> serClass) {
        return (JsonSerializer<?>) findInstance(serClass);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.fasterxml.jackson.databind.cfg.HandlerInstantiator#
     * typeResolverBuilderInstance(com.fasterxml.jackson.databind.cfg.
     * MapperConfig, com.fasterxml.jackson.databind.introspect.Annotated,
     * java.lang.Class)
     */
    @Override
    public TypeResolverBuilder<?> typeResolverBuilderInstance(
            MapperConfig<?> config, Annotated annotated,
            Class<?> builderClass) {
        return (TypeResolverBuilder<?>) findInstance(builderClass);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.fasterxml.jackson.databind.cfg.HandlerInstantiator#
     * typeIdResolverInstance(com.fasterxml.jackson.databind.cfg.MapperConfig,
     * com.fasterxml.jackson.databind.introspect.Annotated, java.lang.Class)
     */
    @Override
    public TypeIdResolver typeIdResolverInstance(MapperConfig<?> config,
            Annotated annotated, Class<?> resolverClass) {
        return (TypeIdResolver) findInstance(resolverClass);
    }
}

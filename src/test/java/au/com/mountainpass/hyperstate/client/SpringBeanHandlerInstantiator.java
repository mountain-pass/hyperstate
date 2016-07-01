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

  private final ApplicationContext applicationContext;

  @Autowired
  public SpringBeanHandlerInstantiator(final ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.fasterxml.jackson.databind.cfg.HandlerInstantiator#
   * deserializerInstance(com.fasterxml.jackson.databind. DeserializationConfig,
   * com.fasterxml.jackson.databind.introspect.Annotated, java.lang.Class)
   */
  @Override
  public JsonDeserializer<?> deserializerInstance(final DeserializationConfig config,
      final Annotated annotated, final Class<?> deserClass) {
    return (JsonDeserializer<?>) findInstance(deserClass);
  }

  private Object findInstance(final Class<?> type) {

    return BeanUtils.instantiateClass(type);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.fasterxml.jackson.databind.cfg.HandlerInstantiator#
   * keyDeserializerInstance(com.fasterxml.jackson.databind. DeserializationConfig,
   * com.fasterxml.jackson.databind.introspect.Annotated, java.lang.Class)
   */
  @Override
  public KeyDeserializer keyDeserializerInstance(final DeserializationConfig config,
      final Annotated annotated, final Class<?> keyDeserClass) {
    return (KeyDeserializer) findInstance(keyDeserClass);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.fasterxml.jackson.databind.cfg.HandlerInstantiator#serializerInstance
   * (com.fasterxml.jackson.databind.SerializationConfig,
   * com.fasterxml.jackson.databind.introspect.Annotated, java.lang.Class)
   */
  @Override
  public JsonSerializer<?> serializerInstance(final SerializationConfig config,
      final Annotated annotated, final Class<?> serClass) {
    return (JsonSerializer<?>) findInstance(serClass);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.fasterxml.jackson.databind.cfg.HandlerInstantiator#
   * typeIdResolverInstance(com.fasterxml.jackson.databind.cfg.MapperConfig,
   * com.fasterxml.jackson.databind.introspect.Annotated, java.lang.Class)
   */
  @Override
  public TypeIdResolver typeIdResolverInstance(final MapperConfig<?> config,
      final Annotated annotated, final Class<?> resolverClass) {
    return (TypeIdResolver) findInstance(resolverClass);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.fasterxml.jackson.databind.cfg.HandlerInstantiator#
   * typeResolverBuilderInstance(com.fasterxml.jackson.databind.cfg. MapperConfig,
   * com.fasterxml.jackson.databind.introspect.Annotated, java.lang.Class)
   */
  @Override
  public TypeResolverBuilder<?> typeResolverBuilderInstance(final MapperConfig<?> config,
      final Annotated annotated, final Class<?> builderClass) {
    return (TypeResolverBuilder<?>) findInstance(builderClass);
  }
}

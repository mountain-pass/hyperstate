package au.com.mountainpass.hyperstate.server.config;

import java.net.URI;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.module.SimpleModule;

import au.com.mountainpass.hyperstate.client.SpringBeanHandlerInstantiator;
import au.com.mountainpass.hyperstate.client.deserialisation.AutowiringDeserializer;
import au.com.mountainpass.hyperstate.client.deserialisation.EntityWrapperProxyDeserializer;

@SpringBootApplication
@EnableAsync
@EnableAutoConfiguration
@EnableAspectJAutoProxy
@ComponentScan("au.com.mountainpass.hyperstate")
public class HyperstateTestConfiguration implements
        ApplicationListener<EmbeddedServletContainerInitializedEvent> {

    @Autowired
    private ApplicationContext context;

    public final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Value("${security.user.name:user}")
    private String name;

    @Value("${security.user.password:password}")
    private String password;

    private int port;

    @Value("${au.com.mountainpass.hyperstate.test.ssl.hostname}")
    private String sslHostname;

    public URI getBaseUri() {
        return URI.create("https://" + sslHostname + ":" + getPort());
    }

    public int getPort() {
        return port;
    }

    @Bean(name = "customObjectMapperBuilder")
    @Primary
    @Profile({ "integration", "ui-integration" })
    Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
        final Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.applicationContext(context);
        final HandlerInstantiator handlerInstantiator = new SpringBeanHandlerInstantiator(
                context);
        // context.getAutowireCapableBeanFactory());
        builder.handlerInstantiator(handlerInstantiator);

        final SimpleModule module = new SimpleModule();
        module.setDeserializerModifier(new BeanDeserializerModifier() {
            @Override
            public JsonDeserializer<?> modifyDeserializer(
                    final DeserializationConfig config,
                    final BeanDescription beanDesc,
                    final JsonDeserializer<?> deserializer) {
                return new AutowiringDeserializer(context, deserializer);
            }
        });

        final SimpleModule module2 = new SimpleModule();
        module2.setDeserializerModifier(new BeanDeserializerModifier() {
            @Override
            public JsonDeserializer<?> modifyDeserializer(
                    final DeserializationConfig config,
                    final BeanDescription beanDesc,
                    final JsonDeserializer<?> deserializer) {
                return new EntityWrapperProxyDeserializer(context,
                        deserializer);
            }
        });
        builder.modules(module, module2);

        return builder;
    }

    @Bean
    public LocaleResolver localeResolver() {
        final SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.ENGLISH);
        return slr;
    }

    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:locale/messages");
        messageSource.setCacheSeconds(3600); // refresh cache once per hour
        return messageSource;
    }

    @Override
    public void onApplicationEvent(
            final EmbeddedServletContainerInitializedEvent event) {
        this.port = event.getEmbeddedServletContainer().getPort();
    }

    public void setPort(final int port) {
        this.port = port;
    }

}

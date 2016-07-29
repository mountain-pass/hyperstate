package au.com.mountainpass.hyperstate.server.config;

import org.apache.catalina.startup.Tomcat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import au.com.mountainpass.hyperstate.SelfSignedCertificate;

@Configuration
public class HyperstateServerCertConfig {

    // TODO: check if this is needed
    private static class CustomTomcatEmbeddedServletContainerFactory
            extends TomcatEmbeddedServletContainerFactory {

        public CustomTomcatEmbeddedServletContainerFactory() {
        }

        @Override
        protected TomcatEmbeddedServletContainer getTomcatEmbeddedServletContainer(
                final Tomcat tomcat) {
            return super.getTomcatEmbeddedServletContainer(tomcat);
        }
    }

    @Value("${server.ssl.key-alias}")
    String keyAlias;

    @Value("${server.ssl.key-password}")
    String keyPassword;

    @Value("${server.ssl.key-store}")
    String keyStore;

    @Value("${server.ssl.key-store-password}")
    String keyStorePassword;

    @Value("${au.com.mountainpass.hyperstate.test.ssl.hostname}")
    String sslHostname;

    @Bean
    SelfSignedCertificate selfSignedCertificate() throws Exception {
        return new SelfSignedCertificate(sslHostname);
    }

    @Bean
    public TomcatEmbeddedServletContainerFactory tomcatFactory()
            throws Exception {
        SelfSignedCertificate.addPrivateKeyToKeyStore(keyStore,
                keyStorePassword, keyPassword, keyAlias,
                selfSignedCertificate());

        return new CustomTomcatEmbeddedServletContainerFactory();
    }
}

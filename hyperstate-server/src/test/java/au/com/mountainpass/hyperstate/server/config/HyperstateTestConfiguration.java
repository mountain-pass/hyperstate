package au.com.mountainpass.hyperstate.server.config;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableAutoConfiguration
@EnableAspectJAutoProxy
@ComponentScan("au.com.mountainpass.hyperstate")
public class HyperstateTestConfiguration implements
        ApplicationListener<EmbeddedServletContainerInitializedEvent> {

    public final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private int port;

    @Value("${au.com.mountainpass.hyperstate.test.ssl.hostname}")
    private String sslHostname;

    public URI getBaseUri() {
        return URI.create("https://" + sslHostname + ":" + getPort());
    }

    public int getPort() {
        return port;
    }

    @Override
    public void onApplicationEvent(
            final EmbeddedServletContainerInitializedEvent event) {
        this.port = event.getEmbeddedServletContainer().getPort();
    }

}

package au.com.mountainpass.hyperstate.server.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableAutoConfiguration
@EnableAspectJAutoProxy
@ComponentScan("au.com.mountainpass.hyperstate")
public class HyperstateTestConfiguration {

    public final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

}

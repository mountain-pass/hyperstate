package au.com.windyroad.hyperstate.client.webdriver;

import java.lang.reflect.InvocationTargetException;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("ui-integration")
public class WebDriverTestConfiguration {

    @Autowired
    private WebDriverFactory webDriverFactory;

    @Bean(destroyMethod = "quit")
    public WebDriver webDriver()
            throws ClassNotFoundException, NoSuchMethodException,
            SecurityException, InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        return webDriverFactory.createWebDriver();
    }
}

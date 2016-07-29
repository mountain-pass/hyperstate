package au.com.mountainpass.hyperstate.client.webdriver;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("ui-integration")
public class HyperstateTestWebDriverConfiguration {

  @Autowired
  private WebDriverFactory webDriverFactory;

  @Bean(destroyMethod = "quit")
  public WebDriver webDriver() throws ClassNotFoundException, NoSuchMethodException,
      SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException,
      InvocationTargetException {
    final WebDriver driver = webDriverFactory.createWebDriver();
    driver.manage().timeouts().setScriptTimeout(15, TimeUnit.SECONDS);
    return driver;
  }
}

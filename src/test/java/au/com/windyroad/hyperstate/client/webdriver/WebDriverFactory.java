package au.com.windyroad.hyperstate.client.webdriver;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("ui-integration")
public class WebDriverFactory {

    @Value(value = "${webdriver.driver:org.openqa.selenium.firefox.FirefoxDriver}")
    String driverClassName;

    @Value(value = "${webdriver.window.width:1024}")
    int width;

    @Value(value = "${webdriver.window.height:768}")
    int height;

    public WebDriver createWebDriver()
            throws ClassNotFoundException, NoSuchMethodException,
            SecurityException, InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        DesiredCapabilities cap = new DesiredCapabilities();
        WebDriver driver = createDriver(cap);
        driver.manage().window().setSize(new Dimension(width, height));
        return driver;
    }

    private WebDriver createDriver(DesiredCapabilities cap)
            throws ClassNotFoundException, NoSuchMethodException,
            InstantiationException, IllegalAccessException,
            InvocationTargetException {
        Class<?> driverClass = Class.forName(driverClassName);
        Constructor<?> constructor = driverClass
                .getConstructor(Capabilities.class);
        return (WebDriver) constructor.newInstance(cap);
    }
}

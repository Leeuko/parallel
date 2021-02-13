package tests;

import java.net.MalformedURLException;
import java.net.URL;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.*;
import pages.CapabilityFactory;
import pages.HomePage;

public class BaseTest {

    public HomePage homePage;

    //Declare ThreadLocal Driver (ThreadLocalMap) for ThreadSafe Tests
    protected static ThreadLocal<RemoteWebDriver> driver = new ThreadLocal<>();
    public CapabilityFactory capabilityFactory = new CapabilityFactory();

    public WebDriver getDriver() {
        //Get driver from ThreadLocalMap
        return driver.get();
    }

    @BeforeClass
    @Parameters(value={"browser"})
    public void setup (String browser) throws MalformedURLException {
        //Set Browser to ThreadLocalMap
        driver.set(new RemoteWebDriver(new URL("http://192.168.88.211:4444/wd/hub"), capabilityFactory.getCapabilities(browser)));
    }

    @BeforeMethod
    public void methodLevelSetup() {
        homePage = new HomePage(driver.get());
    }

    @AfterClass void terminate () {
        //Remove the ThreadLocalMap element
        driver.remove();
    }
}
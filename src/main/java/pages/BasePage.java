package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePage {
    public WebDriver driver;
    public WebDriverWait wait;

    public BasePage(WebDriver driver) {

        this.driver = driver;
        wait = new WebDriverWait(driver,20);
    }

    public static void exceptCookies(WebDriver driver) {
        if (driver.findElements(By.cssSelector("#onetrust-accept-btn-handler")).size()!=0)
        { driver.findElement(By.cssSelector("#onetrust-accept-btn-handler")).click();}
    }

    public void waitVisibility(By by){
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    public static void pageLoaded (WebDriver driver, String url, int timeout) {
        new WebDriverWait(driver, timeout).until(ExpectedConditions.urlToBe(url));
    }

}
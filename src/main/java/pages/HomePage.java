package pages;

import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.sql.Timestamp;

import static java.lang.Thread.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class HomePage extends BasePage {

    public HomePage(WebDriver driver) {
        super(driver);
    }
    private final Logger logger = LogManager.getLogger(HomePage.class);

    String baseURL = "https://events.epam.com/";

    //To show text where result can be different
    @Attachment(value = "{0}", type = "text/plain")
    public static String saveTextLog(String message) {
        return message;
    }

    @Step("Open Epam page")
    public HomePage goToEpam() {

        driver.get(baseURL);
        pageLoaded(driver, baseURL, 60);
        return this;
    }

    @Step("Go to Events page")
    public HomePage goToEventsPage(){

        BasePage.exceptCookies(driver);

        //sometimes buttons from the top menu are not shown
        if (driver.findElements(Objects.eventsFromTopMenu).size()!=0)
        {Objects.events(driver).click();}
        //if buttons do not appear, click on the upcoming events from the bottom section
        else {
            WebElement element = driver.findElement(Objects.eventsFromBottomMenu);
            Actions actions = new Actions(driver);
            actions.moveToElement(element).click().perform();
        }

        waitVisibility(Objects.upcomingEventsCounter);
        return this;
    }

    @Step("Go to Video page")
    public HomePage goToVideoPage() throws InterruptedException {

        BasePage.exceptCookies(driver);
        Objects.videos(driver).click();
        //here sleep is important,test runs too fast, waitVisibility doesn't work
        sleep(500);
        waitVisibility(Objects.filter);
        //here sleep is important, have to wait for correct cards, another way test returns card before filtering
        sleep(500);
        return this;
    }

    @Step("Verify number of events")
    public HomePage verifyEvents(By counter, By events, By numberEvents) {

        int counterNumber = Integer.parseInt(driver.findElement(counter).getText());
        WebElement  allEvents= driver.findElement(events);
        List<WebElement> numberOfEvents = allEvents.findElements(numberEvents);
        Assert.assertEquals(counterNumber, numberOfEvents.size());
        return this;
    }

    @Step("Verify information position")
    public HomePage verifyOrderOfInformation() {

        assertTrue(driver.findElement(By.xpath("//div[@class='evnt-card-wrapper']/div[1]/div/div[1][contains(@class, 'online-cell')]")).isDisplayed());
        assertTrue(driver.findElement(By.xpath("//div[@class='evnt-card-wrapper']/div[1]/div/div[2][contains(@class, 'language-cell')]")).isDisplayed());
        assertTrue(driver.findElement(By.xpath("//div[@class='evnt-card-wrapper']/div[1]/div/div[3][contains(@class, 'calendar-cell')]")).isDisplayed());
        assertTrue(driver.findElement(By.xpath("//div[@class='evnt-card-wrapper']/div[2]/div/div[1][@class='evnt-event-name']/h1/span")).isDisplayed());
        assertTrue(driver.findElement(By.xpath("//div[@class='evnt-card-wrapper']/div[2]/div/div[2][@class='evnt-event-dates']")).isDisplayed());
        assertTrue(driver.findElement(By.xpath("//div[@class='evnt-card-wrapper']/div[3]/div/div[1][contains(@class, 'evnt-people-cell')]")).isDisplayed());
        return this;
    }

    @Step ("Find out the date of event")
    public HomePage getEventsDate(By allsections, By allcards) throws ParseException {
        WebElement allSections = driver.findElement(allsections);
        List<WebElement> allCards = allSections.findElements(allcards);
        for (WebElement card : allCards) {
            String eventDate = card.getText();
            String eventEndDate = eventDate.split("- ")[1];
            String selectMonth = eventEndDate.split(" ")[1];
            String year = eventDate.substring(eventDate.lastIndexOf(" ") + 1);
            String eventStartDateWithoutYear = eventDate.substring(0, eventDate.indexOf(" - "));
            String eventStartDate;
            String[] splittedStartDate = eventStartDateWithoutYear.split(" ");
            if (splittedStartDate.length == 2)
            {eventStartDate = eventStartDateWithoutYear + " " + year;}
            else
            {eventStartDate = eventStartDateWithoutYear + " " + selectMonth + " " + year;}

            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            DateFormat df = new SimpleDateFormat("dd MMM yyyy");
            Date startDateTimestamp = df.parse(eventStartDate);
            Date endDateTimestamp = df.parse(eventEndDate);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", card);
            if (currentTimestamp.after(startDateTimestamp) && currentTimestamp.before(endDateTimestamp))
            { Objects.duringThisWeek.add(eventDate); }
            else
            { Objects.duringNotThisWeek.add(eventDate);}
        }
        return this;
    }

    @Step("Return group of events by dates")
    public HomePage thisWeekEvents(){
        logger.info(Objects.duringThisWeek + " - are during current week" + " and " + Objects.duringNotThisWeek + " - are during not current week");
        saveTextLog(Objects.duringThisWeek + " - are during current week" + " and " + Objects.duringNotThisWeek + " - are during not current week");
        return this;
    }

    @Step("Verify dates of past events in Canada")
    public HomePage pastEventsCanada() throws InterruptedException {
        Objects.pastEvents(driver).click();
        Objects.locations(driver).click();
        waitVisibility(Objects.locationCanada);
        driver.findElement(Objects.locationCanada).click();
        //sleep is needed to wait for valid cards
        sleep(500);
        return this;
    }

    @Step("Event details")
    public  HomePage eventDetails(){
        String Title = driver.findElement(Objects.CardTitle).getText();
        Objects.card(driver).click();
        waitVisibility(Objects.eventContent);
        String eventContentTitle = driver.findElement(Objects.eventContent).getText();
        assertEquals(eventContentTitle, Title);

        return  this;
    }

    @Step("Set filters for videos: Category - Testing, Location - Belarus, Language - English")
    public  HomePage openVideosByCriteria() throws InterruptedException {

        Objects.moreFilters(driver).click();
        Objects.category(driver).click();

        //move scroll down to see Testing checkbox
        JavascriptExecutor je = (JavascriptExecutor) driver;
        WebElement categoryTesting = Objects.categoryTesting(driver);
        je.executeScript("arguments[0].scrollIntoView(true);",categoryTesting);
        //page scroll move down too, so return it back to the top
        JavascriptExecutor je2 = (JavascriptExecutor) driver;
        WebElement login = Objects.category(driver);
        je2.executeScript("arguments[0].scrollIntoView(true);",login);
        // now checkbox can be selected
        categoryTesting.click();

        Objects.location(driver).click();
        Objects.locationBelarus(driver).click();
        Objects.language(driver).click();
        Objects.languageEnglish(driver).click();
        return  this;
    }

    @Step("Verify video results for set criteria")
    public  HomePage verifyVideosShownByCriteria(By Sections, By Cards){

        WebElement allSections = driver.findElement(Sections);
        List<WebElement> allCards = allSections.findElements(Cards);

        //collect all links to the cards details
        for (WebElement card : allCards) {
            String eventLink = card.getAttribute("href");
            Objects.linkLocation.add(eventLink);
        }

        //Open each link and verify information
        for (String cardLink : Objects.linkLocation) {
            driver.get(cardLink);
            waitVisibility(Objects.topic);

            BasePage.exceptCookies(driver);

            String cardTopic = driver.findElement(Objects.cardTitle).getText();
            String Country = Objects.videoCountry(driver).getText();

            //collect all labels with topics
            WebElement Topics = driver.findElement(By.xpath("//div[@class='evnt-topics-wrapper']"));
            List<WebElement> allTopics = Topics.findElements(By.xpath("//div[contains(@class,'evnt-talk-topic')]/label"));
            //verify that Testing label exists among labels
            for (WebElement topic : allTopics) {
                String Topic = topic.getText();
                if (Topic.contains("Testing") )
                {Objects.validTopics.add(cardTopic);}
                else
                {Objects.invalidTopics.add(cardTopic); }
            }

            String Language = Objects.videoLanguage(driver).getText();

            if (Country.contains("Belarus") && Language.contains("ENGLISH") && Objects.validTopics.size()!=0 )
            { Objects.validInfo.add(cardTopic); }
            else
            { Objects.invalidInfo.add(cardTopic);}

        }
        if (Objects.invalidInfo.isEmpty())
        {
            logger.info("all cards contain valid information for set criteria");
            saveTextLog("all cards contain valid information for set criteria");
        }
        else{
            logger.info(Objects.invalidInfo + " - this topics should not be  shown for set criteria");
            saveTextLog(Objects.invalidInfo + " - this topics should not be  shown for set criteria");
        }
        return  this;
    }

    @Step("Verify video results for set criteria")
    public  HomePage videoSearch(By allsections, By allcards) throws InterruptedException {

        Objects.videoSearch(driver).sendKeys("QA");
        //need to wait for correct cards after search
        sleep(800);

        //collects all cards appeared after search
        WebElement allSections = driver.findElement(allsections);
        List<WebElement> allCards = allSections.findElements(allcards);

        //verify QA word in title
        for(WebElement card : allCards)
        {
            String VideoTitle = card.getText();
            if (VideoTitle.contains("QA"))
            {Objects.validTopics.add(VideoTitle);}
            else
            {
                Objects.invalidTopics.add(VideoTitle);
            }
        }
        if(Objects.invalidTopics.isEmpty())
        {
            logger.info("all cards contain valid information for set criteria");
            saveTextLog("all cards contain valid information for set criteria");
        }
        else {
            logger.info(Objects.invalidTopics + " - this topics should not be  shown for set criteria");
            saveTextLog(Objects.invalidTopics + " - this topics should not be  shown for set criteria");
        }
        return this;
    }

}
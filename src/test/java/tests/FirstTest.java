package tests;

import io.qameta.allure.*;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.Objects;
import utils.Listeners.TestListener;

import java.text.ParseException;

@Listeners({ TestListener.class })
@Epic("Regression Tests")
@Feature("Event Tests")
public class FirstTest extends BaseTest {



    @Test (priority = 1, description="Test 3: Preview upcoming events")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Test Description: Verify This week date of upcoming events")
    @Story("Verify upcoming events")
    public void verifyThisWeekDate () throws ParseException{
        homePage
                .goToEpam()
                .goToEventsPage()
                .getEventsDate(Objects.allUpcomingSections, Objects.allCards)
                .thisWeekEvents();

    }

}
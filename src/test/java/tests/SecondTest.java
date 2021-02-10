package tests;

import io.qameta.allure.*;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.Objects;
import utils.Listeners.TestListener;

@Listeners({ TestListener.class })
@Epic("Regression Tests")
@Feature("Event Tests")
public class SecondTest extends BaseTest {


    @Test (priority = 0, groups ={"videos"} , description="Test 7: Verify search for videos by word 'QA'")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Test Description: Open videos and search by word QA")
    @Story("Verify criteria for videos")
    public void VerifySearchByQA () throws InterruptedException {
        homePage
                .goToEpam()
                .goToVideoPage()
                .videoSearch(Objects.allVideos, Objects.CardTitle);
    }
}
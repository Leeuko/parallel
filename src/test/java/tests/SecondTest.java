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

    @Test (priority = 1, description="Test 6: Verify videos by criteria")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Test Description: Open videos and verify filters")
    @Story("Verify criteria for videos")
    public void verifyVideosByCriteria () throws InterruptedException {
        homePage
                .goToEpam()
                .goToVideoPage()
                .videosByCriteria()
                .videosDecision();
    }

    @Test (priority = 1, description="Test 7: Verify search for videos by word 'QA'")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Test Description: Open videos and search by word QA")
    @Story("Verify criteria for videos")
    public void VerifySearchByQA () throws InterruptedException {
        homePage
                .goToEpam()
                .goToVideoPage()
                .searchQAVideos()
                .verifyQATopics()
                .videosDecision();
    }
}
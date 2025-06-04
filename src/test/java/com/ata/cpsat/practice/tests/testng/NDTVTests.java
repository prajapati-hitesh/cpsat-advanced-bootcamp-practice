package com.ata.cpsat.practice.tests.testng;

import com.ata.cpsat.framework.helper.FrameHelper;
import com.ata.cpsat.framework.threads.ThreadLocalSEDriver;
import com.ata.cpsat.framework.utility.DateUtility;
import com.ata.cpsat.framework.utility.ScreenshotUtility;
import com.ata.cpsat.practice.pages.NDTVHomePage;
import com.ata.cpsat.runner.TestNgSuiteRunner;
import org.openqa.selenium.WindowType;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class NDTVTests extends TestNgSuiteRunner {
    @Test
    public void getHrefTests() throws IOException {
        NDTVHomePage ndtvHomePage = new NDTVHomePage();

        // Load Website
        ndtvHomePage.loadWebsite(true).navigateTo(NDTVHomePage.Menu.BUSINESS);

        // Get HREF Of top stories
        List<String> topStoriesHREFs = ndtvHomePage.getAllTopStoriesHREF();

        // Print them
        System.out.println("\n-------------------- TOP STORIES LINKS --------------------\n");
        topStoriesHREFs.forEach(System.out::println);

        // Load home page and open LATEST in new window
        ndtvHomePage.loadWebsite(false).navigateTo(NDTVHomePage.Menu.LATEST, WindowType.WINDOW);

        // Get list of Windows
        List<String> windows = FrameHelper.getAllWindows(ThreadLocalSEDriver.getDriver());

        // Switch to latest
        FrameHelper.switchToWindow(ThreadLocalSEDriver.getDriver(), windows.get(1));
        System.out.println("\nTitle of Current Page Is : " + ThreadLocalSEDriver.getDriver().getTitle());

        Map<String, Object> mapOfTopStories = new NDTVHomePage.Latest().getHREFForTopStories(3);

        System.out.println("\n-------------------- LATEST STORIES HREF --------------------\n");
        List.of(mapOfTopStories.get("HREF_LIST")).forEach(link -> {
            System.out.println(link +"\n");
        });
        // Save Image
        ScreenshotUtility.saveScreenshotFile((byte[]) mapOfTopStories.get("SCREENSHOT"), SCREENSHOT_DIR.concat("ndtv-latest-").concat(DateUtility.getCurrentTimeStamp()).concat(".png"));

        // Close current window
        ThreadLocalSEDriver.getDriver().close();
        FrameHelper.switchToWindow(ThreadLocalSEDriver.getDriver(), windows.get(0));
    }
}

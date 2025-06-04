package com.ata.cpsat.practice.tests.testng;

import com.assertthat.selenium_shutterbug.core.Capture;
import com.assertthat.selenium_shutterbug.core.PageSnapshot;
import com.assertthat.selenium_shutterbug.core.Shutterbug;
import com.ata.cpsat.framework.helper.LinkHelper;
import com.ata.cpsat.framework.threads.ThreadLocalSEDriver;
import com.ata.cpsat.framework.utility.DateUtility;
import com.ata.cpsat.framework.utility.ScreenshotUtility;
import com.ata.cpsat.runner.TestNgSuiteRunner;
import org.junit.platform.commons.util.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class BrokenTests extends TestNgSuiteRunner {

    @Test
    public void brokenLinksTest() throws IOException {
        // Load WebSite
        ThreadLocalSEDriver.getDriver().get("http://www.deadlinkcity.com/");

        // Get All <a> tags
        List<WebElement> listOfAllATags = ThreadLocalSEDriver.getDriver()
                .findElements(By.tagName("a"))
                .stream().filter(element -> !StringUtils.isBlank(element.getDomAttribute("href")))
                .toList();

        System.out.println("Total Links Found : " + listOfAllATags.size());

        // Take full page screenshot
        PageSnapshot fullPageSnapshot = Shutterbug.shootPage(ThreadLocalSEDriver.getDriver(), Capture.FULL_SCROLL, 500);

        // iterate over each link and check its status
        listOfAllATags.forEach(linkElement -> {
            // Get HREF
            String url = linkElement.getDomAttribute("href");

            // Check if the link is valid or not
            boolean isBroken = LinkHelper.isBrokenLink(url);

            if (isBroken) {
                // Link is broken
                System.out.println("BROKEN\t| " + url);
                fullPageSnapshot.highlight(linkElement, Color.RED, 2);
            } else {
                // link is working
                System.out.println("WORKING\t| " + url);
                fullPageSnapshot.highlight(linkElement, Color.GREEN, 2);
            }
        });

        String filePath = SCREENSHOT_DIR.concat("broken-links-").concat(DateUtility.getCurrentTimeStamp()).concat(".png");
        // Save screenshot
        ScreenshotUtility.saveScreenshotFile(fullPageSnapshot.getBytes(), filePath);
    }

    @Test(priority = 1)
    public void brokenImageTests() throws IOException {
        // Load WebSite
        ThreadLocalSEDriver.getDriver().get("http://www.deadlinkcity.com/");

        // Get All <a> tags
        List<WebElement> listOfAllImageTags = ThreadLocalSEDriver.getDriver()
                .findElements(By.tagName("img"))
                .stream().filter(element -> !StringUtils.isBlank(element.getDomAttribute("src")))
                .toList();

        System.out.println("Total Images Found : " + listOfAllImageTags.size());

        // Take full page screenshot
        PageSnapshot fullPageSnapshot = Shutterbug.shootPage(ThreadLocalSEDriver.getDriver(), Capture.FULL_SCROLL, 500);

        // iterate over each link and check its status
        listOfAllImageTags.forEach(linkElement -> {
            // Get HREF
            String url = linkElement.getDomAttribute("src");

            // Check if the link is valid or not
            boolean isBroken = LinkHelper.isBrokenImage(linkElement);

            if (isBroken) {
                // Link is broken
                System.out.println("BROKEN\t| " + url);
                fullPageSnapshot.highlight(linkElement, Color.RED, 2);
            } else {
                // link is working
                System.out.println("WORKING\t| " + url);
                fullPageSnapshot.highlight(linkElement, Color.GREEN, 2);
            }
        });

        String filePath = SCREENSHOT_DIR.concat("broken-images-").concat(DateUtility.getCurrentTimeStamp()).concat(".png");
        // Save screenshot
        ScreenshotUtility.saveScreenshotFile(fullPageSnapshot.getBytes(), filePath);
    }
}

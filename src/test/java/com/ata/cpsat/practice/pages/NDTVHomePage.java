package com.ata.cpsat.practice.pages;

import com.assertthat.selenium_shutterbug.core.Capture;
import com.assertthat.selenium_shutterbug.core.PageSnapshot;
import com.assertthat.selenium_shutterbug.core.Shutterbug;
import com.ata.cpsat.framework.helper.ElementHelper;
import com.ata.cpsat.framework.manager.SeleniumObjectManager;
import com.ata.cpsat.framework.threads.ThreadLocalSEDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.awt.*;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NDTVHomePage {
    By topStoriesSectionBy = By.className("widcont_topstories");
    By linksBy = By.tagName("a");
    By mainHeaderMenuBy = By.id("header2");


    public List<WebElement> getHREFs(WebElement parentElement) {
        return parentElement.findElements(linksBy);
    }

    public NDTVHomePage loadWebsite(boolean isFirstTime) {
        ThreadLocalSEDriver.getDriver().get("https://www.ndtv.com/");

        if (isFirstTime) {
            // Wait until notification popup is visible
            SeleniumObjectManager.getWaitObject(Duration.ofMinutes(3))
                    .until(ExpectedConditions.visibilityOfElementLocated(By.className("noti_wrap")));

            if (ElementHelper.getInstance().isElementDisplayed(By.className("noti_wrap"))) {
                ElementHelper.getInstance().click(By.linkText("No Thanks"));
            }
        }
        return this;
    }

    public List<String> getAllTopStoriesHREF() {
        List<WebElement> allHrefElements = getHREFs(ThreadLocalSEDriver.getDriver().findElement(topStoriesSectionBy));

        List<String> allHrefs = new ArrayList<>();

        allHrefElements.forEach(element -> {
            allHrefs.add(ElementHelper.getInstance().getAttribute(element, "href"));
        });
        return allHrefs;
    }

    public void navigateTo(Menu menu, WindowType windowType) {

        WebElement headerMenuElement = ThreadLocalSEDriver.getDriver().findElement(mainHeaderMenuBy);

        if (windowType == WindowType.TAB) {
            // New TAB - CTRL + CLICK
            SeleniumObjectManager.getActionObject()
                    .moveToElement(headerMenuElement.findElement(menu.getBy()))
                    .keyDown(Keys.LEFT_CONTROL)
                    .click(headerMenuElement.findElement(menu.getBy()))
                    .keyUp(Keys.LEFT_CONTROL)
                    .build()
                    .perform();
        } else if (windowType == WindowType.WINDOW) {
            // New WINDOW - SHIFT + CLICK
            SeleniumObjectManager.getActionObject()
                    .moveToElement(headerMenuElement.findElement(menu.getBy()))
                    .keyDown(Keys.LEFT_SHIFT)
                    .click(headerMenuElement.findElement(menu.getBy()))
                    .keyUp(Keys.LEFT_SHIFT)
                    .build()
                    .perform();
        }
    }

    public void navigateTo(Menu menu) {
        ElementHelper
                .getInstance()
                .click(ThreadLocalSEDriver.getDriver().findElement(menu.getBy()));
    }

    public enum Menu {
        HOME(By.className("ndtvlogo")),
        LIVE_TV(By.linkText("LIVE TV")),
        LATEST(By.linkText("LATEST")),
        COVID_19(By.linkText("COVID-19")),
        OLYMPICS(By.linkText("Olympics ")),
        INDIA(By.linkText("INDIA")),
        VIDEO(By.linkText("VIDEO")),
        OPINION(By.linkText("OPINION")),
        WORLD(By.linkText("WORLD")),
        CITIES(By.linkText("CITIES")),
        OFFBEAT(By.linkText("OFFBEAT")),
        TRENDS(By.linkText("TRENDS")),
        BUSINESS(By.linkText("BUSINESS"));

        final By byElement;

        Menu(By by) {
            this.byElement = by;
        }

        public By getBy() {
            return byElement;
        }
    }

    public static class Latest {
        By latestNewsTopStoriesSectionBy = By.className("lisingNews");
        By latestNewsTopStoriedNewsHeadingBy = By.className("newsHdng");
        By linksBy = By.tagName("a");

        public Map<String, Object> getHREFForTopStories(int maxTopStories) throws IOException {
            List<WebElement> linkSectionElements = ThreadLocalSEDriver.getDriver()
                    .findElement(latestNewsTopStoriesSectionBy)
                    .findElements(latestNewsTopStoriedNewsHeadingBy);

            PageSnapshot originalSnapshot = Shutterbug.shootPage(ThreadLocalSEDriver.getDriver(), Capture.FULL, true);
            List<String> hrefList = new ArrayList<>();
            linkSectionElements.forEach(sectionElement -> {
                // Highlight element
                originalSnapshot.highlight(sectionElement, Color.RED, 3);
                WebElement linkElement = sectionElement.findElement(linksBy);
                hrefList.add(ElementHelper.getInstance().getAttribute(linkElement, "href"));
            });

            // Get Screenshot as Bytes
            byte[] screenshotAsBytes = originalSnapshot.blurExcept(ThreadLocalSEDriver.getDriver().findElement(latestNewsTopStoriesSectionBy))
                    .getBytes();

            return Map.of("HREF_LIST", hrefList, "SCREENSHOT", screenshotAsBytes);
        }

    }
}

package com.ata.cpsat.practice.tests.testng.setA;

import com.ata.cpsat.framework.helper.ActionHelper;
import com.ata.cpsat.framework.helper.ElementHelper;
import com.ata.cpsat.framework.manager.SeleniumObjectManager;
import com.ata.cpsat.framework.threads.ThreadLocalSEDriver;
import com.ata.cpsat.practice.pages.setA.MockSetAHomePage;
import com.ata.cpsat.runner.TestNgSuiteRunner;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class QuestionOne extends TestNgSuiteRunner {
    WebDriver driver = ThreadLocalSEDriver.getDriver();

    @BeforeClass
    public void setupApplication() {
        ThreadLocalSEDriver.getDriver().get("https://mockexam1cpsat.agiletestingalliance.org/");

        // wait until the popup [CP-SAT Selenium Summit 2021] is visible. Close it once in frame
        SeleniumObjectManager.getWaitObject(Duration.ofSeconds(60))
                .until(ExpectedConditions.visibilityOfElementLocated(By.className("eicon-close")))
                .click();
    }

    @Test
    public void testA() {
        MockSetAHomePage page = new MockSetAHomePage();
        // click on hamburger
        page.openMenu();
        // Using findElements read all the menu items values and print on the system console.
        List<WebElement> allVisibleHrefs = SeleniumObjectManager.getWaitObject(Duration.ofSeconds(60))
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("menu-slideout-spacious")))
                .findElements(By.tagName("a"))
                .stream().filter(WebElement::isDisplayed)
                .filter(e -> !StringUtils.isBlank(e.getText()))
                .toList();
        System.out.println("\n----------------------------- Menu Items -----------------------------");
        allVisibleHrefs.forEach(ele -> {
            System.out.println(ElementHelper.getInstance().getText(ele));
        });

        // For all the social media icons,
        // find the respective urls of all the social media icons and print them on system console
        List<WebElement> socialMediaIconAnchorElements = ThreadLocalSEDriver.getDriver()
                .findElement(By.id("lsi_widget-1"))
                .findElements(By.tagName("a"));

        System.out.println("\n--------------------- HREF for all social Media Icon ---------------------");
        socialMediaIconAnchorElements.forEach(ele -> {
            System.out.println(ElementHelper.getInstance().getAttribute(ele, "title") + " : " +
                    ElementHelper.getInstance().getAttribute(ele, "href"));
        });

        // Right click on all the social media icon one by one and open them in a new window.
        // Get the title of the new window and print the same on the system console
        socialMediaIconAnchorElements.forEach(element -> {
            String parentWindow = ThreadLocalSEDriver.getDriver().getWindowHandle();

            SeleniumObjectManager.getActionObject()
                    .moveToElement(element)
                    .contextClick(element)
                    .build().perform();
        });

    }
}
package com.ata.cpsat.practice.tests.testng.setA;

import com.ata.cpsat.framework.helper.ElementHelper;
import com.ata.cpsat.framework.helper.SyncHelper;
import com.ata.cpsat.framework.manager.SeleniumObjectManager;
import com.ata.cpsat.framework.threads.ThreadLocalSEDriver;
import com.ata.cpsat.framework.utility.ColorUtility;
import com.ata.cpsat.framework.utility.DateUtility;
import com.ata.cpsat.framework.utility.ScreenshotUtility;
import com.ata.cpsat.framework.utility.StringUtility;
import com.ata.cpsat.practice.enums.MockExamSetAMenu;
import com.ata.cpsat.practice.pages.setA.MockSetAHomePage;
import com.ata.cpsat.runner.TestNgSuiteRunner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class QuestionFourTests extends TestNgSuiteRunner {
    private static final Logger logger = LogManager.getLogger(QuestionFourTests.class);

    @BeforeClass
    public void setupApplication() {
        ThreadLocalSEDriver.getDriver().get("https://mockexam1cpsat.agiletestingalliance.org/");

        // wait until the popup [CP-SAT Selenium Summit 2021] is visible. Close it once in frame
        SeleniumObjectManager.getWaitObject(Duration.ofSeconds(60))
                .until(ExpectedConditions.visibilityOfElementLocated(By.className("eicon-close")))
                .click();
    }

    @Test(priority = 0)
    public void challengeOneTest() {
        // navigate to Challenge one page
        new MockSetAHomePage().navigateTo(MockExamSetAMenu.CHALLENGE_1);

        // wait until the popup [Welcome To Challenge 1] is visible. Close it once in frame
        SeleniumObjectManager.getWaitObject(Duration.ofSeconds(60))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@aria-label='Close']")))
                .click();

        List<WebElement> accordionPanelElement = ThreadLocalSEDriver.getDriver()
                .findElements(By.className("eael-accordion-list"))
                .stream().filter(WebElement::isDisplayed).toList();
        for (WebElement accordion : accordionPanelElement) {
            // get current accordion title element
            WebElement accordionPanelTitleElement = accordion.findElement(By.className("eael-accordion-tab-title"));

            // get current accordion title element
            String currAccordionTitle = accordionPanelTitleElement.getText();
            // print accordion para one by one
            logger.info("--------------- Printing Content Details For [{}] ---------------", currAccordionTitle);

            SyncHelper.hardWait(Duration.ofMillis(800));
            // capture accordion color before click
            String panelColorRgbValueBefore = ElementHelper.getInstance()
                    .getBackgroundColorAsRGB(accordion.findElement(By.className("eael-accordion-header")));

            // click on accordion
            ElementHelper.getInstance().click(accordion);
            SyncHelper.hardWait(Duration.ofMillis(800));
            WebElement accordionBodyElement = accordion.findElement(By.tagName("p"));

            String panelColorRgbValueAfter = ElementHelper.getInstance()
                    .getBackgroundColorAsRGB(accordion.findElement(By.className("eael-accordion-header")));

            logger.info("Background color for [{}] before click is : {}", currAccordionTitle, ColorUtility.rgbaToHex(panelColorRgbValueBefore));
            logger.info("Background color for [{}] after click is : {}", currAccordionTitle, ColorUtility.rgbaToHex(panelColorRgbValueAfter));
            logger.info(ElementHelper.getInstance().getText(accordionBodyElement));

            // capture screenshot
            byte[] panelScreenshot = accordion.getScreenshotAs(OutputType.BYTES);

            // save screenshot to disc
            ScreenshotUtility.saveScreenshotFile(
                    panelScreenshot,
                    SCREENSHOT_DIR
                            .concat(StringUtility.toKebabCase(currAccordionTitle).concat("-")   // convert the panel name to kebab-case
                                    .concat(DateUtility.getCurrentTimeStamp())                      // generate current date time stamp
                                    .concat(".png"))                                            // add extension
            );

            assertThat(ColorUtility.rgbaToHex(panelColorRgbValueBefore))
                    .isNotEqualToIgnoringCase(ColorUtility.rgbaToHex(panelColorRgbValueAfter));
        }
    }

    @Test(priority = 1)
    public void challengeTwoTest() {
       ThreadLocalSEDriver.getDriver().get("https://mockexam1cpsat.agiletestingalliance.org/index.php/challenge-2/");

       SeleniumObjectManager.getWaitObject(Duration.ofSeconds(60))
                       .until(ExpectedConditions.textToBePresentInElementLocated(
                               By.className("entry-title"),
                               "Challenge 2"
                       ));

       logger.info("---------------------------- Printing Speaker Names ----------------------------");
       ThreadLocalSEDriver.getDriver().findElements(By.className("elementor-heading-title"))
               .stream().filter(WebElement::isDisplayed)
               .toList().forEach(titleElement -> {
                   logger.info(ElementHelper.getInstance().getText(titleElement));
               });
    }

    @Test(priority = 2)
    public void challengeFourTest() {
        ThreadLocalSEDriver.getDriver().get("https://mockexam1cpsat.agiletestingalliance.org/index.php/challenge-4/");

        // the tweets are not visible right now
    }
}

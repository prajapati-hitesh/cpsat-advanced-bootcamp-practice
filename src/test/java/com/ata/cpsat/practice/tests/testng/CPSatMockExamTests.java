package com.ata.cpsat.practice.tests.testng;

import com.assertthat.selenium_shutterbug.core.Capture;
import com.assertthat.selenium_shutterbug.core.Shutterbug;
import com.ata.cpsat.framework.driver.DriverFactory;
import com.ata.cpsat.framework.threads.ThreadLocalSEDriver;
import com.ata.cpsat.practice.pages.CPSatMockExamPage;
import com.ata.cpsat.runner.TestNgSuiteRunner;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.util.Map;

public class CPSatMockExamTests extends TestNgSuiteRunner {

    @Test(description = "Hover on CP-SAT Logo, Capture Screenshot & Print the ToolTips")
    public void hoverAndCaptureScreenshotTest() {
        // Load Website
        DriverFactory.getInstance().setUrl("https://mockexam2cpsat.agiletestingalliance.org/about/");
        WebDriver driver = ThreadLocalSEDriver.getDriver();

        CPSatMockExamPage cpSatMockExamPage = new CPSatMockExamPage();

        Map<CPSatMockExamPage.CPSat, WebElement> cpSatLogoElements = Map.of(
                CPSatMockExamPage.CPSat.JAVA, cpSatMockExamPage.cpSatLogoElement(CPSatMockExamPage.CPSat.JAVA),
                CPSatMockExamPage.CPSat.PYTHON, cpSatMockExamPage.cpSatLogoElement(CPSatMockExamPage.CPSat.PYTHON),
                CPSatMockExamPage.CPSat.C_SHARP, cpSatMockExamPage.cpSatLogoElement(CPSatMockExamPage.CPSat.C_SHARP)
        );

        // Hover
        cpSatLogoElements.forEach((enumVal, element) -> {
            // Hover & Get Tool Tip
            String currentToolTip = cpSatMockExamPage
                    .hover(element)
                    .getToolTip(enumVal);

            // Print Tool Tip
            System.out.println("Current Tool Tip : " + currentToolTip);

            // Take Screenshot
            Shutterbug.shootPage(driver, Capture.FULL, true)
                    .cropAround(element, 0, 50)
                    .withName(currentToolTip)
                    .save(SCREENSHOT_DIR);
        });
    }
}

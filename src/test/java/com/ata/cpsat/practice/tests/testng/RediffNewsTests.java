package com.ata.cpsat.practice.tests.testng;

import com.ata.cpsat.framework.utility.DateUtility;
import com.ata.cpsat.practice.pages.RediffHomePage;
import com.ata.cpsat.runner.TestNgSuiteRunner;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Using TestNG and WebDriver script Please try to cover the following. This question
 * carries 15 marks. You are free to use any of the browsers (Chrome or Firefox)
 * <pre>
 *      a. Open the website https://www.rediff.com/ (1/2 mark)
 *      b. Click on the menu for News (1/2 mark)
 *      c. Take a screenshot (showing that the News is clicked) (1/2 mark)
 *      d. Get the Href’s of all the submenu items (e.g headlines, defence etc) (2 marks)
 *      e. Assert that the tool tip of the adjacent menu item Business is “Business Headlines” (1.5 marks)
 *      f. Hover the mouse on the adjacent menu item (Business) showing the tool tip “Business Headlines”. Take a screenshot showing the tooltip (4 marks)
 *      g. Assert that the background colour of the submenu items for News is having the following RGB (188, 62, 49) value. ( 2 Marks)
 *      h. Assert that the value of RGB in above step maps to the following colour name (Grenadier) – Hint use the following website to get the colour name from RGB values https://www.color-blindness.com/color-name-hue/ (4 Marks)
 * </pre>
 */
public class RediffNewsTests extends TestNgSuiteRunner {

    /**
     * Using TestNG and WebDriver script Please try to cover the following. This question
     * carries 15 marks. You are free to use any of the browsers (Chrome or Firefox)
     * <pre>
     *      a. Open the website https://www.rediff.com/ (1/2 mark)
     *      b. Click on the menu for News (1/2 mark)
     *      c. Take a screenshot (showing that the News is clicked) (1/2 mark)
     *      d. Get the Href’s of all the submenu items (e.g headlines, defence etc) (2 marks)
     *      e. Assert that the tool tip of the adjacent menu item Business is “Business Headlines” (1.5 marks)
     *      f. Hover the mouse on the adjacent menu item (Business) showing the tool tip “Business Headlines”. Take a screenshot showing the tooltip (4 marks)
     *      g. Assert that the background colour of the submenu items for News is having the following RGB (188, 62, 49) value. ( 2 Marks)
     *      h. Assert that the value of RGB in above step maps to the following colour name (Grenadier) – Hint use the following website to get the colour name from RGB values https://www.color-blindness.com/color-name-hue/ (4 Marks)
     *  </pre>
     *
     * @throws IOException
     * @throws AWTException
     */
    @Test(alwaysRun = true, description = "Load https://www.rediff.com/ and assert menus, hover & background color of links", priority = 0)
    public void verifyNewsAndSubMenusWithTheirHREFTest() throws IOException, AWTException {
        RediffHomePage rediffHomePage = new RediffHomePage();
        byte[] byteArrayOfImage = rediffHomePage
                .loadWebsite()
                .navigateTo(RediffHomePage.Navigate.NEWS);


        if (byteArrayOfImage.length > 0) {
            FileUtils.writeByteArrayToFile(
                    new File(SCREENSHOT_DIR.concat("rediff-news-selected-menu-ss-").concat(DateUtility.getCurrentTimeStamp()).concat(".png")),
                    byteArrayOfImage
            );
            // Get All HREF Of Submenu & Print them
            System.out.println("\n============================== Sub Menu HREF ==============================\n");
            rediffHomePage.getAllHrefOfSubMenuForCurrentSelection().forEach(System.out::println);

            // Get Adjacent element
            WebElement adjacentElement = rediffHomePage.getAndMoveToAdjacentMenuOfCurrentSelection(RediffHomePage.ElementLocation.AFTER);

            // Hover
            //rediffHomePage.hoverElement(adjacentElement);

            // Get Hover text
            String actualHoverText = rediffHomePage.getTooltipForMenu(adjacentElement);
            System.out.println("\nActual Hover Text : " + actualHoverText);
            // Assert
            assertThat(actualHoverText)
                    .withFailMessage("")
                    .isEqualToIgnoringCase("Business headlines")
                    .isInstanceOf(String.class);

            // Get screenshot
            byte[] hoveredScreenshot = rediffHomePage.getScreenshotOfMenu();
            FileUtils.writeByteArrayToFile(
                    new File(SCREENSHOT_DIR.concat("rediff-business-hovered-menu-ss-").concat(DateUtility.getCurrentTimeStamp()).concat(".png")),
                    hoveredScreenshot
            );

            // Assert background color for all sub menu
            boolean hasSameColorForAllSubMenu = rediffHomePage.hasRGBValueAsForAllSubMenu("rgb(188, 62, 49)");
            System.out.println("\nAll Sub Menu Has Same Color : " + hasSameColorForAllSubMenu);
            // Assert
            assertThat(hasSameColorForAllSubMenu)
                    .withFailMessage("Submenu background color is not equal to RGB(188, 62, 49)")
                    .isTrue();

            // Get Color Name
            String actualColor = rediffHomePage.getColorName("rgb(188, 62, 49)");
            System.out.println("\nActual Color Name : " + actualColor);
            // Assert color name
            assertThat(actualColor)
                    .withFailMessage("Actual Color name is not matching expected.")
                    .isEqualToIgnoringCase("Grenadier")
                    .isInstanceOf(String.class);

        }
    }

    @Test(alwaysRun = true, description = "Load https://www.rediff.com/ and assert menus, hover & background color of links", priority = 0)
    public void verifyBusinessAndSubMenusWithTheirHREFTest() throws IOException, AWTException {
        RediffHomePage rediffHomePage = new RediffHomePage();
        byte[] byteArrayOfImage = rediffHomePage
                .loadWebsite()
                .navigateTo(RediffHomePage.Navigate.BUSINESS);


        if (byteArrayOfImage.length > 0) {
            FileUtils.writeByteArrayToFile(
                    new File(SCREENSHOT_DIR.concat("rediff-business-selected-menu-ss-").concat(DateUtility.getCurrentTimeStamp()).concat(".png")),
                    byteArrayOfImage
            );
            // Get All HREF Of Submenu & Print them
            System.out.println("\n============================== Sub Menu HREF ==============================\n");
            rediffHomePage.getAllHrefOfSubMenuForCurrentSelection().forEach(System.out::println);

            // Get Adjacent element
            WebElement adjacentElement = rediffHomePage.getAndMoveToAdjacentMenuOfCurrentSelection(RediffHomePage.ElementLocation.BEFORE);

            // Get Hover text
            String actualHoverText = rediffHomePage.getTooltipForMenu(adjacentElement);
            System.out.println("\nActual Hover Text : " + actualHoverText);
            // Assert
            assertThat(actualHoverText)
                    .withFailMessage("")
                    .isEqualToIgnoringCase("News Headlines")
                    .isInstanceOf(String.class);

            // Get screenshot
            byte[] hoveredScreenshot = rediffHomePage.getScreenshotOfMenu();
            FileUtils.writeByteArrayToFile(
                    new File(SCREENSHOT_DIR.concat("rediff-business-hovered-menu-ss-").concat(DateUtility.getCurrentTimeStamp()).concat(".png")),
                    hoveredScreenshot
            );

            // Assert background color for all sub menu
            boolean hasSameColorForAllSubMenu = rediffHomePage.hasRGBValueAsForAllSubMenu("rgb(159, 107, 63)");
            System.out.println("\nAll Sub Menu Has Same Color : " + hasSameColorForAllSubMenu);
            // Assert
            assertThat(hasSameColorForAllSubMenu)
                    .withFailMessage("Submenu background color is not equal to RGB(159, 107, 63)")
                    .isTrue();

            // Get Color Name
            String actualColor = rediffHomePage.getColorName("rgb(159, 107, 63)");
            System.out.println("\nActual Color Name : " + actualColor);
            // Assert color name
            assertThat(actualColor)
                    .withFailMessage("Actual Color name is not matching expected.")
                    .isEqualToIgnoringCase("Desert")
                    .isInstanceOf(String.class);

        }
    }
}

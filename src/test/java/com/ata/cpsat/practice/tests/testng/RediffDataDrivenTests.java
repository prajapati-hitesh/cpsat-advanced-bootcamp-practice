package com.ata.cpsat.practice.tests.testng;

import com.ata.cpsat.framework.json.JSONArray;
import com.ata.cpsat.framework.json.JSONObject;
import com.ata.cpsat.framework.threads.ThreadLocalSEDriver;
import com.ata.cpsat.framework.utility.DateUtility;
import com.ata.cpsat.framework.utility.ResourceUtility;
import com.ata.cpsat.framework.utility.ScreenshotUtility;
import com.ata.cpsat.framework.xls2json.ExcelParser;
import com.ata.cpsat.practice.pages.RediffHomePage;
import com.ata.cpsat.runner.TestNgSuiteRunner;
import org.apache.commons.io.FilenameUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A) Read the values of Menu / SubMenu and Href from excel sheet and for each row and do the following – 6 marks
 * B) Assert that for the menu/Sbumenu, actual Href is same as expected HREF as per excel sheet value – 4 marks
 * C) Click on the submenu and take a screenshot of the new window thus opened. - 3 marks
 */
public class RediffDataDrivenTests extends TestNgSuiteRunner {

    @Test(alwaysRun = true, dataProvider = "getRediffMenuData")
    public void assertSubMenuHREFTest(String menu, String subMenu, String expectedHref) {
        RediffHomePage rediffHomePage = new RediffHomePage();

        // Navigate
        rediffHomePage.loadWebsite().navigateTo(menu, subMenu);

        // Get Current URL
        String currPageUrl = ThreadLocalSEDriver.getDriver().getCurrentUrl();

        // Take screenshot
        byte[] currScreenBinaryArray = ScreenshotUtility.takeScreenshotAsByteArray(ThreadLocalSEDriver.getDriver());

        String filePath = SCREENSHOT_DIR.concat(menu).concat(" - ")
                .concat(subMenu).concat(" - ")
                .concat(DateUtility.getCurrentTimeStamp())
                .concat(".png");

        boolean isScreenshotSaved = ScreenshotUtility.saveScreenshotFile(currScreenBinaryArray, filePath);

        assertThat(isScreenshotSaved)
                .isTrue();

        assertThat(currPageUrl)
                .withFailMessage("Actual URL for [" + menu + " > " + subMenu + "] did not match expected URL")
                .isNotBlank()
                .isEqualToIgnoringCase(expectedHref)
                .isInstanceOf(String.class);
    }

    @DataProvider(name = "getRediffMenuData")
    public Object[][] getRediffMenuData() throws URISyntaxException {
        // Get Excel file path
        URI excelFilePath = ResourceUtility.getResourceFilePath("CP-SAT - Practice Paper - Data File.xlsx");

        // Read Excel
        JSONArray keywordsJsonArray = new JSONObject(ExcelParser.parseExcelFile(FilenameUtils.removeExtension(excelFilePath.getPath()), true))
                .getJSONArray("REDIFF");

        int rows = keywordsJsonArray.length();
        int columns = keywordsJsonArray.getJSONObject(0).length();

        Object[][] dataObject = new Object[rows][columns];

        final int[] rowCounter = {0};
        keywordsJsonArray.forEach(object -> {
            JSONObject personJsonObject = (JSONObject) object;
            int colCounter = 0;
            dataObject[rowCounter[0]][colCounter++] = personJsonObject.get("Menu");
            dataObject[rowCounter[0]][colCounter++] = personJsonObject.get("SubMenu");
            dataObject[rowCounter[0]][colCounter] = personJsonObject.get("ExpectedHref");
            rowCounter[0]++;
        });
        return dataObject;
    }
}

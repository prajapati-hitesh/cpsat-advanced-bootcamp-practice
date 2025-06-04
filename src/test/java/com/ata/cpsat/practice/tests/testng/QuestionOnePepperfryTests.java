package com.ata.cpsat.practice.tests.testng;

import com.ata.cpsat.framework.json.JSONArray;
import com.ata.cpsat.framework.json.JSONObject;
import com.ata.cpsat.framework.utility.ResourceUtility;
import com.ata.cpsat.framework.xls2json.ExcelParser;
import com.ata.cpsat.practice.pages.PepperfryHomePage;
import com.ata.cpsat.practice.pages.PepperfryProductViewPage;
import com.ata.cpsat.runner.TestNgSuiteRunner;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.openqa.selenium.WebElement;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <p>On http://www.pepperfry.com/, search for the items related to the search keyword.
 * Rearrange the search results in Ascending order of the price.</p>
 *
 * <pre>
 * Test #1: Use keyword “Bedsheets”,
 * Test #2: Use keyword “Clocks”
 * Test #3: Use keyword “Padlocks”.
 * </pre>
 * <p>Save these keywords in an Excel (.xls) file, read the search keywords from this file and then
 * execute your test. Write script in TestNG using WebDriver to test that the results are indeed in
 * Ascending order in Google Chrome. (12 points)</p>
 */
public class QuestionOnePepperfryTests extends TestNgSuiteRunner {

    @Test(dataProvider = "getSearchKeywords")
    public void checkResource(String searchKeyword) {
        PepperfryProductViewPage pepperfryProductViewPage = new PepperfryHomePage()
                .loadPepperfry()
                .searchProducts(searchKeyword.trim())
                .sortBy(PepperfryProductViewPage.SortBy.PRICE_LOW_TO_HIGH);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<WebElement> offerPriceElements = pepperfryProductViewPage.getOfferPriceElements();
        stopWatch.stop();
        long timeTakenToCaptureOfferPrice = stopWatch.getTime(TimeUnit.SECONDS);
        stopWatch.reset();
        stopWatch.start();
        List<WebElement> retailPriceElements = pepperfryProductViewPage.getRetailPriceElements();
        stopWatch.stop();

        long timeTakenToCaptureRetailPrice = stopWatch.getTime(TimeUnit.SECONDS);

        System.out.println("Time taken to get Offer Price Elements : " + timeTakenToCaptureOfferPrice);
        List<Integer> offerPrices = pepperfryProductViewPage.getOfferPrice(offerPriceElements);
        System.out.println("\n============================= OfferPrice ============================= \n");
        offerPrices.forEach(System.out::println);

        List<Integer> retailPrices = pepperfryProductViewPage.getRetailPrice(retailPriceElements);
        System.out.println("Time taken to get Offer Price Elements : " + timeTakenToCaptureRetailPrice);
        System.out.println("\n============================= Retail Price =============================\n");
        retailPrices.forEach(System.out::println);

        assertThat(offerPrices)
                .withFailMessage("Products are not in ascending order of their OFFER prices")
                .isSorted();

        assertThat(retailPrices)
                .withFailMessage("Products are not in ascending order of their RETAIL prices")
                .isSorted();
    }

    @DataProvider(name = "getSearchKeywords")
    public Object[][] getSearchKeywords() throws URISyntaxException {
        // Get Excel file path
        URI excelFilePath = ResourceUtility.getResourceFilePath("CP-SAT - Practice Paper - Data File.xlsx");

        // Read Excel
        JSONArray keywordsJsonArray = new JSONObject(ExcelParser.parseExcelFile(FilenameUtils.removeExtension(excelFilePath.getPath()), true))
                .getJSONArray("PAPER_A_Q1");

        int rows = keywordsJsonArray.length();
        int columns = keywordsJsonArray.getJSONObject(0).length();

        Object[][] dataObject = new Object[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                dataObject[i][j] = ((JSONObject) keywordsJsonArray.get(i)).get("SearchKeywords");
            }
        }
        return dataObject;
    }
}

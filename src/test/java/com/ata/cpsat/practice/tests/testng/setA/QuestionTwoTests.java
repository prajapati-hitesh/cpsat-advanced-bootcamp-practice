package com.ata.cpsat.practice.tests.testng.setA;

import com.ata.cpsat.framework.manager.SeleniumObjectManager;
import com.ata.cpsat.framework.threads.ThreadLocalSEDriver;
import com.ata.cpsat.framework.utility.DateUtility;
import com.ata.cpsat.framework.utility.ExcelUtility;
import com.ata.cpsat.framework.utility.SystemUtility;
import com.ata.cpsat.practice.enums.MockExamSetAMenu;
import com.ata.cpsat.practice.pages.setA.MockSetAHomePage;
import com.ata.cpsat.practice.pages.setA.MockSetAListOfParticipantsPage;
import com.ata.cpsat.runner.TestNgSuiteRunner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class QuestionTwoTests extends TestNgSuiteRunner {
    private static final Logger logger = LogManager.getLogger(QuestionTwoTests.class.getName());
    private final String SEPARATOR = SystemUtility.getFileSeparator();
    private final String INPUT_EXCEL_PATH = SystemUtility.getUserDirectory()
            .concat(SEPARATOR).concat("test-data").concat(SEPARATOR)
            .concat("CPSAT-Mock-Exam-SET-A-2022-Java.xlsx");

    private final String RESULT_EXCEL_PATH = SystemUtility.getUserDirectory()
            .concat(SEPARATOR).concat("result-data").concat(SEPARATOR);

    private final List<LinkedHashMap<String, String>> resultRowsForWrite = new ArrayList<>();

    @BeforeClass
    public void setupApplication() {
        ThreadLocalSEDriver.getDriver().get("https://mockexam1cpsat.agiletestingalliance.org/");

        // wait until the popup [CP-SAT Selenium Summit 2021] is visible. Close it once in frame
        SeleniumObjectManager.getWaitObject(Duration.ofSeconds(60))
                .until(ExpectedConditions.visibilityOfElementLocated(By.className("eicon-close")))
                .click();

        // open list of participants menu
        new MockSetAHomePage().navigateTo(MockExamSetAMenu.LIST_OF_PARTICIPANTS);
    }

    @Test(dataProvider = "getListOfParticipants")
    public void verifyListOfParticipantsTest(String testName, String name, String designation, String orgName, String expResult) {
        MockSetAListOfParticipantsPage participantsPage = new MockSetAListOfParticipantsPage();

        // check if values of each column matches to expected value or not
        boolean isAllColValueMatches = participantsPage.isIsAllColValueMatches(name, designation, orgName);

        // prepare a map to hold the result value
        LinkedHashMap<String, String> resultData = new LinkedHashMap<>();
        resultData.put("Test Name", testName);
        resultData.put("Name", name);
        resultData.put("Designation", designation);
        resultData.put("Organization Name", orgName);
        resultData.put("Expected Result", expResult);
        resultData.put("Actual Result", isAllColValueMatches ? "Matched" : "Not Matched, Fail");

        // add to row list for writing purpose
        resultRowsForWrite.add(resultData);

        // perform assertion so that the test fails in case of validation failure
        assertThat(isAllColValueMatches)
                .withFailMessage("Data mismatch. Please check excel sheet for details.")
                .isTrue()
                .isInstanceOf(Boolean.class);
    }


    @DataProvider(name = "getListOfParticipants")
    public Object[][] getListOfParticipants() {
        return ExcelUtility.readExcelAsArray(
                INPUT_EXCEL_PATH,
                "Q2_INPUT_DATA",
                false
        );
    }

    @AfterClass
    public void writeTestResultsToExcel() {
        ExcelUtility.writeExcelFromMapList(
                resultRowsForWrite,
                RESULT_EXCEL_PATH.concat("CPSAT-Mock-Exam-SET-A-2022-Java_Q2_result_").concat(DateUtility.getCurrentTimeStampWithFormatAs("dd-MM-yyyy_HH.mm.ss")).concat(".xlsx"),
                "Q2_RESULT_DATA"
        );
    }
}

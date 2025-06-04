package com.ata.cpsat.practice.tests.junit;

import com.ata.cpsat.framework.threads.ThreadLocalSEDriver;
import com.ata.cpsat.practice.pages.RediffHomePage;
import com.ata.cpsat.runner.JUnitSuiteRunner;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * a. Please capture the By locators for the following in the POM page
 * a. BSE Index Value
 * b. NSE Index Value
 * c. Enter company or MF (input field)
 * b. Using the POM class create a Junit Test for printing the BSE Index Value, NSE Index
 * Value and Enter company “Reliance Industries” in the company or MF Input field
 * c. Step b will open a new window, please assert that the title of this new window has
 * “Reliance” in it
 * d. Using Selenium switch back to the previous window and assert that the title of
 * that window has “Rediff.com:” in it
 */
public class RediffStockTests extends JUnitSuiteRunner {

    @ParameterizedTest
    @CsvSource({
            "Reliance Industries, Reliance, Rediff.com",
            "Kotak Mahindra Bank Ltd, Kotak Mahindra Bank Ltd., Rediff.com"
    })
    public void getStockDetailsAndValidateCompanyTest(String companyName, String newPageTitle, String parentPageTitle) {
        // Load Page
        RediffHomePage rediffHomePage = new RediffHomePage();
        rediffHomePage.loadWebsite();

        String nseIndex = rediffHomePage.getNSEIndex();
        String bseIndex = rediffHomePage.getBSEIndex();

        System.out.println("BSE Index : " + bseIndex);
        System.out.println("NSE Index : " + nseIndex);

        // Search for company
        rediffHomePage.searchCompany(companyName).switchToCompanyTab();

        // Assert title
        assertThat(ThreadLocalSEDriver.getDriver().getTitle())
                .withFailMessage("Actual page title does not match expected.")
                .containsIgnoringCase(newPageTitle);

        // Close current tab
        ThreadLocalSEDriver.getDriver().close();

        // Switch back to parent window
        rediffHomePage.switchToParentTab();

        // Assert the title
        assertThat(ThreadLocalSEDriver.getDriver().getTitle())
                .withFailMessage("Actual page title does not match expected.")
                .containsIgnoringCase(parentPageTitle);
    }
}

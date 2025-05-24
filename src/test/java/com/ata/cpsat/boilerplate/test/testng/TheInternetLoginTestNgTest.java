package com.ata.cpsat.boilerplate.test.testng;

import com.ata.cpsat.boilerplate.pages.theInternet.HomePage;
import com.ata.cpsat.boilerplate.pages.theInternet.LoginPage;
import com.ata.cpsat.runner.TestNgSuiteRunner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TheInternetLoginTestNgTest extends TestNgSuiteRunner {
    private static final Logger logger = LogManager.getLogger(TheInternetLoginTestNgTest.class.getName());


    @Test(priority = 0)
    public void verifyInvalidLoginCredential() {
        new HomePage().loadWebsite();

        LoginPage loginPage = new LoginPage();

        // Login to Application
        loginPage.login("tomsmith", "SuperSecretPassword");

        // Get error Message
        String errorMessage = new LoginPage().getErrorMessage().replaceAll("[@#$%×\\n]+", "");

        assertThat(errorMessage)
                .withFailMessage("Invalid error message validation failure.")
                .isInstanceOf(String.class)
                .isEqualTo("Your password is invalid!");

    }

    @Test(priority = 1)
    public void verifyValidLoginCredentials() {
        new HomePage().loadWebsite();

        LoginPage loginPage = new LoginPage();

        // Login to Application
        loginPage.login("tomsmith", "SuperSecretPassword!");

        // Get error Message
        String successLoginMessage = new HomePage().getSuccessMessage().replaceAll("[@#$%×\\n]+", "");

        assertThat(successLoginMessage)
                .withFailMessage("Failed validating of Login Success message")
                .isInstanceOf(String.class)
                .isEqualTo("You logged into a secure area!");
    }
}

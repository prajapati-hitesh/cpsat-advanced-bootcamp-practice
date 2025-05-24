package com.ata.cpsat.boilerplate.test.junit;

import com.ata.cpsat.boilerplate.pages.theInternet.HomePage;
import com.ata.cpsat.boilerplate.pages.theInternet.LoginPage;
import com.ata.cpsat.framework.threads.ThreadLocalSEDriver;
import com.ata.cpsat.runner.JUnitSuiteRunner;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TheInternetLoginJunitTest extends JUnitSuiteRunner {

    @Test
    @Order(1)
    public void verifyInvalidLoginCredential() {
        ThreadLocalSEDriver.getDriver().get("https://the-internet.herokuapp.com/login");
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

    @Test
    @Order(2)
    public void verifyValidLoginCredentials() {
        ThreadLocalSEDriver.getDriver().get("https://the-internet.herokuapp.com/login");
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

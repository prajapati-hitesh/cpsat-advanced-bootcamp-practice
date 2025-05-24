package com.ata.cpsat.boilerplate.pages.theInternet;

import com.ata.cpsat.framework.helper.ElementHelper;
import com.ata.cpsat.framework.threads.ThreadLocalSEDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

    @FindBy(id = "flash")
    private WebElement flashErrorLblElement;

    @FindBy(id = "username")
    private WebElement usernameTxtElement;

    @FindBy(id = "password")
    private WebElement passwordTxtElement;

    @FindBy(className = "radius")
    private WebElement loginBtnElement;

    public LoginPage() {
        PageFactory.initElements
                (
                        ThreadLocalSEDriver.getDriver(),
                        this
                );
    }

    public void login(String username, String password) {
        ElementHelper
                .getInstance()
                .sendKeys(usernameTxtElement, username);

        ElementHelper
                .getInstance()
                .sendKeys(passwordTxtElement, password);

        ElementHelper
                .getInstance()
                .click(loginBtnElement);
    }

    public String getErrorMessage() {
        return ElementHelper
                .getInstance()
                .getText(flashErrorLblElement);
    }
}

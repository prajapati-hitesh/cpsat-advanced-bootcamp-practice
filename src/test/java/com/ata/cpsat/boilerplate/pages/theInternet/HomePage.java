package com.ata.cpsat.boilerplate.pages.theInternet;

import com.ata.cpsat.framework.helper.ElementHelper;
import com.ata.cpsat.framework.threads.ThreadLocalSEDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {

    @FindBy(id = "flash")
    private WebElement successLblElement;

    @FindBy(className = "subheader")
    private WebElement subHeaderLblElement;

    @FindBy(xpath = "//a[@href='/logout']")
    private WebElement logoutBtnElement;

    public HomePage() {
        PageFactory
                .initElements
                        (
                                ThreadLocalSEDriver.getDriver(),
                                this
                        );
    }

    public void loadWebsite() {
        ThreadLocalSEDriver.getDriver().get("https://the-internet.herokuapp.com/login");
    }

    public String getSuccessMessage() {
        return ElementHelper
                .getInstance()
                .getText(successLblElement);
    }

    public void logout() {
        ElementHelper
                .getInstance()
                .click(logoutBtnElement);
    }

    public String getHeaderText() {
        return ElementHelper
                .getInstance()
                .getText(subHeaderLblElement);
    }
}

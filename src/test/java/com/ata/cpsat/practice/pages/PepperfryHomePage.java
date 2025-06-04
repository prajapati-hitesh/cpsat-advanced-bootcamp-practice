package com.ata.cpsat.practice.pages;

import com.ata.cpsat.framework.helper.ElementHelper;
import com.ata.cpsat.framework.helper.SyncHelper;
import com.ata.cpsat.framework.threads.ThreadLocalSEDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class PepperfryHomePage {
    By searchInputBy = By.id("search");
    By autoSuggestListBy = By.id("site_auto_suggest");
    By searchProductButtonBy = By.id("searchButton");
    By searchResultsForLabelBy = By.xpath("//span[text()='Search Results for']");
    By signUpPopUpBy = By.id("regPopUp");

    public PepperfryHomePage loadPepperfry() {
        ThreadLocalSEDriver.getDriver().get("https://www.pepperfry.com/");
        return this;
    }

    public PepperfryProductViewPage searchProducts(String keyword) {
        ElementHelper.getInstance().sendKeys(searchInputBy, keyword);

        // Wait until auto suggestion is visible
        SyncHelper.waitUntilElementVisible(
                ThreadLocalSEDriver.getDriver(),
                autoSuggestListBy,
                Duration.ofMinutes(2)
        );

        // Get List of Li
        List<WebElement> matchingKeywordElement = ThreadLocalSEDriver.getDriver()
                .findElement(autoSuggestListBy)
                .findElements(By.tagName("li"))
                .stream().filter(WebElement::isDisplayed)
                .filter(element -> ElementHelper.getInstance().getText(element).equalsIgnoreCase(keyword))
                .collect(Collectors.toList());

        if (matchingKeywordElement.size() > 0) {
            ElementHelper.getInstance().click(matchingKeywordElement.get(0));
        } else {
            // Click on Search
            ElementHelper.getInstance().click(searchProductButtonBy);
        }

        // Wait until search result is visible
        SyncHelper.waitUntilElementVisible(ThreadLocalSEDriver.getDriver(), searchResultsForLabelBy, Duration.ofMinutes(2));
        return new PepperfryProductViewPage();
    }


}

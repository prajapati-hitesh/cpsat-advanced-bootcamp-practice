package com.ata.cpsat.practice.pages;

import com.ata.cpsat.framework.helper.ElementHelper;
import com.ata.cpsat.framework.helper.SyncHelper;
import com.ata.cpsat.framework.manager.SeleniumObjectManager;
import com.ata.cpsat.framework.threads.ThreadLocalSEDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PepperfryProductViewPage {
    By productViewSectionBy = By.id("productView");
    By productCardViewBy = By.xpath("//div[@unbxdattr='product']");
    By sortByDropdownBy = By.id("curSortType");
    By sortByOptionsBy = By.id("sortBY");

    //div[@id='productView']//div[contains(@class, 'clip-price-blocks')]


    private List<WebElement> getProductCardElements() {
        return ThreadLocalSEDriver.getDriver()
                .findElement(productViewSectionBy)
                .findElements(productCardViewBy)
                .stream().filter(WebElement::isDisplayed)
                .collect(Collectors.toList());
    }

    public List<WebElement> getOfferPriceElements() {
        List<WebElement> offerPriceElements = new ArrayList<>();
        List<WebElement> cardElements = getProductCardElements()
                .stream()
                .filter(webElement -> webElement.findElement(By.className("clip-offr-price")).isDisplayed())
                .collect(Collectors.toList());

        cardElements.forEach(priceElement -> {
            offerPriceElements.add(priceElement.findElement(By.className("clip-offr-price")));

        });
        return offerPriceElements;
    }

    public List<WebElement> getRetailPriceElements() {
        List<WebElement> retailPriceElements = new ArrayList<>();
        List<WebElement> cardElements = getProductCardElements()
                .stream()
                .filter(webElement -> webElement.findElement(By.className("clip-retail-price")).isDisplayed())
                .collect(Collectors.toList());

        cardElements.forEach(priceElement -> {
            retailPriceElements.add(priceElement.findElement(By.className("clip-retail-price")));
        });
        return retailPriceElements;
    }

    public PepperfryProductViewPage sortBy(SortBy sortBy) {
        ElementHelper.getInstance().click(sortByDropdownBy);

        // wait until dropdown is visible
        SyncHelper.waitUntilElementVisible(ThreadLocalSEDriver.getDriver(), sortByOptionsBy, Duration.ofMinutes(1));

        List<WebElement> sortByOptions = ThreadLocalSEDriver.getDriver()
                .findElement(sortByOptionsBy)
                .findElements(By.tagName("li"))
                .stream().filter(WebElement::isDisplayed)
                .collect(Collectors.toList());

        for (WebElement element : sortByOptions) {
            String currText = ElementHelper.getInstance().getText(element);

            if (currText.trim().equalsIgnoreCase(sortBy.getSortBy())) {
                ElementHelper.getInstance().click(element);
                break;
            }
        }

        // wait for results to be updated
        SeleniumObjectManager.getWaitObject(Duration.ofMinutes(5))
                .until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@id='loaderOverlay']//img")));

        return this;
    }

    public List<Integer> getOfferPrice(List<WebElement> elementList) {
        List<Integer> offerPriceList = new ArrayList<>();
        elementList.forEach(webElement -> {
            String price = ElementHelper.getInstance().getText(webElement);
            offerPriceList.add(Integer.parseInt(price.split("\\p{Sc}")[1].trim()));

        });
        return offerPriceList;
    }

    public List<Integer> getRetailPrice(List<WebElement> elementList) {
        List<Integer> retailPriceList = new ArrayList<>();
        elementList.forEach(webElement -> {
            String price = ElementHelper.getInstance().getText(webElement);
            retailPriceList.add(Integer.parseInt(price.split("\\p{Sc}")[1].trim()));

        });
        return retailPriceList;
    }

    public enum SortBy {
        RELEVANCE("Relevance"),
        NEWEST_FIRST("Newest first"),
        PRICE_LOW_TO_HIGH("Price Low to High"),
        PRICE_HIGH_TO_LOW("Price High to Low"),
        FASTEST_SHIPPING("Fastest Shipping");

        final String sortBy;

        SortBy(String text) {
            this.sortBy = text;
        }

        public String getSortBy() {
            return sortBy;
        }
    }
}

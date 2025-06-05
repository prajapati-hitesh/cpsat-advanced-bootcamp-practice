package com.ata.cpsat.practice.pages.setA;

import com.ata.cpsat.framework.helper.ElementHelper;
import com.ata.cpsat.framework.helper.SyncHelper;
import com.ata.cpsat.framework.manager.SeleniumObjectManager;
import com.ata.cpsat.framework.threads.ThreadLocalSEDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javatuples.Pair;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class MockSetAListOfParticipantsPage {
    private static final Logger logger = LogManager.getLogger(MockSetAListOfParticipantsPage.class);
    private final By listOfParticipantsHeader = By.className("entry-title");
    private final By searchInput = By.xpath("//input[@aria-label='Search in Table']");
    private final By searchButton = By.xpath("//button[@aria-label='Search']");

    private void performSearch(String searchParameter) {
        SeleniumObjectManager.getWaitObject(Duration.ofSeconds(60))
                .until(ExpectedConditions.and(
                        ExpectedConditions.visibilityOfElementLocated(searchInput),
                        ExpectedConditions.textToBePresentInElementLocated(listOfParticipantsHeader, "List of Participants")
                ));
        if (ElementHelper.getInstance().isElementDisplayed(By.className("fooicon-remove"))) {
            ElementHelper.getInstance().click(searchButton);
            SyncHelper.hardWait(Duration.ofSeconds(1));
        }
        // search for the participants
        ElementHelper.getInstance().sendKeys(searchInput, searchParameter);
        ElementHelper.getInstance().click(searchButton);
        SyncHelper.hardWait(Duration.ofSeconds(3));
    }

    public List<String> searchParticipantName(String participantName) {
        List<String> matchedParticipants = new ArrayList<>();
        // search using participants name
        performSearch(participantName);

        List<WebElement> rowElements = ThreadLocalSEDriver.getDriver()
                .findElement(By.tagName("table"))
                .findElement(By.tagName("tbody"))
                .findElements(By.tagName("tr"))
                .stream().filter(WebElement::isDisplayed)
                .toList();

        if (!rowElements.isEmpty()) {
            for (WebElement eachRowEle : rowElements) {
                // get columns
                List<WebElement> colElements = eachRowEle.findElements(By.tagName("td"))
                        .stream().filter(WebElement::isDisplayed)
                        .toList();

                if (!colElements.isEmpty()) {
                    String actualParticipantName = ElementHelper.getInstance().getText(colElements.get(1));
                    if (actualParticipantName.toUpperCase().contains(participantName.toUpperCase())) {
                        matchedParticipants.add(actualParticipantName);
                    }
                }

                // To Do: implement logic to navigate between pages when record are more and get their text
            }
        }
        return matchedParticipants;
    }

    public List<String> searchParticipantDesignation(String participantName) {
        List<String> matchedDesignation = new ArrayList<>();
        // search using participants name
        performSearch(participantName);

        List<WebElement> rowElements = ThreadLocalSEDriver.getDriver()
                .findElement(By.tagName("table"))
                .findElement(By.tagName("tbody"))
                .findElements(By.tagName("tr"))
                .stream().filter(WebElement::isDisplayed)
                .toList();

        if (!rowElements.isEmpty()) {
            for (WebElement eachRowEle : rowElements) {
                // get columns
                List<WebElement> colElements = eachRowEle.findElements(By.tagName("td"))
                        .stream().filter(WebElement::isDisplayed)
                        .toList();

                // get value from designation column and add it to list
                matchedDesignation.add(ElementHelper.getInstance().getText(colElements.get(2)));

                // To Do: implement a logic to return only those designation where the name matches the search string

                // To Do: Also implement logic to navigate between pages when record are more and get their text
            }
        }
        return matchedDesignation;
    }

    public List<Pair<String, String>> searchAndGetActualParticipantDetails(String name, String designation, String organization) {

        // search using participants name
        performSearch(name);

        // get matching row elements
        List<WebElement> matchingElement = ThreadLocalSEDriver.getDriver()
                .findElements(By.xpath("//td[normalize-space()='" + name + "']//parent::tr"))
                .stream().filter(WebElement::isDisplayed).toList();

        List<WebElement> headerElements = ThreadLocalSEDriver.getDriver()
                .findElements(By.className("footable-header"))
                .stream().filter(WebElement::isDisplayed).toList();

        if (!matchingElement.isEmpty()) {
            List<Pair<String, String>> result = new ArrayList<>();
            for (WebElement row : matchingElement) {
                List<WebElement> columns = row.findElements(By.tagName("td"))
                        .stream().filter(WebElement::isDisplayed).toList();

                // validate values
                String actualName = ElementHelper.getInstance().getText(columns.get(1));
                String actualDesignation = ElementHelper.getInstance().getText(columns.get(2));
                String actualOrg = ElementHelper.getInstance().getText(columns.get(3));


                result.add(Pair.with("Name", actualName));
                result.add(Pair.with("Designation", actualDesignation));
                result.add(Pair.with("Organization", actualOrg));
            }
            return result;

        } else {
            return List.of(Pair.with("Fail", "No records found matching participant name [" + name + "]"));
        }
    }

    public boolean isIsAllColValueMatches(String name, String designation, String orgName) {
        List<Pair<String, String>> result = searchAndGetActualParticipantDetails(name, designation, orgName);

        result.forEach(e -> {
            logger.info("{} | {}", e.getValue0(), e.getValue1());
        });

        boolean isNameMatches = result.get(0).getValue1().equalsIgnoreCase(name);
        boolean isDesignationMatches = result.get(1).getValue1().equalsIgnoreCase(designation);
        boolean isOrgMatches = result.get(2).getValue1().equalsIgnoreCase(orgName);

        return isNameMatches && isDesignationMatches && isOrgMatches;
    }
}

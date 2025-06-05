package com.ata.cpsat.practice.tests.junit.SetA;

import com.ata.cpsat.framework.manager.SeleniumObjectManager;
import com.ata.cpsat.framework.threads.ThreadLocalSEDriver;
import com.ata.cpsat.practice.enums.MockExamSetAMenu;
import com.ata.cpsat.practice.pages.setA.MockSetAHomePage;
import com.ata.cpsat.practice.pages.setA.MockSetAListOfParticipantsPage;
import com.ata.cpsat.runner.JUnitSuiteRunner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.List;

public class QuestionThreeTests extends JUnitSuiteRunner {
    private static final Logger logger = LogManager.getLogger(QuestionThreeTests.class);

    @Test
    public void searchAndReturnParticipantDetails() {
        ThreadLocalSEDriver.getDriver().get("https://mockexam1cpsat.agiletestingalliance.org/");

        // wait until the popup [CP-SAT Selenium Summit 2021] is visible. Close it once in frame
        SeleniumObjectManager.getWaitObject(Duration.ofSeconds(60))
                .until(ExpectedConditions.visibilityOfElementLocated(By.className("eicon-close")))
                .click();

        MockSetAListOfParticipantsPage participantsPage = new MockSetAListOfParticipantsPage();
        // open participants page
        new MockSetAHomePage().navigateTo(MockExamSetAMenu.LIST_OF_PARTICIPANTS);
        // search and get matching participant names
        List<String> matchingParticipants = participantsPage.searchParticipantName("Ch");

        logger.info("---------------------------- Matching Participants ----------------------------");
        matchingParticipants.forEach(logger::info);

        // search and get matching designation
        List<String> matchingDesignation = participantsPage.searchParticipantDesignation("Ch");

        logger.info("---------------------------- Matching Designation ----------------------------");
        matchingDesignation.forEach(logger::info);
    }
}

package com.ata.cpsat.framework.helper;

import com.ata.cpsat.framework.manager.SeleniumObjectManager;
import com.ata.cpsat.framework.threads.ThreadLocalSEDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.MoveTargetOutOfBoundsException;

public class ActionHelper {
    public static void moveToElement(WebElement elementToMove) {
        try {
            new Actions(ThreadLocalSEDriver.getDriver())
                    .moveToElement(elementToMove)
                    .build()
                    .perform();
        } catch (MoveTargetOutOfBoundsException
                | ElementNotInteractableException
                | StaleElementReferenceException ex) {
            SeleniumObjectManager
                    .getJSExecutor()
                    .executeScript("arguments[0].scrollIntoView(true);", elementToMove);
        }
    }

    public static void moveToElement(By by) {
        WebElement elementToMove = ThreadLocalSEDriver.getDriver()
                .findElement(by);

        try {
            new Actions(ThreadLocalSEDriver.getDriver())
                    .moveToElement(elementToMove)
                    .build()
                    .perform();
        } catch (MoveTargetOutOfBoundsException
                | ElementNotInteractableException
                | StaleElementReferenceException ex) {
            SeleniumObjectManager
                    .getJSExecutor()
                    .executeScript("arguments[0].scrollIntoView(true);", elementToMove);
        }
    }

    public static void scrollToTop() {
        // Scroll to top of the page
        new Actions(ThreadLocalSEDriver.getDriver())
                .sendKeys(Keys.HOME)
                .build()
                .perform();
    }
}

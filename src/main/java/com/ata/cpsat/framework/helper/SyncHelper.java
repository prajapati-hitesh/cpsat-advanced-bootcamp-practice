package com.ata.cpsat.framework.helper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class SyncHelper {
    private static final Logger logger = LogManager.getLogger(SyncHelper.class.getName());

    public SyncHelper() {
        // To Do
    }

    public static void hardWait(Duration duration) {
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    /**
     * To wait until element is visible.
     *
     * @param driver   Object of {@link WebDriver}
     * @param element  {@link WebElement} to wait for
     * @param duration Timeout in seconds to wait for
     */
    public static void waitUntilElementVisible(WebDriver driver, WebElement element, Duration duration) {
        new WebDriverWait(driver, duration)
                .until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * To wait until the element is visible by {@link By}
     *
     * @param driver   Object of {@link WebDriver}
     * @param by       {@link By} to wait for
     * @param duration Timeout in seconds to wait for
     */
    public static void waitUntilElementVisible(WebDriver driver, By by, Duration duration) {
        new WebDriverWait(driver, duration)
                .until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    /**
     * To wait until all the element is visible by {@link By}
     *
     * @param driver   Object of {@link WebDriver}
     * @param by       {@link By} to wait for
     * @param duration Timeout in seconds to wait for
     */
    public static void waitUntilVisibilityOfAllElementsLocated(WebDriver driver, By by, Duration duration) {
        new WebDriverWait(driver, duration)
                .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
    }

    /**
     * To wait until all elements visible
     *
     * @param driver   Object of {@link WebDriver}
     * @param elements List of elements to be visible
     * @param duration Timeout in seconds to wait for
     */
    public static void waitUntilVisibilityOfAllElementsLocated(WebDriver driver, List<WebElement> elements, Duration duration) {
        new WebDriverWait(driver, duration)
                .until(ExpectedConditions.visibilityOfAllElements(elements));
    }

    /**
     * To wait until element is present in DOM.
     *
     * @param driver   Object of {@link WebDriver}
     * @param by       {@link By} to wait for
     * @param duration Timeout in seconds to wait for
     */
    public static void waitUntilElementPresence(WebDriver driver, By by, Duration duration) {
        new WebDriverWait(driver, duration)
                .until(ExpectedConditions.presenceOfElementLocated(by));
    }

    /**
     * To wait until a specific text is visible in an {@link WebElement}
     *
     * @param driver        Object of {@link WebDriver}
     * @param element       {@link WebElement} at which to wait for a text
     * @param textToWaitFor Text to wait for in a element
     * @param duration      Timeout in seconds to wait for
     */
    public static void waitUntilTextToBePresentInElement(WebDriver driver, WebElement element, String textToWaitFor, Duration duration) {
        new WebDriverWait(driver, duration)
                .until(ExpectedConditions.textToBePresentInElement(element, textToWaitFor));
    }

    /**
     * To wait until Element is clickable
     *
     * @param driver   Object of {@link WebDriver}
     * @param element  {@link WebElement} to wait for
     * @param duration Timeout in seconds to wait for
     */
    public static void waitUntilElementClickable(WebDriver driver, WebElement element, Duration duration) {
        new WebDriverWait(driver, duration)
                .until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * To wait until Element is clickable by {@link By}
     *
     * @param driver   Object of {@link WebDriver}
     * @param by       {@link By} to wait for
     * @param duration Timeout in seconds to wait for
     */
    public static void waitUntilElementClickable(WebDriver driver, By by, Duration duration) {
        new WebDriverWait(driver, duration)
                .until(ExpectedConditions.elementToBeClickable(by));
    }
}

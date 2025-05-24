package com.ata.cpsat.framework.helper;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.Objects;

public class ExpectedConditionsHelper {

    private static final Logger logger = LogManager.getLogger(ExpectedConditions.class.getName());

    private ExpectedConditionsHelper() {
        // To Do
    }

    /**
     * An expectation of checking of an element having a particular text and has length > 0.
     *
     * @param locator Used to find an element
     * @param text    to be present in element found by locator
     * @return true, if the first element located by locator contains text and has length > 0, false otherwise
     */
    public static ExpectedCondition<Boolean> textToBePresentAndHasLengthInElementLocated(By locator, final String text) {
        return webDriver -> {
            try {
                String elementText = Objects.requireNonNull(webDriver).findElement(locator).getText();

                if (!StringUtils.isBlank(elementText)) {
                    int textLength = elementText.length();
                    return StringUtils.normalizeSpace(elementText).contains(StringUtils.normalizeSpace(text)) && textLength > 0;
                } else {
                    return false;
                }
            } catch (NoSuchElementException | StaleElementReferenceException tEx) {
                logger.error(tEx.getMessage(), tEx);
                return null;
            }
        };
    }

    /**
     * An expectation of checking length of an element
     *
     * @param locator Used to find an element
     * @return true, if the first element located by locator has length > 0, false otherwise
     */
    public static ExpectedCondition<Boolean> textHasLengthInElementLocated(By locator) {
        return webDriver -> {
            try {
                String elementText = Objects.requireNonNull(webDriver).findElement(locator).getText();

                if (!StringUtils.isBlank(elementText)) {
                    int length = elementText.length();
                    return length > 0;
                } else {
                    return false;
                }
            } catch (NoSuchElementException | StaleElementReferenceException tEx) {
                logger.error(tEx.getMessage(), tEx);
                return null;
            }
        };
    }

    /**
     * To wait until Value of an Element is Greater than value provided as an argument or not
     *
     * @param locator        Used to find an element
     * @param valueToCompare Value to compare
     * @return true, if the first element located by has value > valueToCompare, false otherwise
     */
    public static ExpectedCondition<Boolean> valueOfElementLocatedByShouldBeGreaterThan(By locator, double valueToCompare) {
        return webDriver -> {
            try {
                String elementText = Objects
                        .requireNonNull(webDriver)
                        .findElement(locator)
                        .getText();

                if (!StringUtils.isBlank(elementText)) {

                    double valueInDouble = NumberUtils
                            .toDouble(elementText);

                    return valueInDouble > valueToCompare;
                } else {
                    return false;
                }
            } catch (NoSuchElementException | StaleElementReferenceException tEx) {
                logger.error(tEx.getMessage(), tEx);
                return null;
            }
        };
    }
}

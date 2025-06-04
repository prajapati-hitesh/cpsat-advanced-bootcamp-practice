package com.ata.cpsat.practice.pages;

import com.ata.cpsat.framework.manager.SeleniumObjectManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

public class CPSatMockExamPage {
    By cpSatLogoImageBy = By.className("elementor-image");
    By cpSatJavaImageToolTipBy = By.id("tippy-1");
    By cpSatPythonImageToolTipBy = By.id("tippy-2");
    By cpSatCSharpImageToolTipBy = By.id("tippy-3");
    By cpSatLogoToolTipContentBy = By.className("tippy-content");

    public CPSatMockExamPage hover(WebElement element) {
        SeleniumObjectManager.getActionObject()
                .moveToElement(element)
                .pause(Duration.ofSeconds(2))
                .build()
                .perform();
        return this;
    }

    public WebElement cpSatLogoElement(CPSat cpSat) {
        return switch (cpSat) {
            case JAVA -> SeleniumObjectManager.getWaitObject(Duration.ofSeconds(60))
                    .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(cpSatLogoImageBy))
                    .get(0);

            case PYTHON -> SeleniumObjectManager.getWaitObject(Duration.ofSeconds(60))
                    .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(cpSatLogoImageBy))
                    .get(1);

            case C_SHARP -> SeleniumObjectManager.getWaitObject(Duration.ofSeconds(60))
                    .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(cpSatLogoImageBy))
                    .get(2);
        };
    }

    public String getToolTip(CPSat cpSat) {
        return switch (cpSat) {
            case JAVA -> SeleniumObjectManager.getWaitObject(Duration.ofSeconds(60))
                    .until(ExpectedConditions.visibilityOfElementLocated(cpSatJavaImageToolTipBy))
                    .findElement(cpSatLogoToolTipContentBy)
                    .getText();
            case PYTHON -> SeleniumObjectManager.getWaitObject(Duration.ofSeconds(60))
                    .until(ExpectedConditions.visibilityOfElementLocated(cpSatPythonImageToolTipBy))
                    .findElement(cpSatLogoToolTipContentBy)
                    .getText();
            case C_SHARP -> SeleniumObjectManager.getWaitObject(Duration.ofSeconds(60))
                    .until(ExpectedConditions.visibilityOfElementLocated(cpSatCSharpImageToolTipBy))
                    .findElement(cpSatLogoToolTipContentBy)
                    .getText();
        };
    }

    public enum CPSat {
        JAVA,
        C_SHARP,
        PYTHON
    }
}

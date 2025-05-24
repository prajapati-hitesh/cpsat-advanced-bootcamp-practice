package com.ata.cpsat.framework.manager;

import com.ata.cpsat.framework.threads.ThreadLocalSEDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SeleniumObjectManager {
    /**
     * To check whether the driver session is created or not
     *
     * @return true if driver session created, false otherwise
     */
    public static boolean isDriverSessionCreated() {
        return ThreadLocalSEDriver.getDriver() != null;
    }

    /**
     * To get Object of {@link WebDriverWait}
     *
     * @param duration {@link Duration} Timeout in seconds to wait for
     * @return {@link WebDriverWait}
     */
    public static WebDriverWait getWaitObject(Duration duration) {
        return new WebDriverWait(ThreadLocalSEDriver.getDriver(), duration);
    }

    /**
     * To get Object of {@link FluentWait}.
     *
     * <p> Exception to be ignored. </p>
     * <p> 1. {@link StaleElementReferenceException} </p>
     * <p> 2. {@link TimeoutException} </p>
     * <p> 3. {@link NoSuchElementException} </p>
     *
     * @param timeOutDuration Timeout in seconds to wait for
     * @param pollingDuration Poll in every N milliseconds
     * @return {@link FluentWait}
     */
    public static FluentWait<WebDriver> getFluentWait(Duration timeOutDuration, Duration pollingDuration) {
        return new FluentWait<>(ThreadLocalSEDriver.getDriver())
                .withTimeout(timeOutDuration)
                .pollingEvery(pollingDuration)
                .ignoring(TimeoutException.class)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(NoSuchElementException.class);
    }

    /**
     * To get the instance of {@link JavascriptExecutor}
     *
     * @return {@link JavascriptExecutor} object
     */
    public static JavascriptExecutor getJSExecutor() {
        return (JavascriptExecutor) ThreadLocalSEDriver.getDriver();
    }

    /**
     * To get instance of {@link Actions}
     *
     * @return {@link Actions}
     */
    public static Actions getActionObject() {
        return new Actions(ThreadLocalSEDriver.getDriver());
    }
}

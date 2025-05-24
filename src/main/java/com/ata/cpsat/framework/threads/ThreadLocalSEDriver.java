package com.ata.cpsat.framework.threads;

import org.openqa.selenium.WebDriver;

/**
 * Class to manage thread safe object of {@link  WebDriver}
 *
 * @author Hitesh Prajapati
 * @version %I%, %G%
 * @since 1.0-SNAPSHOT
 */
public class ThreadLocalSEDriver {
    private static final ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();

    /**
     * To get Thread Safe object of WebDriver
     *
     * @return <code>WebDriver</code> <b>Thread safe object of WebDriver</b>
     */
    public static WebDriver getDriver() {
        return tlDriver.get();
    }

    /**
     * To set Thread Safe object of WebDriver
     *
     * @param driverInstance <code>WebDriver</code> <b>Object of WebDriver instance</b>
     */
    public static void setDriver(WebDriver driverInstance) {
        tlDriver.set(driverInstance);
    }

}

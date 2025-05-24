package com.ata.cpsat.framework.driver;

import com.ata.cpsat.framework.enums.Browser;
import com.ata.cpsat.framework.threads.ThreadLocalSEDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;

import java.time.Duration;

/**
 * Class to manage {@link WebDriver}
 *
 * @author Hitesh Prajapati
 * @version %I%, %G%
 * @since 1.0
 */
public class DriverFactory {

    private static final Logger logger = LogManager.getLogger(DriverFactory.class.getName());
    private static final ThreadLocal<DriverFactory> instance = new ThreadLocal<>();
    private FirefoxOptions firefoxOptions;
    private ChromeOptions chromeOptions;

    /**
     * To get static instance of Driver Factory Class
     *
     * @return <code>DriverFactory</code>
     */
    public static DriverFactory getInstance() {
        if (instance.get() == null) {
            instance.set(new DriverFactory());
        }
        return instance.get();
    }

    /**
     * To set the Object of <code>ChromeOptions</code> before initializing
     * browser instance
     *
     * @param optionsAs Object of type <code>ChromeOptions</code>
     * @return <code>DriverFactory</code>
     */
    public DriverFactory withBrowserOptionsAs(ChromeOptions optionsAs) {
        this.chromeOptions = new ChromeOptions();
        chromeOptions = optionsAs;
        return this;
    }

    /**
     * To set the Object of <code>FirefoxOptions</code> before initializing
     * browser instance
     *
     * @param firefoxOptionsAs Object of type <code>FirefoxOptions</code>
     * @return <code>DriverFactory</code>
     */
    public DriverFactory withBrowserOptionsAs(FirefoxOptions firefoxOptionsAs) {
        this.firefoxOptions = new FirefoxOptions();
        firefoxOptions = firefoxOptionsAs;
        return this;
    }

    /**
     * @param browserOptionsAs Either {@link FirefoxOptions} or {@link @ChromeOptions}
     * @param <T>
     * @return
     */
    public <T> DriverFactory withBrowserOptionsAs(T browserOptionsAs) {
        if (browserOptionsAs instanceof ChromeOptions) {
            this.chromeOptions = new ChromeOptions();
            chromeOptions = (ChromeOptions) browserOptionsAs;
        } else if (browserOptionsAs instanceof FirefoxOptions) {
            this.firefoxOptions = new FirefoxOptions();
            firefoxOptions = (FirefoxOptions) browserOptionsAs;
        }
        return this;
    }

    /**
     * To set Implicit wait for <code>WebDriver</code> instance.
     *
     * @param duration {@link Duration}
     * @return <code>DriverFactory</code>
     */
    public DriverFactory setImplicitTimeoutAs(Duration duration) {
        if (ThreadLocalSEDriver.getDriver() != null) {
            ThreadLocalSEDriver.getDriver().manage().timeouts().implicitlyWait(duration);
        } else {
            throw new NullPointerException("WebDriver instance is not created. Please call initBrowser() before setImplicitTimeout method");
        }
        return this;
    }

    /**
     * To set Asynchronous script to time out after specified time
     *
     * @param duration {@link Duration}
     * @return <code>DriverFactory</code>
     */
    public DriverFactory setScriptTimeoutAs(Duration duration) {
        if (ThreadLocalSEDriver.getDriver() != null) {
            ThreadLocalSEDriver.getDriver().manage().timeouts().scriptTimeout(duration);
        } else {
            throw new NullPointerException("WebDriver instance is not created. Please call initBrowser() before setScriptTimeout method");
        }
        return this;
    }

    /**
     * To set page load timeout
     *
     * @param duration {@link Duration}
     * @return <code>DriverFactory</code>
     */
    public DriverFactory setPageLoadTimeoutAs(Duration duration) {
        if (ThreadLocalSEDriver.getDriver() != null) {
            ThreadLocalSEDriver.getDriver().manage().timeouts().pageLoadTimeout(duration);
        } else {
            throw new NullPointerException("WebDriver instance is not created. Please call initBrowser() before setPageLoadTimeout method");
        }
        return this;
    }

    /**
     * To download binary for the browser specified as parameter
     * and create instance of it
     *
     * @param browser Browser to initialize of type <code>Browser</code>
     * @return <code>DriverFactory</code>
     */
    public DriverFactory initBrowser(Browser browser) {
        switch (browser) {
            case IE:
                WebDriverManager.iedriver().setup();
                InternetExplorerOptions ieOptions = new InternetExplorerOptions();
                ieOptions.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
                ieOptions.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
                ThreadLocalSEDriver
                        .setDriver(new InternetExplorerDriver(ieOptions));
                break;
            case Edge:
                WebDriverManager.edgedriver().setup();
                ThreadLocalSEDriver.setDriver(new EdgeDriver());
                break;
            case Firefox:
                WebDriverManager.firefoxdriver().setup();
                if (firefoxOptions != null) {
                    ThreadLocalSEDriver
                            .setDriver(new FirefoxDriver(firefoxOptions));
                } else {
                    ThreadLocalSEDriver
                            .setDriver(new FirefoxDriver());
                }

                break;
            case Chromium:
                WebDriverManager.chromiumdriver().setup();
                ThreadLocalSEDriver.setDriver(new ChromeDriver());
                break;
            default:
                WebDriverManager.chromedriver().setup();
                if (chromeOptions != null) {
                    ThreadLocalSEDriver
                            .setDriver(new ChromeDriver(chromeOptions));
                } else {
                    ThreadLocalSEDriver
                            .setDriver(new ChromeDriver());
                }

                break;
        }

        // Maximize browser
        ThreadLocalSEDriver.getDriver().manage().window().maximize();
        return this;
    }


    /**
     * To set the Web Url.
     *
     * @param webUrl Web Url of Application
     */
    public void setUrl(String webUrl) {
        ThreadLocalSEDriver.getDriver().get(webUrl);
    }

    /**
     * To close the browser and clean the driver service.
     */
    public void closeBrowser() {
        try {
            if (ThreadLocalSEDriver.getDriver() != null) {
                ThreadLocalSEDriver
                        .getDriver()
                        .close();

                if (ThreadLocalSEDriver.getDriver() instanceof ChromeDriver) {
                    ThreadLocalSEDriver
                            .getDriver()
                            .quit();
                }
            }
        } catch (WebDriverException ex) {
            logger.error(ex.getMessage(), ex);
        }

        // set value to null
        ThreadLocalSEDriver
                .setDriver(null);
    }
}

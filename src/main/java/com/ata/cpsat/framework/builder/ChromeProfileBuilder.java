package com.ata.cpsat.framework.builder;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to manage profile specific to {@link org.openqa.selenium.chrome.ChromeDriver}
 *
 * @author Hitesh Prajapati
 * @version %I%, %G%
 * @since 1.0-SNAPSHOT
 */
public class ChromeProfileBuilder {

    private static final ThreadLocal<ChromeProfileBuilder> instance = new ThreadLocal<>();
    private final ChromeOptions chromeOptions;
    private final Map<String, Object> chromePrefs;

    private ChromeProfileBuilder() {
        // To Do
        this.chromeOptions = new ChromeOptions();
        this.chromePrefs = new HashMap<>();
    }

    /**
     * Get Instance of ChromeProfile to build ChromeOptions
     *
     * @return <code>ChromeProfile</code>
     */
    public static ChromeProfileBuilder getInstance() {
        if (instance.get() == null) {
            instance.set(new ChromeProfileBuilder());
        }
        return instance.get();
    }

    /**
     * To get instance of {@link ChromeOptions} from the capability
     * build using {@link ChromeProfileBuilder} instance.
     *
     * @return <code>ChromeOptions</code>
     */
    public ChromeOptions build() {
        // If ChromePrefs object has value > 0 then attach it to chrome options
        if (!chromePrefs.isEmpty()) {
            chromeOptions.setExperimentalOption("prefs", chromePrefs);
        }
        return chromeOptions;
    }

    /**
     * To disable extensions for Chrome
     *
     * @return <code>ChromeProfile</code>
     */
    public ChromeProfileBuilder disabledExtension() {
        chromeOptions.addArguments("--disable-extensions");
        return this;
    }

    /**
     * Disables GPU hardware acceleration.
     * If software renderer is not in place, then the GPU process won't launch.
     * Applicable to Windows OS Only
     *
     * @return <code>ChromeProfile</code>
     */
    public ChromeProfileBuilder disabledGPU() {
        chromeOptions.addArguments("--disable-gpu");
        return this;
    }

    /**
     * Disable LocalStorage
     *
     * @return <code>ChromeProfile</code>
     */
    public ChromeProfileBuilder disabledLocalStorage() {
        chromeOptions.addArguments("-disable-local-storage");
        return this;
    }

    /**
     * Disable pop-up blocking
     *
     * @return <code>ChromeProfile</code>
     */
    public ChromeProfileBuilder disablePopUpBlocking() {
        chromeOptions.addArguments("--disable-popup-blocking");
        return this;
    }

    /**
     * Run in headless mode, i.e., without a UI or display server dependencies.
     *
     * @return <code>ChromeProfile</code>
     */
    public ChromeProfileBuilder asHeadless() {
        chromeOptions.addArguments("--headless");
        return this;
    }

    /**
     * Causes the browser to launch directly in incognito mode
     *
     * @return <code>ChromeProfile</code>
     */
    public ChromeProfileBuilder launchIncognito() {
        chromeOptions.addArguments("--incognito");
        return this;
    }

    /**
     * Disables the default browser check.
     * Useful for UI/browser tests where we want to avoid having the default browser info-bar displayed.
     *
     * @return <code>ChromeProfile</code>
     */
    public ChromeProfileBuilder disableDefaultBrowserCheck() {
        chromeOptions.addArguments("--no-default-browser-check");
        return this;
    }

    /**
     * Disables the sandbox for all process types that are normally sandboxed.
     * This options will be used to Bypass OS Security Model
     *
     * @return <code>ChromeProfile</code>
     */
    public ChromeProfileBuilder noSandbox() {
        chromeOptions.addArguments("--no-sandbox");
        return this;
    }

    /**
     * Specifies if the browser should start in fullscreen mode,
     * like if the user had pressed F11 right after startup.
     *
     * @return <code>ChromeProfile</code>
     */
    public ChromeProfileBuilder startFullScreen() {
        chromeOptions.addArguments("--start-fullscreen");
        return this;
    }

    /**
     * Starts the browser maximized, regardless of any previous settings.
     *
     * @return <code>ChromeProfile</code>
     */
    public ChromeProfileBuilder startMaximized() {
        chromeOptions.addArguments("--start-maximized");
        return this;
    }

    /**
     * Sets the initial window size.
     * Provided as string in the format "800,600".
     *
     * @param height <code>String</code> <b>Browser windows height in Pixels</b>
     * @param width  <code>String</code> <b>Browser windows Width in Pixels</b>
     * @return <code>ChromeProfile</code>
     */
    public ChromeProfileBuilder withWindowSizeAs(String height, String width) {
        chromeOptions.addArguments("--window-size=" + width + "," + height);
        return this;
    }


    /**
     * To Disable info bars on chrome which reads
     * "Chrome is controlled by automation software"
     *
     * @return <code>ChromeProfile</code>
     */
    public ChromeProfileBuilder disableInfoBars() {
        chromeOptions.addArguments("enable-automation");
        chromeOptions.addArguments("disable-infobars");
        return this;
    }

    /**
     * Run Chrome browser in silent mode to not show time out logs while execution
     *
     * @return <code>ChromeProfile</code>
     */
    public ChromeProfileBuilder disableChromeLogs() {
        System.setProperty("webdriver.chrome.silentOutput", "true");
        return this;
    }

    /**
     * Initialize Chrome Browser with default download directory passed as an
     * argument to this method.
     *
     * @param downloadDirPath <code>String</code> <b>Absolute path to download files to.</b>
     * @return <code>ChromeProfile</code>
     */
    public ChromeProfileBuilder downloadDirectoryAs(String downloadDirPath) {
        chromePrefs.put("download.default_directory", downloadDirPath);
        return this;
    }

    /**
     * To load chrome with extension
     *
     * @param pathToCRXFile <code>String</code> <b>Path to *.crx file</b>
     * @return <code>ChromeProfile</code>
     */
    public ChromeProfileBuilder withExtension(String pathToCRXFile) {
        chromeOptions.addExtensions(new File(pathToCRXFile));
        return this;
    }

    /**
     * To Load chrome with more than one extension
     *
     * @param pathToCRXFile <code>List{@literal <}String{@literal >}</code> <b>List of path of more than one *.crx file</b>
     * @return <code>ChromeProfile</code>
     */
    public ChromeProfileBuilder withExtensions(List<String> pathToCRXFile) {
        pathToCRXFile.forEach(crxPath -> chromeOptions.addExtensions(new File(crxPath)));
        return this;
    }

    /**
     * To ignore SSL error when trying to access non-ssl websites
     *
     * @return <code>ChromeProfile</code>
     */
    public ChromeProfileBuilder ignoreSSLErrors() {
        chromeOptions.addArguments("--ignore-certificate-errors");
        return this;
    }

    /**
     * To set the page load strategy used by Selenium. Default values is Normal <br>
     * <ul>
     *     <li><b>Normal:</b> This strategy causes Selenium to wait for the full page loading (html content and sub resources downloaded and parsed)</li>
     *     <li><b>Eager:</b> This strategy causes Selenium to wait for the DOMContentLoaded event (html content downloaded and parsed only).</li>
     *     <li><b>None:</b> This strategy causes Selenium to return immediately after the initial page content is fully received (html content downloaded).</li>
     * </ul>
     *
     * @param pageLoadStrategy {@link PageLoadStrategy}
     * @return {@link ChromeProfileBuilder}
     */
    public ChromeProfileBuilder setPageLoadStrategy(PageLoadStrategy pageLoadStrategy) {
        chromeOptions.setPageLoadStrategy(pageLoadStrategy);
        return this;
    }

}

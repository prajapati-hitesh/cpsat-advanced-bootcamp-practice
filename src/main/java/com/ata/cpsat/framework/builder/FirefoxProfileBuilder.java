package com.ata.cpsat.framework.builder;

import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.io.File;
import java.util.List;

public class FirefoxProfileBuilder {

    private static final ThreadLocal<FirefoxProfileBuilder> instance = new ThreadLocal<>();
    private final FirefoxOptions firefoxOptions;
    private final FirefoxProfile ffProfile;

    private FirefoxProfileBuilder() {
        // To Do
        this.firefoxOptions = new FirefoxOptions();
        this.ffProfile = new FirefoxProfile();
    }

    /**
     * Get Instance of FireFoxProfile to build FirefoxOptions
     *
     * @return <code>firefoxProfile</code>
     */
    public static FirefoxProfileBuilder getInstance() {
        if (instance.get() == null) {
            instance.set(new FirefoxProfileBuilder());
        }
        return instance.get();
    }

    /**
     * To get instance of {@link FirefoxOptions}
     *
     * @return <code>FirefoxOptions</code>
     */
    public FirefoxOptions build() {
        firefoxOptions.setProfile(ffProfile);
        return firefoxOptions;
    }

    /**
     * Disable pop-up blocking
     *
     * @return <code>firefoxProfile</code>
     */
    public FirefoxProfileBuilder disablePopUpBlocking() {
        firefoxOptions.addPreference("dom.disable_beforeunload", true);
        return this;
    }

    /**
     * Run in headless mode, i.e., without a UI or display server dependencies.
     *
     * @return <code>firefoxProfile</code>
     */
    public FirefoxProfileBuilder asHeadless() {
        firefoxOptions.addArguments("-headless");
        return this;
    }

    /**
     * Causes the browser to launch directly in incognito mode
     *
     * @return <code>firefoxProfile</code>
     */
    public FirefoxProfileBuilder launchIncognito() {
        firefoxOptions.addPreference("browser.privatebrowsing.autostart", true);
        return this;
    }

    /**
     * Disables the default browser check.
     * Useful for UI/browser tests where we want to avoid having the default browser info-bar displayed.
     *
     * @return <code>firefoxProfile</code>
     */
    public FirefoxProfileBuilder disableDefaultBrowserCheck() {
        firefoxOptions.addPreference("browser.shell.checkDefaultBrowser", false);
        return this;
    }


    /**
     * Sets the initial window size.
     * Provided as string in the format "800,600".
     *
     * @param height <code>String</code> <b>Browser windows height in Pixels</b>
     * @param width  <code>String</code> <b>Browser windows Width in Pixels</b>
     * @return <code>firefoxProfile</code>
     */
    public FirefoxProfileBuilder withWindowSizeAs(String height, String width) {
        firefoxOptions.addArguments("privacy.window.maxInnerHeight", height);
        firefoxOptions.addArguments("privacy.window.maxInnerWidth", width);
        return this;
    }

    /**
     * To ignore SSL error when trying to access non-ssl websites
     *
     * @return <code>firefoxProfile</code>
     */
    public FirefoxProfileBuilder ignoreSSLErrors() {
        ffProfile.setAcceptUntrustedCertificates(true);
        return this;
    }

    /**
     * Initialize Chrome Browser with default download directory passed as an
     * argument to this method.
     *
     * @param downloadDirPath <code>String</code> <b>Absolute path to download files to.</b>
     * @return <code>firefoxProfile</code>
     */
    public FirefoxProfileBuilder downloadDirectoryAs(String downloadDirPath) {
        ffProfile.setPreference("browser.download.folderList", 2);
        ffProfile.setPreference("browser.helperApps.alwaysAsk.force", false);

        ffProfile.setPreference("browser.download.dir", downloadDirPath);
        ffProfile.setPreference("browser.download.defaultFolder", downloadDirPath);
        ffProfile.setPreference("browser.download.manager.showWhenStarting", false);
        return this;
    }

    /**
     * To load chrome with extension
     *
     * @param pathToXPIFile <code>String</code> <b>Path to *.xpi file</b>
     * @return <code>firefoxProfile</code>
     */
    public FirefoxProfileBuilder withExtension(String pathToXPIFile) {
        ffProfile.addExtension(new File(pathToXPIFile));
        return this;
    }

    /**
     * To Load chrome with more than one extensions
     *
     * @param pathToXPIFile <code>List{@literal <}String{@literal >}</code> <b>List of path of more than one *.xpi file</b>
     * @return <code>firefoxProfile</code>
     */
    public FirefoxProfileBuilder withExtensions(List<String> pathToXPIFile) {
        pathToXPIFile.forEach(xpiPath -> ffProfile.addExtension(new File(xpiPath)));
        return this;
    }
}


package com.ata.cpsat.runner;

import com.ata.cpsat.framework.builder.ChromeProfileBuilder;
import com.ata.cpsat.framework.driver.DriverFactory;
import com.ata.cpsat.framework.enums.Browser;
import com.ata.cpsat.framework.utility.SystemUtility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.runner.RunWith;
import org.openqa.selenium.chrome.ChromeOptions;


@RunWith(JUnitPlatform.class)
@SelectPackages("com.ata.cpsat.test.junit")
public class JUnitSuiteRunner {
    private static final Logger logger = LogManager.getLogger(JUnitSuiteRunner.class.getName());
    private static final String FILE_SEPARATOR = SystemUtility.getFileSeparator();
    private static final String USER_DIR = SystemUtility.getUserDirectory();
    public static final String SCREENSHOT_DIR = USER_DIR.concat(FILE_SEPARATOR).concat("screenshot").concat(FILE_SEPARATOR);
    public static final String FILE_DIR = USER_DIR.concat(FILE_SEPARATOR).concat("files-directory").concat(FILE_SEPARATOR);

    @BeforeAll
    static void initializeJUnitRunEnvironment() {

        ChromeOptions chromeOptions = ChromeProfileBuilder.getInstance()
                .disableChromeLogs()
                .disableInfoBars()
                .downloadDirectoryAs(SystemUtility.getDefaultTempFilePath())
                .disableDefaultBrowserCheck()
               // .withExtension(USER_DIR.concat(FILE_SEPARATOR).concat("browser-extension").concat(FILE_SEPARATOR).concat("Ultimate AdBlocker_2_2_6_0.crx"))
                .build();

        DriverFactory.getInstance()
                .withBrowserOptionsAs(chromeOptions)
                .initBrowser(Browser.Chrome);

        logger.info("Browser Launched and website Loaded.");
    }

    @AfterAll
    static void cleanUpJUnitRunEnvironment() {
        DriverFactory.getInstance().closeBrowser();
    }

}

package com.ata.cpsat.runner;

import com.ata.cpsat.framework.builder.ChromeProfileBuilder;
import com.ata.cpsat.framework.builder.FirefoxProfileBuilder;
import com.ata.cpsat.framework.driver.DriverFactory;
import com.ata.cpsat.framework.enums.Browser;
import com.ata.cpsat.framework.helper.SyncHelper;
import com.ata.cpsat.framework.utility.SystemUtility;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.time.Duration;

public class TestNgSuiteRunner {
    private static final Logger logger = LogManager.getLogger(TestNgSuiteRunner.class.getName());
    private final String FILE_SEPARATOR = SystemUtility.getFileSeparator();
    private final String USER_DIR = SystemUtility.getUserDirectory();
    public final String SCREENSHOT_DIR = USER_DIR.concat(FILE_SEPARATOR).concat("screenshot").concat(FILE_SEPARATOR);

    @BeforeTest(alwaysRun = true)
    @Parameters({"browser-name"})
    public void initializeTestNgRunEnvironment(@Optional String browserName) {
        final Browser DEFAULT_BROWSER = Browser.Chrome;
        browserName = DEFAULT_BROWSER.getBrowserName();

        ChromeOptions chromeOptions = ChromeProfileBuilder.getInstance()
                .disableChromeLogs()
                .disableInfoBars()
                .downloadDirectoryAs(SystemUtility.getDefaultTempFilePath())
                .disableDefaultBrowserCheck()
                .build();

        FirefoxOptions firefoxOptions = FirefoxProfileBuilder.getInstance()
                .disableDefaultBrowserCheck()
                .downloadDirectoryAs(SystemUtility.getDefaultTempFilePath())
                .build();

        DriverFactory.getInstance()
                .withBrowserOptionsAs((StringUtils.isBlank(browserName) && browserName.trim().equalsIgnoreCase("Firefox") ? firefoxOptions : chromeOptions))
                .initBrowser(StringUtils.isBlank(browserName) ? DEFAULT_BROWSER : Browser.getEnum(browserName.trim()));

        logger.info("Browser Launched and website Loaded.");
    }

    @AfterTest(alwaysRun = true)
    public void cleanUpTestNgRunEnvironment() {
        SyncHelper.hardWait(Duration.ofSeconds(5));
        DriverFactory.getInstance().closeBrowser();
    }
}

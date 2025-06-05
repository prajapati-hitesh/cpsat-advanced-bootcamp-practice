package com.ata.cpsat.framework.helper;

import com.ata.cpsat.framework.enums.LocateBy;
import com.ata.cpsat.framework.enums.TextBy;
import com.ata.cpsat.framework.manager.SeleniumObjectManager;
import com.ata.cpsat.framework.threads.ThreadLocalSEDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Objects;

public class ElementHelper {
    private static final Logger logger = LogManager.getLogger(ElementHelper.class.getName());
    private static final long defaultWaitFor = 60;
    private static final ThreadLocal<ElementHelper> instance = new ThreadLocal<>();

    /**
     * To get instance of {@link ElementHelper}
     *
     * @return {@link ElementHelper}
     */
    public static ElementHelper getInstance() {
        if (instance.get() == null) {
            instance.set(new ElementHelper());
        }
        return instance.get();
    }

    /**
     * To wait for an Element until it's visible on Page and click
     *
     * @param element {@link WebElement} to click on
     */
    public void click(WebElement element) {
        try {
            SyncHelper.waitUntilElementClickable(
                    ThreadLocalSEDriver.getDriver(),
                    element,
                    Duration.ofSeconds(defaultWaitFor)
            );

            // Move to Element
            ActionHelper.moveToElement(element);

            //Highlight Element
            highlightElement(element);

            // Click On Element
            element.click();

            // Remove Highlight
            unhighlightElement(element);
        } catch (TimeoutException tEx) {
            logger.error(tEx.getMessage(), tEx);
        }
    }

    /**
     * To wait for an element by {@link By} Object until it's visible followed by click event
     *
     * @param by {@link By} to search & click on
     */
    public void click(By by) {
        try {
            SyncHelper.waitUntilElementClickable(
                    ThreadLocalSEDriver.getDriver(),
                    by,
                    Duration.ofSeconds(defaultWaitFor)
            );
            WebElement element = ThreadLocalSEDriver
                    .getDriver()
                    .findElement(by);

            // Move to Element
            ActionHelper.moveToElement(element);

            // Highlight element
            highlightElement(element);

            // Click
            element.click();

            // Remove Highlight
            unhighlightElement(element);
        } catch (StaleElementReferenceException sEx) {

            WebElement element = ThreadLocalSEDriver
                    .getDriver()
                    .findElement(by);

            // Move to Element
            ActionHelper.moveToElement(element);

            // Highlight element
            highlightElement(element);

            // Click
            element.click();

            // Remove Highlight
            unhighlightElement(element);
        } catch (TimeoutException | NoSuchElementException tEx) {
            logger.error(tEx.getMessage(), tEx);
        }
    }

    /**
     * To enter a text into a {@link WebElement} but waits for it's visibility before sending keys
     *
     * @param element {@link WebElement} to where the CharSequence to send
     * @param text    Text to set in an element
     */
    public void sendKeys(WebElement element, String text) {
        try {
            SyncHelper.waitUntilElementVisible(
                    ThreadLocalSEDriver.getDriver(),
                    element,
                    Duration.ofSeconds(defaultWaitFor)
            );

            // Move to Element
            ActionHelper.moveToElement(element);

            // Highlight Element
            highlightElement(element);

            element.clear();
            element.sendKeys(text);

            // Unhighlight element
            unhighlightElement(element);
        } catch (TimeoutException tEx) {
            logger.error(tEx.getMessage(), tEx);
        }
    }

    /**
     * To enter text into an element of type {@link By} but waits for it's visibility before sending keys
     *
     * @param by   {@link By} to search
     * @param text <code>String</code> to enter to an element
     */
    public void sendKeys(By by, String text) {
        try {

            SyncHelper.waitUntilElementVisible(
                    ThreadLocalSEDriver.getDriver(),
                    by,
                    Duration.ofSeconds(defaultWaitFor)
            );
            WebElement element = ThreadLocalSEDriver
                    .getDriver()
                    .findElement(by);

            // Move to Element
            ActionHelper.moveToElement(element);

            // Highlight Element
            highlightElement(element);

            element.clear();
            element.sendKeys(text);

            // Remove Highlight
            unhighlightElement(element);
        } catch (StaleElementReferenceException sEx) {
            WebElement element = ThreadLocalSEDriver
                    .getDriver()
                    .findElement(by);

            // Move to Element
            ActionHelper.moveToElement(element);

            // Highlight Element
            highlightElement(element);

            element.clear();
            element.sendKeys(text);

            // Unhighlight Element
            unhighlightElement(element);
        } catch (TimeoutException | NoSuchElementException tEx) {
            logger.error(tEx.getMessage(), tEx);
        }
    }

    /**
     * To clear value set at element represented by {@link By}
     *
     * @param by
     */
    public void clear(By by) {
        try {

            SyncHelper.waitUntilElementVisible(
                    ThreadLocalSEDriver.getDriver(),
                    by,
                    Duration.ofSeconds(defaultWaitFor)
            );
            WebElement element = ThreadLocalSEDriver
                    .getDriver()
                    .findElement(by);

            // Move to Element
            ActionHelper.moveToElement(element);

            // Highlight Element
            highlightElement(element);

            element.clear();

            // Remove Highlight
            unhighlightElement(element);
        } catch (StaleElementReferenceException sEx) {

            WebElement element = ThreadLocalSEDriver
                    .getDriver()
                    .findElement(by);

            // Move to Element
            ActionHelper.moveToElement(element);

            // Highlight Element
            highlightElement(element);

            element.clear();

            // Unhighlight Element
            unhighlightElement(element);
        } catch (TimeoutException | NoSuchElementException tEx) {
            logger.error(tEx.getMessage(), tEx);
        }
    }

    /**
     * To get text of type {@link WebElement}
     *
     * @param element {@link WebElement} of which to get text
     * @return text inside of a particular element
     */
    public String getText(WebElement element) {

        String value = null;
        try {
            SyncHelper.waitUntilElementVisible(
                    ThreadLocalSEDriver.getDriver(),
                    element,
                    Duration.ofSeconds(defaultWaitFor)
            );

            // Move to Element
            ActionHelper.moveToElement(element);

            // Highlight Element
            highlightElement(element);

            value = element.getText();

            // Remove Highlighting
            unhighlightElement(element);
        } catch (TimeoutException tEx) {
            logger.error(tEx.getMessage(), tEx);
        }
        return value;
    }

    /**
     * To get attribute value of an Element
     *
     * @param element       {@link WebElement} whose attribute value to get
     * @param attributeName Attribute name of {@link WebElement}
     * @return Value of attribute
     */
    public String getAttribute(WebElement element, String attributeName) {
        String value = null;
        try {
            SyncHelper.waitUntilElementVisible(
                    ThreadLocalSEDriver.getDriver(),
                    element,
                    Duration.ofSeconds(defaultWaitFor)
            );

            // Move to Element
            ActionHelper.moveToElement(element);

            // Highlight Element
            highlightElement(element);

            value = element.getAttribute(attributeName);

            // Remove Highlighting
            unhighlightElement(element);
        } catch (TimeoutException tEx) {
            logger.error(tEx.getMessage(), tEx);
        }
        return value;
    }

    /**
     * To get attribute value of an Element
     *
     * @param by            {@link By} whose attribute value to get
     * @param attributeName Attribute name of {@link By}
     * @return Value of attribute
     */
    public String getAttribute(By by, String attributeName) {

        String value = null;
        try {
            SyncHelper.waitUntilElementVisible(
                    ThreadLocalSEDriver.getDriver(),
                    by,
                    Duration.ofSeconds(defaultWaitFor)
            );

            WebElement element = ThreadLocalSEDriver
                    .getDriver()
                    .findElement(by);

            // Move to Element
            ActionHelper.moveToElement(element);

            // Highlight Element
            highlightElement(element);

            value = element.getAttribute(attributeName);

            // Remove Highlighting
            unhighlightElement(element);
        } catch (StaleElementReferenceException sEx) {
            WebElement element = ThreadLocalSEDriver
                    .getDriver()
                    .findElement(by);

            // Move to Element
            ActionHelper.moveToElement(element);

            // Highlight element
            highlightElement(element);

            // Click
            element.click();

            // Remove Highlight
            unhighlightElement(element);
        } catch (TimeoutException | NoSuchElementException tEx) {
            logger.error(tEx.getMessage(), tEx);
        }
        return value;
    }

    /**
     * To get text of type {@link By}
     *
     * @param by {@link By} of which to get text
     * @return text inside of an element
     */
    public String getText(By by) {
        String value = null;
        try {

            SyncHelper.waitUntilElementVisible(
                    ThreadLocalSEDriver.getDriver(),
                    by,
                    Duration.ofSeconds(defaultWaitFor)
            );
            WebElement element = ThreadLocalSEDriver
                    .getDriver()
                    .findElement(by);

            // Move to Element
            ActionHelper.moveToElement(element);

            // Highlight Element
            highlightElement(element);

            value = element.getText();

            // Remove highlighting
            unhighlightElement(element);
        } catch (StaleElementReferenceException sEx) {

            WebElement element = ThreadLocalSEDriver
                    .getDriver()
                    .findElement(by);

            // Move to Element
            ActionHelper.moveToElement(element);

            // Highlight Element
            highlightElement(element);

            value = element.getText();

            // Remove highlighting
            unhighlightElement(element);
        } catch (TimeoutException | NoSuchElementException tEx) {
            logger.error(tEx.getMessage(), tEx);
        }
        return value;
    }

    /**
     * To check if a particular element is present in DOM or Not
     *
     * @param by {@link By} for which to check the presence of
     * @return <code>true</code> if present, <code>false</code> otherwise
     */
    public boolean isElementPresent(By by) {
        boolean isPresent = false;
        boolean isDisplayed = false;
        try {
            isPresent = ThreadLocalSEDriver
                    .getDriver()
                    .findElements(by)
                    .size() > 0;

            if (isPresent) {
                // Check if it's displayed
                isDisplayed = ThreadLocalSEDriver
                        .getDriver()
                        .findElements(by)
                        .stream()
                        .allMatch(WebElement::isDisplayed);

            }
        } catch (Exception ignored) {
        }
        return isPresent && isDisplayed;
    }

    /**
     * To highlight an element with Red Border
     *
     * @param element {@link WebElement} to highlight
     */
    public void highlightElement(WebElement element) {
        try {
            SeleniumObjectManager
                    .getJSExecutor()
                    .executeScript("arguments[0].style.border='3px inset red'", element);
        } catch (Exception ignored) {
        }
    }

    /**
     * To highlight an element with Red Border
     *
     * @param by {@link By} object
     */
    public void highlightElement(By by) {
        try {
            SeleniumObjectManager
                    .getJSExecutor()
                    .executeScript(
                            "arguments[0].style.border='3px inset red'",
                            ThreadLocalSEDriver.getDriver().findElement(by)
                    );
        } catch (Exception ignored) {
        }
    }

    /**
     * To highlight an element with specific border color
     *
     * @param element              {@link WebElement} to highlight
     * @param colorToHighlightWith Highlight with color
     */
    public void highlightElement(WebElement element, String colorToHighlightWith) {
        try {
            SeleniumObjectManager
                    .getJSExecutor()
                    .executeScript("arguments[0].style.border='3px inset " + colorToHighlightWith + "'", element);
        } catch (Exception ignored) {
        }
    }

    /**
     * To unhighlight and element after highlighting it
     *
     * @param element {@link WebElement} to unhighlight
     */
    public void unhighlightElement(WebElement element) {
        try {
            SeleniumObjectManager
                    .getJSExecutor()
                    .executeScript("arguments[0].style.border=''", element);
        } catch (Exception ignored) {
        }
    }

    /**
     * To unhighlight and element after highlighting it
     *
     * @param by {@link By} Object
     */
    public void unhighlightElement(By by) {
        try {
            SeleniumObjectManager
                    .getJSExecutor()
                    .executeScript("arguments[0].style.border=''", ThreadLocalSEDriver.getDriver().findElement(by));
        } catch (Exception ignored) {
        }
    }

    /**
     * To check whether an element is displayed on DOM or not
     *
     * @param by {@link By} of element
     * @return <code>true</code> if displayed, <code>false</code> otherwise
     */
    public boolean isElementDisplayed(By by) {
        boolean isDisplayed = false;
        try {
            // check if element is present
            if (isElementPresent(by)) {
                isDisplayed = ThreadLocalSEDriver
                        .getDriver()
                        .findElement(by)
                        .isDisplayed();
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return isDisplayed;
    }

    /**
     * To check if a WebElement is displayed on DOM or not
     *
     * @param element {@link WebElement} to check
     * @return <code>true</code> if displayed, <code>false</code> otherwise
     */
    public boolean isElementDisplayed(WebElement element) {
        boolean isDisplayed = false;
        try {
            isDisplayed = element.isDisplayed();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return isDisplayed;
    }

    /**
     * To wait until JQuery & JavaScripts are loaded
     *
     * @param duration Timeout in seconds to wait for JQuery & JavaScript to load
     * @return <code>true</code> if loaded in timeout specified, <code>false</code> otherwise
     */
    public boolean waitForJStoLoad(Duration duration) {
        try {
            final JavascriptExecutor jse = SeleniumObjectManager.getJSExecutor();
            WebDriverWait wait = SeleniumObjectManager.getWaitObject(duration);

            // wait for jQuery to load
            ExpectedCondition<Boolean> jQueryLoad = driver -> {
                try {
                    return ((Long) jse.executeScript("return jQuery.active") == 0);
                } catch (Exception e) {
                    return true;
                }
            };

            // wait for Javascript to load
            ExpectedCondition<Boolean> jsLoad = driver -> jse.executeScript("return document.readyState")
                    .toString().equals("complete");

            return wait.until(jQueryLoad) && wait.until(jsLoad);
        } catch (Exception ex) {
            logger.info("JS did not load in " + duration.getSeconds() + " seconds");
        }
        return false;
    }

    /**
     * To delete all the cookies before starting browser
     */
    public void deleteAllCookies() {
        ThreadLocalSEDriver
                .getDriver()
                .manage()
                .deleteAllCookies();
    }

    /**
     * To delete a specific cookie of type {@link Cookie}
     *
     * @param cookie {@link Cookie} to delete
     */
    public void deleteCookie(Cookie cookie) {
        try {
            ThreadLocalSEDriver
                    .getDriver()
                    .manage()
                    .deleteCookie(cookie);
        } catch (Exception ex) {
            logger.error("Error deleting cookie.");
        }

    }

    /**
     * To delete a specific cookie with it's name.
     *
     * @param cookieName Cookie to delete having specified name
     */
    public void deleteCookieNamed(String cookieName) {
        try {
            ThreadLocalSEDriver
                    .getDriver()
                    .manage()
                    .deleteCookieNamed(cookieName);
        } catch (Exception ex) {
            logger.error("Error deleting cookied named : " + cookieName);
        }

    }

    /**
     * To execute a JavaScript
     *
     * @param jsScript JavaScript to execute
     */
    public Object executeScript(String jsScript) {
        return SeleniumObjectManager
                .getJSExecutor()
                .executeScript(jsScript);
    }

    /**
     * To enter text into a specific textbox using JavaScript
     *
     * @param element {@link WebElement} at where to set text
     * @param text    Text to set in an element
     */
    public void sendKeysWithJs(WebElement element, String text) {
        try {
            SeleniumObjectManager
                    .getJSExecutor()
                    .executeScript("arguments[0].setAttribute('value', '" + text + "');", element);

            logger.info("Send [" + text + "] to an element using JavaScript.");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * To set value in an element using javascript executor
     *
     * @param by        {@link LocateBy} Value by which to locate
     * @param locator   Locator value
     * @param textValue Text to set
     */
    public void sendKeysWithJS(LocateBy by, String locator, String textValue) {
        switch (by) {
            case ID:
                SeleniumObjectManager
                        .getJSExecutor()
                        .executeScript("document.getElementById('" + locator + "').value='" + textValue + "';");
                break;
            case NAME:
                SeleniumObjectManager
                        .getJSExecutor()
                        .executeScript("document.getElementsByName('" + locator + "')[0].value='" + textValue + "';");
                break;
            case TAG_NAME:
                SeleniumObjectManager
                        .getJSExecutor()
                        .executeScript("document.getElementsByTagName('" + locator + "')[0].value='" + textValue + "';");
                break;
            case CLASS_NAME:
                SeleniumObjectManager
                        .getJSExecutor()
                        .executeScript("document.getElementsByClassName('" + locator + "')[0].value='" + textValue + "';");
                break;
        }
    }

    /**
     * To get current url of a browser session
     *
     * @return Current Website url
     */
    public String getCurrentUrl() {
        return ThreadLocalSEDriver
                .getDriver()
                .getCurrentUrl();
    }

    /**
     * To get text value of an element using JavaScript
     *
     * @param by      {@link LocateBy} with which to locate element
     * @param locator Locator value
     * @return Text value, null otherwise
     */
    public String getTextByJS(LocateBy by, String locator) {
        String textValue = null;

        switch (by) {
            case ID:
                textValue = SeleniumObjectManager
                        .getJSExecutor()
                        .executeScript("return document.getElementById('" + locator + "').value;")
                        .toString();
                break;
            case NAME:
                textValue = SeleniumObjectManager
                        .getJSExecutor()
                        .executeScript("return document.getElementsByName('" + locator + "')[0].value;")
                        .toString();
                break;
            case TAG_NAME:
                textValue = SeleniumObjectManager
                        .getJSExecutor()
                        .executeScript("return document.getElementsByTagName('" + locator + "')[0].value;")
                        .toString();
                break;
            case CLASS_NAME:
                textValue = SeleniumObjectManager
                        .getJSExecutor()
                        .executeScript("return document.getElementsByClassName('" + locator + "')[0].value;")
                        .toString();
                break;
            case XPATH:
                textValue = SeleniumObjectManager
                        .getJSExecutor()
                        .executeScript(
                                "return arguments[0].value;",
                                findElement(By.xpath(locator))
                        )
                        .toString();
                break;
        }
        return textValue;
    }


    /**
     * To get text By JS using {@link By} Object
     *
     * @param by        {@link By} Object for which to get text
     * @param getTextBy {@link TextBy} which to get text
     * @return Value if found, null otherwise.
     */
    public String getTextByJS(By by, TextBy getTextBy) {
        String textValue;

        // Get Locator String from By Element
        String locator = getLocator(by);

        StringBuilder jsQueryBuilder = new StringBuilder();

        if (by instanceof By.ByClassName) {

            jsQueryBuilder
                    .append("return document.")
                    .append("getElementsByClassName")
                    .append("('")
                    .append(locator)
                    .append("')")
                    .append("[0]");

        } else if (by instanceof By.ById) {

            jsQueryBuilder
                    .append("return document.")
                    .append("getElementById")
                    .append("('")
                    .append(locator)
                    .append("')");

        } else if (by instanceof By.ByName) {

            jsQueryBuilder
                    .append("return document.")
                    .append("getElementsByName")
                    .append("('")
                    .append(locator)
                    .append("')")
                    .append("[0]");

        } else if (by instanceof By.ByTagName) {

            jsQueryBuilder
                    .append("return document.")
                    .append("getElementsByTagName")
                    .append("('")
                    .append(locator)
                    .append("')")
                    .append("[0]");

        } else if (by instanceof By.ByXPath) {

            jsQueryBuilder
                    .append("return arguments[0]");

        } else {
            throw new IllegalArgumentException("Illegal By argument provided.");
        }

        if (getTextBy == TextBy.VALUE) {
            jsQueryBuilder
                    .append(".value;");
        } else if (getTextBy == TextBy.INNER_TEXT) {
            jsQueryBuilder
                    .append(".innerText;");
        } else {
            throw new IllegalArgumentException("Illegal argument provided for TextBy");
        }

        WebDriver driver = ThreadLocalSEDriver.getDriver();

        if (by instanceof By.ByXPath) {
            textValue = SeleniumObjectManager
                    .getJSExecutor()
                    .executeScript(
                            jsQueryBuilder.toString(),
                            driver.findElement(by)
                    )
                    .toString();
        } else {
            textValue = SeleniumObjectManager
                    .getJSExecutor()
                    .executeScript(jsQueryBuilder.toString()).toString();
        }

        return textValue;
    }

    /**
     * To click on an Element using JS
     *
     * @param by      Locator Strategy
     * @param locator Locator Value
     */
    public void clickByJS(LocateBy by, String locator) {

        switch (by) {
            case ID:
                SeleniumObjectManager
                        .getJSExecutor()
                        .executeScript("document.getElementById('" + locator + "').click();");
                break;
            case NAME:
                SeleniumObjectManager
                        .getJSExecutor()
                        .executeScript("document.getElementsByName('" + locator + "')[0].click();");
                break;
            case TAG_NAME:
                SeleniumObjectManager
                        .getJSExecutor()
                        .executeScript("document.getElementsByTagName('" + locator + "')[0].click();");
                break;
            case CLASS_NAME:
                SeleniumObjectManager
                        .getJSExecutor()
                        .executeScript("document.getElementsByClassName('" + locator + "')[0].click();");
                break;
        }
    }

    /**
     * To click on a {@link WebElement} using JavaScript
     *
     * @param element {@link WebElement} on which to click
     */
    public void clickByJS(WebElement element) {
        SeleniumObjectManager
                .getJSExecutor()
                .executeScript("arguments[0].click();", element);
    }

    /**
     * To click on {@link By} element
     *
     * @param by {@link By} element
     */
    public void clickByJS(By by) {
        SeleniumObjectManager
                .getJSExecutor()
                .executeScript(
                        "arguments[0].click();",
                        ThreadLocalSEDriver.getDriver()
                                .findElement(by)
                );
    }

    /**
     * To get a Shadow Root Element Using JavaScript
     *
     * @param element Parent Element under which to find the Shadow Element
     * @return Shadow {@link WebElement}
     */
    public WebElement expandRootElement(WebElement element) {
        WebElement shadowRootElement = (WebElement) SeleniumObjectManager
                .getJSExecutor()
                .executeScript("return arguments[0].shadowRoot", element);
        return shadowRootElement;
    }

    /**
     * To find an element using {@link By} method
     *
     * @param by {@link By} locator
     * @return {@link WebElement}
     */
    public WebElement findElement(By by) {
        return SeleniumObjectManager
                .getWaitObject(Duration.ofSeconds(30))
                .until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    /**
     * To check if Element is present for Nested Elements or not
     *
     * @param parent {@link By} Parent element
     * @param child  {@link By} Child Element
     * @return <code>true</code> if found, <code>false</code> otherwise
     */
    public boolean isElementPresentForNestedElements(By parent, By child) {
        return Objects.requireNonNull(ExpectedConditions
                .visibilityOfNestedElementsLocatedBy(parent, child)
                .apply(ThreadLocalSEDriver.getDriver()))
                .stream()
                .allMatch(WebElement::isDisplayed);
    }

    /**
     * To set browser zoom level to a certain value.
     *
     * @param zoomLevelToSet Zoom level in percentage
     */
    public void setZoomLevel(int zoomLevelToSet) {
        SeleniumObjectManager
                .getJSExecutor()
                .executeScript("document.body.style.zoom='" + zoomLevelToSet + "%'");
    }

    /**
     * To Get Locator String from {@link By} element
     *
     * @param by {@link By} object
     * @return Locator Value as a String
     */
    public String getLocator(By by) {
        return by
                .toString()
                .split(":")[1]
                .trim();
    }

    public String getBackgroundColorAsRGB(WebElement element) {
        return element.getCssValue("background-color");
    }

    public String getBackgroundColorAsHex(WebElement element) {
        return Color.fromString(element.getCssValue("background-color")).asHex();
    }


    public String getElementColorAsRGB(WebElement element) {
        return element.getCssValue("color");
    }

    public String getElementColorAsHex(WebElement element) {
        return Color.fromString(element.getCssValue("color")).asHex();
    }

    public String getElementFontFamilyAsRGB(WebElement element) {
        return element.getCssValue("font-family");
    }

    public String getElementFontFamilyAsHex(WebElement element) {
        return Color.fromString(element.getCssValue("font-family")).asHex();
    }
}

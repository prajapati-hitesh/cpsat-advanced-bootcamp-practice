package com.ata.cpsat.practice.pages;

import com.assertthat.selenium_shutterbug.core.Shutterbug;
import com.ata.cpsat.framework.helper.ActionHelper;
import com.ata.cpsat.framework.helper.ElementHelper;
import com.ata.cpsat.framework.helper.FrameHelper;
import com.ata.cpsat.framework.helper.SyncHelper;
import com.ata.cpsat.framework.manager.SeleniumObjectManager;
import com.ata.cpsat.framework.threads.ThreadLocalSEDriver;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.awt.Point;
import java.awt.*;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class RediffHomePage {

    By newsMenuBy = By.linkText("NEWS");

    @FindBy(id = "bseindex")
    WebElement bseLabelElement;
    @FindBy(id = "nseindex")
    WebElement nseLabelElement;
    @FindBy(id = "query")
    WebElement enterCompanyOrMfElement;
    @FindBy(className = "getqbtn")
    WebElement searchCompanyAndGetQuoteElement;

    public RediffHomePage() {
        PageFactory.initElements(ThreadLocalSEDriver.getDriver(), this);
    }


    public RediffHomePage loadWebsite() {
        ThreadLocalSEDriver.getDriver().get("https://www.rediff.com/");
        return this;
    }

    public byte[] navigateTo(Navigate navigate) throws IOException {
        ElementHelper.getInstance().click(navigate.getMenuBy());

        // capture screenshot
        return Shutterbug.shootPage(ThreadLocalSEDriver.getDriver())
                .highlight(ThreadLocalSEDriver.getDriver().findElement(navigate.getActiveMenuBy()), java.awt.Color.BLACK, 3)
                .getBytes();
    }

    private List<WebElement> getSubMenuElements() {
        return ThreadLocalSEDriver.getDriver()
                .findElement(By.className("subnavbar"))
                .findElements(By.tagName("li"));
    }

    private List<WebElement> getMenuElements() {
        return ThreadLocalSEDriver.getDriver()
                .findElement(By.className("navbar"))
                .findElements(By.tagName("li"));
    }

    public List<String> getAllHrefOfSubMenuForCurrentSelection() {
        List<WebElement> subMenuElements = getSubMenuElements();

        List<String> hrefList = new ArrayList<>();

        subMenuElements.stream().filter(e -> e.findElements(By.tagName("a")).size() > 0)
                .forEach(webElement -> {
                    hrefList.add(webElement.findElement(By.tagName("a")).getAttribute("href").trim());
                });

        return hrefList;
    }

    public WebElement getAndMoveToAdjacentMenuOfCurrentSelection(ElementLocation elementLocation) {
        List<WebElement> menuElements = getMenuElements();

        for (int i = 0; i < menuElements.size(); i++) {
            String currEleClassValue = menuElements.get(i).getAttribute("class");

            if (!StringUtils.isBlank(currEleClassValue)) {
                WebElement adjacentElement = null;
                if (elementLocation == ElementLocation.AFTER) {
                    adjacentElement = menuElements.get(i + 1);
                } else if (elementLocation == ElementLocation.BEFORE) {
                    adjacentElement = menuElements.get(i == 0 ? 0 : i - 1);
                }
                ActionHelper.moveToElement(adjacentElement);
                SyncHelper.hardWait(Duration.ofSeconds(1));
                return adjacentElement;
            }
        }
        return null;
    }

    public String getTooltipForMenu(WebElement element) {
        return ElementHelper.getInstance().getAttribute(element.findElement(By.tagName("a")), "title");
    }

    public void hoverElement(WebElement element) throws AWTException {
        PointerInfo pointerInfo = MouseInfo.getPointerInfo();

        Point mousePoint = pointerInfo.getLocation();
        int mouseX = (int) mousePoint.getX();
        int mouseY = (int) mousePoint.getY();

        System.out.println("Mouse pointer is at location (x,y) : (" + mouseX + ", " + mouseY + ")");

        // Get Robot object
        Robot robot = new Robot();
        robot.mouseMove(element.getLocation().getX(), element.getLocation().getY());
    }

    public byte[] getScreenshotOfMenu() {
        return ThreadLocalSEDriver.getDriver()
                .findElement(By.className("stickynavbar"))
                .getScreenshotAs(OutputType.BYTES);
    }

    public boolean hasRGBValueAsForAllSubMenu(String rgbColor) {
        WebElement subMenuBarElement = ThreadLocalSEDriver
                .getDriver()
                .findElement(By.className("subnavbar"));

        String expectedHex = Color.fromString(rgbColor).asHex();

        return Color.fromString(subMenuBarElement.getCssValue("background-color")).asHex().equalsIgnoreCase(expectedHex);
    }

    public String getColorName(String rgbColor) {
        int red = Color.fromString(rgbColor).getColor().getRed();
        int green = Color.fromString(rgbColor).getColor().getGreen();
        int blue = Color.fromString(rgbColor).getColor().getBlue();

        // Open new window and get name of color
        ThreadLocalSEDriver.getDriver().switchTo().newWindow(WindowType.TAB);
        ThreadLocalSEDriver.getDriver().get("https://www.color-blindness.com/color-name-hue/");

        List<String> windows = new ArrayList<>(ThreadLocalSEDriver.getDriver().getWindowHandles());

        // Switch to Iframe//article[@id='post-438']//iframe
        WebElement frameElement = ThreadLocalSEDriver.getDriver()
                .findElement(By.id("post-438"))
                .findElement(By.tagName("iframe"));

        ThreadLocalSEDriver.getDriver().switchTo().frame(frameElement);

        ElementHelper.getInstance().click(By.id("cp1_RedRadio"));
        setColorCode(ElementHelper.getInstance().findElement(By.id("cp1_Red")), red);

        ElementHelper.getInstance().click(By.id("cp1_GreenRadio"));
        setColorCode(ElementHelper.getInstance().findElement(By.id("cp1_Green")), green);

        ElementHelper.getInstance().click(By.id("cp1_BlueRadio"));
        setColorCode(ElementHelper.getInstance().findElement(By.id("cp1_Blue")), blue);
        // Perform Tab
        SeleniumObjectManager.getActionObject().sendKeys(Keys.TAB);

        SyncHelper.hardWait(Duration.ofSeconds(2));

        String colorName = ElementHelper.getInstance().getText(By.id("cp1_ColorNameText"));

        // Switch to parent frame
        ThreadLocalSEDriver.getDriver().switchTo().parentFrame();

        // Close the tab and switch back to original
        ThreadLocalSEDriver.getDriver().close();

        // Switch to original
        ThreadLocalSEDriver.getDriver().switchTo().window(windows.get(0));

        // Get name of color
        return colorName;
    }

    private void setColorCode(WebElement element, int value) {
        int length = SeleniumObjectManager
                .getJSExecutor()
                .executeScript("return arguments[0].value;", element)
                .toString()
                .length();

        for (int i = 0; i < length; i++) {
            element.sendKeys(Keys.BACK_SPACE);
        }
        element.sendKeys(String.valueOf(value));
    }

    public void navigateTo(String menu, String subMenu) {
        // Get all menu elements
        List<WebElement> menuElements = getMenuElements();

        // Click on requested menu
        for (WebElement e : menuElements) {
            String currMenuText = e.getText();
            if (!StringUtils.isBlank(currMenuText)) {
                if (currMenuText.trim().equalsIgnoreCase(menu.trim())) {
                    // Click
                    ElementHelper.getInstance().click(e);
                    break;
                }
            }
        }

        // Get List Of Submenu
        List<WebElement> subMenuElements = getSubMenuElements();

        // click on requested submenu
        for (WebElement subMenuElement : subMenuElements) {
            String currMenuText = subMenuElement.getText();
            if (!StringUtils.isBlank(currMenuText)) {
                if (currMenuText.trim().equalsIgnoreCase(subMenu.trim())) {
                    // Click
                    ElementHelper.getInstance().click(subMenuElement);
                    break;
                }
            }
        }
    }

    private void switchToTradingFrame() {
        ThreadLocalSEDriver.getDriver()
                .switchTo()
                .frame(ThreadLocalSEDriver.getDriver().findElement(By.id("moneyiframe")));
    }

    public String getNSEIndex() {
        switchToTradingFrame();

        // Get NSE Value
        String nseIndex = ElementHelper
                .getInstance()
                .getText(nseLabelElement);

        // Switch to parent
        FrameHelper.switchToParentFrame(ThreadLocalSEDriver.getDriver());
        return nseIndex;
    }

    public String getBSEIndex() {
        switchToTradingFrame();

        // Get NSE Value
        String bseIndex = ElementHelper
                .getInstance()
                .getText(bseLabelElement);

        // Switch to parent
        FrameHelper.switchToParentFrame(ThreadLocalSEDriver.getDriver());
        return bseIndex;
    }

    public RediffHomePage searchCompany(String companyName) {
        switchToTradingFrame();
        ElementHelper.getInstance().sendKeys(enterCompanyOrMfElement, companyName);
        ElementHelper.getInstance().click(searchCompanyAndGetQuoteElement);
        switchToParentTab();
        return this;
    }

    public RediffHomePage switchToCompanyTab() {
        FrameHelper.switchToWindow(
                ThreadLocalSEDriver.getDriver(),
                FrameHelper.getAllWindows(ThreadLocalSEDriver.getDriver()).get(1)
        );
        return this;
    }

    public RediffHomePage switchToParentTab() {
        FrameHelper.switchToWindow(
                ThreadLocalSEDriver.getDriver(),
                FrameHelper.getAllWindows(ThreadLocalSEDriver.getDriver()).get(0)
        );
        return this;
    }

    public List<String> getAllTopStoriesHREFs() {
        List<WebElement> topStoriesElements = ThreadLocalSEDriver.getDriver()
                .findElement(By.id("topdiv_0"))
                .findElements(By.tagName("a"));

        List<String> allTopStoriesHREF = new ArrayList<>();
        topStoriesElements.forEach(webElement -> {
            allTopStoriesHREF.add(ElementHelper.getInstance().getAttribute(webElement, "href"));
        });
        return allTopStoriesHREF;
    }

    public enum Navigate {
        HOME(By.linkText("HOME"), null),
        NEWS(By.linkText("NEWS"), By.className("news")),
        BUSINESS(By.linkText("BUSINESS"), By.className("business")),
        MOVIES(By.linkText("MOVIES"), By.className("movies")),
        CRICKET(By.linkText("CRICKET"), By.className("cricket")),
        SPORTS(By.linkText("SPORTS"), By.className("sports")),
        GET_AHEAD(By.linkText("GET AHEAD"), By.className("getahead")),
        REALTIME_NEWS(By.linkText("REALTIME NEWS"), By.className("news")),
        COVID_19(By.linkText("COVID-19"), null);

        final By mainMenuBy;
        final By activeMenuBy;

        Navigate(By by, By activeBy) {
            this.mainMenuBy = by;
            this.activeMenuBy = activeBy;
        }

        public By getMenuBy() {
            return this.mainMenuBy;
        }

        public By getActiveMenuBy() {
            return this.activeMenuBy;
        }
    }

    public enum ElementLocation {
        BEFORE,
        AFTER
    }
}

package com.ata.cpsat.framework.helper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class FrameHelper {
    private static final Logger logger = LogManager.getLogger(FrameHelper.class.getName());

    /**
     * To get list of all windows opened with a particular driver session
     *
     * @param driver {@link WebDriver} instance
     * @return List of windows
     */
    public static List<String> getAllWindows(WebDriver driver) {
        return new ArrayList<>(driver.getWindowHandles());
    }

    /**
     * To get Window Id of a window where Driver Instance is currently active
     *
     * @param driver {@link WebDriver} instance
     * @return Window Id as a <code>String</code>
     */
    public static String getCurrentWindow(WebDriver driver) {
        String windowId = null;
        try {
            windowId = driver.getWindowHandle();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return windowId;
    }

    /**
     * To switch to a particular window having a specific Id
     *
     * @param driver   {@link WebDriver} instance
     * @param windowId Id of window to where Switch To
     */
    public static void switchToWindow(WebDriver driver, String windowId) {
        try {
            driver.switchTo().window(windowId);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    /**
     * To switch to frame using index, frame Id or frame of type <code>WebElement</code>
     *
     * @param driver           {@link WebDriver} instance
     * @param frameIdOrElement frame index, id or <code>WebElement</code>
     */
    public static void switchToFrame(WebDriver driver, Object frameIdOrElement) {
        try {
            if (frameIdOrElement instanceof String) {
                driver.switchTo().frame(String.valueOf(frameIdOrElement));
            } else if (frameIdOrElement instanceof Integer) {
                driver.switchTo().frame((Integer) frameIdOrElement);
            } else if (frameIdOrElement instanceof WebElement) {
                driver.switchTo().frame((WebElement) frameIdOrElement);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    /**
     * Switch to Active Element
     *
     * @param driver {@link WebDriver} instance
     */
    public static void switchToActiveContent(WebDriver driver) {
        try {
            driver.switchTo().activeElement();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    /**
     * To switch to Parent Frame from Child Frame
     *
     * @param driver {@link WebDriver} instance
     */
    public static void switchToParentFrame(WebDriver driver) {
        try {
            driver.switchTo().parentFrame();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    /**
     * To switch to Default content
     *
     * @param driver {@link WebDriver} instance
     */
    public static void switchToDefaultContent(WebDriver driver) {
        try {
            driver.switchTo().defaultContent();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    /**
     * To dismiss JavaScript alert
     *
     * @param driver {@link WebDriver} instance
     */
    public static void dismissAlert(WebDriver driver) {
        try {
            driver.switchTo().alert().dismiss();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    /**
     * To accept JavaScript alert
     *
     * @param driver {@link WebDriver} instance
     */
    public static void acceptAlert(WebDriver driver) {
        try {
            driver.switchTo().alert().accept();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    /**
     * To enter text into JavaScript alert
     *
     * @param driver {@link WebDriver} instance
     * @param text   Text to insert into alert
     */
    public static void enterAlertText(WebDriver driver, String text) {
        try {
            Alert alert = driver.switchTo().alert();
            alert.sendKeys(text);
            alert.accept();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    /**
     * To get text of a JavaScript alert
     *
     * @param driver {@link WebDriver} instance
     * @return Text from a JavaScript alert
     */
    public static String getAlertText(WebDriver driver) {
        String alertText = null;
        try {
            alertText = driver.switchTo().alert().getText();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return alertText;
    }
}

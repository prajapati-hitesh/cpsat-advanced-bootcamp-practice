package com.ata.cpsat.framework.utility;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;
import java.util.Objects;

public class ScreenshotUtility {

    private static final Logger logger = LogManager.getLogger(ScreenshotUtility.class.getName());

    /**
     * public <T> String takeScreenshotAsBase64(T driverObj) {
     * return ((TakesScreenshot) driverObj).getScreenshotAs(OutputType.BASE64);
     * }
     * <p>
     * <p>
     * /**
     * To take screenshot as Bytes Array
     *
     * @param driverObj Object of {@link org.openqa.selenium.WebDriver}
     * @return Screenshot in Byte Array
     */
    public static <T> byte[] takeScreenshotAsByteArray(T driverObj) {
        return ((TakesScreenshot) driverObj).getScreenshotAs(OutputType.BYTES);
    }

    /**
     * Takes Screenshot of an element as Byte Array
     *
     * @param driverObj Object of {@link org.openqa.selenium.WebDriver}
     * @param element   MobileElement object
     * @param <T>       Generic object of Driver instance
     * @return ByteArray, null otherwise
     */
    public static <T> byte[] takeElementScreenshotAsByteArray(T driverObj, WebElement element) {
        return Objects.requireNonNull(getScreenshotOfElement(driverObj, element));
    }

    /**
     * Takes Screenshot of an element as Base64 String
     *
     * @param driverObj Object of {@link org.openqa.selenium.WebDriver}
     * @param element   MobileElement object
     * @return Base64 String
     */
    public static <T> String takeScreenshotAsBase64(T driverObj, WebElement element) {
        return convertToBase64Image(Objects.requireNonNull(getScreenshotOfElement(driverObj, element)));
    }

    /**
     * Take screenshot at File
     *
     * @param driverObj Object of {@link org.openqa.selenium.WebDriver}
     * @return Screenshot As File
     */
    public static <T> File takeScreenshotAsFile(T driverObj) {
        return ((TakesScreenshot) driverObj).getScreenshotAs(OutputType.FILE);
    }


    /**
     * Converts the image of type File to Base64 String
     *
     * @param imageAsFileObj Image object of type File
     * @return Base64 String
     */
    private static String convertToBase64Image(File imageAsFileObj) {
        String base64Image = "";

        try (FileInputStream imageInFile = new FileInputStream(imageAsFileObj)) {
            // Reading a Image file from file system
            byte[] imageData = new byte[(int) imageAsFileObj.length()];
            imageInFile.read(imageData);
            base64Image = Base64.getEncoder().encodeToString(imageData);
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException ioe) {
            logger.error("Exception while reading the Image : " + ioe.getMessage(), ioe);
        }
        return base64Image;
    }

    /**
     * To convert Image of type Byte Arrays to Base64 String
     *
     * @param imageAsByteArray Image as Byte Array
     * @return Base64 String
     */
    public static String convertToBase64Image(byte[] imageAsByteArray) {
        return Base64.getEncoder().encodeToString(imageAsByteArray);
    }

    /**
     * Takes the screenshot of element
     *
     * @param driverObj AppiumDriver object
     * @param element   MobileElement Object
     * @return Screenshot object of type File
     * @throws IOException Throws <code>IOException</code>
     */
    private static <T> byte[] getScreenshotOfElement(T driverObj, WebElement element) {

        try {
            // Get Default screen's original configuration
            GraphicsConfiguration graphicsConfiguration = GraphicsEnvironment
                    .getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice()
                    .getDefaultConfiguration();

            AffineTransform transform = graphicsConfiguration.getDefaultTransform();

            // Get Current Screen Resolution Scales
            double scaleX = transform.getScaleX();
            double scaleY = transform.getScaleY();

            logger.info("Graphics Resolution Scale for X Is : " + scaleX);
            logger.info("Graphics Resolution Scale for Y Is : " + scaleY);

            // Take Screenshot ast Byte Array
            final byte[] screenShotAsByteArray = ((TakesScreenshot) driverObj).getScreenshotAs(OutputType.BYTES);

            // Read Screenshot as BufferedImage to perform Cropping
            BufferedImage fullScreenshot = ImageIO.read(new ByteArrayInputStream(screenShotAsByteArray));

            // Get Height and width of original screenshot
            int fullSSHeight = fullScreenshot.getHeight();
            int fullSSWidth = fullScreenshot.getWidth();

            // Get the location of element on the page
            final Point elementLocation = element.getLocation();
            int elementX = elementLocation.getX();
            int elementY = elementLocation.getY();

            // Set Default Screen Resolution Scale
            final double DEFAULT_SCALE = 1.0;

            // Formulate the scale factor for X & Y
            double adjustXWithScale = DEFAULT_SCALE <= scaleX ? (scaleX - DEFAULT_SCALE) : (DEFAULT_SCALE - scaleX);
            double adjustYWithScale = DEFAULT_SCALE <= scaleY ? (scaleY - DEFAULT_SCALE) : (DEFAULT_SCALE - scaleY);

            // Calculate Scaled/New X & Y Co-Ordinates based on scale factor
            int scaledX = (int) (elementX + (elementX * adjustXWithScale));
            int scaledY = (int) (elementY + (elementY * adjustYWithScale));

            // Get new Top Left location from Scaled/New X, Y Co-Ordinates
            final Point topLeftLocation = new Point(scaledX, scaledY);

            // Get Original Dimensions of Element
            int elementWidth = element.getSize().getWidth();
            int elementHeight = element.getSize().getHeight();

            // Calculate Scaled/New dimension for element based on scaling factor
            int scaledWidth = (int) (elementWidth + (elementWidth * adjustXWithScale));
            int scaledHeight = (int) (elementHeight + (elementHeight * adjustYWithScale));

            // Check if bottom right co-ordinates are > Full height and width of Screenshot,
            // if yes then crop by max height & width of full screenshot
            boolean isRightXLocationOutsideImage = (scaledX + scaledWidth) > fullSSWidth;
            boolean isRightYLocationOutsideImage = (scaledY + scaledHeight) > fullSSHeight;

            // Set new scale width to crop image with original image's width,
            // if crop co-ordinates are out of range of Original Image Width
            if (isRightXLocationOutsideImage) {
                scaledWidth = fullSSWidth - scaledX;
            }

            // Set new scale Height to crop image with original image's height,
            // if crop co-ordinates are out of range of Original Image Height
            if (isRightYLocationOutsideImage) {
                scaledHeight = fullSSHeight - scaledY;
            }

            // Decide whether to add water mark of cropped image or not
            boolean addWaterMark = isRightXLocationOutsideImage || isRightYLocationOutsideImage;

            // Create object of BufferedImage to store cropped image
            // Crop the entire page screenshot to get only the element screenshot
            BufferedImage elementScreenshot = fullScreenshot
                    .getSubimage(
                            topLeftLocation.getX(),
                            topLeftLocation.getY(),
                            scaledWidth,
                            scaledHeight
                    );

            BufferedImage finalImage;
            // Add Water Mark to screenshot if either height or width of element is > max height & width of screenshot
            if (addWaterMark) {
                finalImage = setCaptionInImage(elementScreenshot, "This is cropped image.");
            } else {
                finalImage = elementScreenshot;
            }
            return toByteArray(finalImage, "png");
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }
        return null;
    }

    /**
     * To store File Object of screenshot on disk
     *
     * @param screenshotAsFile <code>File</code> <b>File type object of Screenshot</b>
     * @param filePath         <code>String</code> <b>Absolute file path with extension</b>
     * @return <code>boolean</code> <b>True if file created, false otherwise.</b>
     */
    public static boolean saveScreenshotFile(File screenshotAsFile, String filePath) {
        boolean isFileCreated = false;
        try {
            File file = new File(filePath);

            // If directory structure does not exist, create one
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            // Store screenshot
            FileUtils.copyFile(screenshotAsFile, file);

            if (file.exists()) {
                isFileCreated = true;
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return isFileCreated;
    }

    /**
     * To store byte array of screenshot on disk
     *
     * @param imageByteArray {@link Byte} array of image
     * @param filePath       file path at where to store file
     * @return <code>true</code> if file is created and exists, <code>false</code> otherwise
     */
    public static boolean saveScreenshotFile(byte[] imageByteArray, String filePath) {
        boolean isFileCreated = false;
        try {
            File file = new File(filePath);

            // If directory structure does not exist, create one
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            // Store screenshot
            FileUtils.writeByteArrayToFile(file, imageByteArray);

            if (file.exists()) {
                isFileCreated = true;
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return isFileCreated;
    }

    /**
     * To convert Buffered Image To Byte Array
     *
     * @param bufferedImage Buffered Image to convert to Byte Array
     * @param imageFormat   Image Format in which to convert
     * @return Byte Array
     * @throws IOException
     */
    private static byte[] toByteArray(BufferedImage bufferedImage, String imageFormat)
            throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, imageFormat, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * To set caption on a Buffered Image at bottom right area.
     *
     * @param originalImage {@link BufferedImage}
     * @param caption       Caption to set on image
     * @return {@link BufferedImage} having caption text at the bottom
     */
    public static BufferedImage setCaptionInImage(BufferedImage originalImage, String caption) {
        // Initialize Font to be used as a caption
        Font font = new Font("Calibri", Font.BOLD, 20);

        // Initialize object of Graphics from Buffered Image
        Graphics g = originalImage.getGraphics();

        // Initialize object of font metrics from Image Graphic
        FontMetrics fontMetrics = g.getFontMetrics(font);

        int textPaddingInPx = 10;
        // Find out the position for text to be placed at bottom right
        int positionX = (originalImage.getWidth() - fontMetrics.stringWidth(caption)) - textPaddingInPx;
        int positionY = (originalImage.getHeight() - fontMetrics.getHeight()) + fontMetrics.getAscent() - textPaddingInPx;

        // set font and color
        g.setFont(font);
        g.setColor(Color.RED);

        // draw caption into image
        g.drawString(StringUtils.normalizeSpace(caption), positionX, positionY);

        return originalImage;
    }
}

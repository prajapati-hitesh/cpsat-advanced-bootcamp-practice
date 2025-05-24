package com.ata.cpsat.framework.helper;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;

import java.net.URL;

public class LinkHelper {
    private static final Logger logger = LogManager.getLogger(LinkHelper.class.getName());
    public static boolean isBrokenLink(String url) {
        boolean isValid = true;
        int statusCode = 0;

        if (!StringUtils.isBlank(url)) {
            try {
                statusCode = RestAssured.given().accept(ContentType.JSON)
                        .when()
                        .request(Method.HEAD, new URL(url))
                        .then()
                        .extract()
                        .statusCode();
            } catch (Exception eX) {
                logger.error(eX.getMessage());
            }
            // logger.info(statusCode + " | " + url);
            isValid = statusCode >= 400;
        }
        return isValid;
    }

    public static boolean isBrokenImage(WebElement imageElement) {
        boolean isBroken = false;
        int statusCode = 0;
        boolean hasHeight;
        boolean hasWidth;

        if (imageElement != null) {
            String imageSourceUrl = imageElement.getAttribute("src");

            if (!StringUtils.isBlank(imageSourceUrl)) {
                statusCode = RestAssured.given().accept(ContentType.JSON)
                        .when()
                        .get(imageSourceUrl)
                        .then()
                        .extract()
                        .statusCode();
            }
            hasWidth = Integer.parseInt(imageElement.getAttribute("naturalWidth")) > 0;
            hasHeight = Integer.parseInt(imageElement.getAttribute("naturalHeight")) > 0;

            if(statusCode == 200) {
                isBroken = !hasHeight && !hasWidth;
            } else {
                isBroken = true;
            }
        }
        return isBroken;
    }
}

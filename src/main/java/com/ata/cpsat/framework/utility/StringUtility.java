package com.ata.cpsat.framework.utility;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

public class StringUtility {

    /**
     * To check if the string is Number or not
     *
     * @param strNum String
     * @return True if number, false otherwise
     */
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /***
     * To Convert CamelCase String to Sentence Case. i.e. IAmCamelCase --> I Am Camel Case
     * @param stringInCamelCase String
     * @return Sentence Case String
     */
    public static String splitCamelCase(String stringInCamelCase) {
        return stringInCamelCase.replaceAll(
                String.format("%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[^A-Za-z])"
                ),
                " "
        );
    }

    /**
     * To check if the String is Null Or WhiteSpace
     *
     * @param value
     * @return
     */
    public static boolean isStringNullOrWhiteSpace(String value) {
        if (value == null) {
            return true;
        }

        for (int i = 0; i < value.length(); i++) {
            if (!Character.isWhitespace(value.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * To check whether a string is not null and has value
     *
     * @param value <code>String</code>
     * @return true if String has value, false otherwise
     */
    public static boolean isNullAndHasValue(String value) {
        boolean hasValue = false;

        if (value != null) {
            if (!value.isEmpty()) {
                hasValue = true;
            }
        }
        return hasValue;
    }

    /***
     * Converts Key to Pascal Case and Remove all Special Characters
     * @param text Text to be formatted To Pascal Case
     * @return Formatted String in PascalCase
     */
    public static String getFormattedKey(String text) {
        if (!StringUtils.isBlank(text)) {
            String pascalCase = WordUtils.capitalizeFully(text).trim();
            pascalCase = pascalCase.replaceAll("[\\s()]+", "");
            pascalCase = pascalCase.replaceAll("[^A-Za-z0-9]", "");
            return pascalCase.trim();
        } else {
            throw new NullPointerException("Parameter value cannot be null.");
        }
    }

    /**
     * Converts String to Upper Case
     *
     * @param stringToConvert <code>String</code> to be converted Upper Case
     * @return Upper Case <code>String</code>
     */
    public static String toUpperCase(String stringToConvert) {
        return stringToConvert.trim().toUpperCase();
    }

    /***
     * To check if the String is Boolean or not
     * @param value String
     * @return True if boolean, false otherwise
     */
    public static boolean isBoolean(String value) {
        return Arrays.stream(new String[]{"true", "false", "1", "0"})
                .anyMatch(b -> b.equalsIgnoreCase(value.trim()));
    }

    /***
     * Converts String to Pascal Case and Remove all Special Characters. (i.e. I Am Sentence Case --> IAmSentenceCase)
     * @param text String
     * @return String as pascal case
     */
    public static String toPascalCase(String text) {
        String pascalCase = WordUtils.capitalizeFully(text).trim();
        pascalCase = pascalCase.replaceAll("[\\s()]+", "");
        pascalCase = pascalCase.replaceAll("[^A-Za-z0-9]", "");
        return pascalCase.trim();
    }

    public static boolean isNeitherBlankNullNorWhitespace(Object... t) {
        boolean isNotNullNotEmpty = t != null && t.length > 0;
        boolean isNotWhiteSpace = false;

        if (isNotNullNotEmpty) {
            isNotWhiteSpace = Arrays.stream(t)
                    .toList()
                    .stream()
                    .noneMatch(e -> e == null ? StringUtils.isBlank(null) : StringUtils.isBlank(String.valueOf(e)));
        }
        return isNotNullNotEmpty && isNotWhiteSpace;
    }
}

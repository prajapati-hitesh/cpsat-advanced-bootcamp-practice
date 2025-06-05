package com.ata.cpsat.framework.utility;

import com.google.common.base.CaseFormat;

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

    public static String toKebabCase(String str) {
        if (str == null || str.isEmpty()) return "";

        // Remove special characters except space
        String cleaned = str.replaceAll("[^a-zA-Z0-9 ]+", "");

        // Replace spaces with underscores to make it compatible with Guava
        cleaned = cleaned.trim().replaceAll("\\s+", "_");

        // Assume input is now in lower_underscore
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, cleaned).toLowerCase();
    }
}

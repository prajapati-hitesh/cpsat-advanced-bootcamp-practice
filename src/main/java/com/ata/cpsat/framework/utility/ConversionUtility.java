package com.ata.cpsat.framework.utility;

public class ConversionUtility {

    /**
     * To convert double value to Two Decimal. (i.e. 2.8876 to 2.89)
     *
     * @param doubleValueAsString double
     * @return double rounded to two decimal
     */
    public static double doubleToTwoDecimal(double doubleValueAsString) {
        return (double) Math.round(doubleValueAsString * 100) / 100;
    }

    /**
     * To convert String value to Two Decimal. (i.e. 2.8876 to 2.89)
     *
     * @param doubleValueAsString String
     * @return double rounded to Two decimal
     */
    public static double stringToTwoDecimal(String doubleValueAsString) {
        return (double) Math.round(Double.parseDouble(doubleValueAsString) * 100) / 100;
    }

    /**
     * To check if the Number is Integer or Double
     *
     * @param number Double
     * @return true if Integer, false otherwise
     */
    public static boolean isInteger(double number) {
        return Math.ceil(number) == Math.floor(number);
    }
}

package com.ata.cpsat.framework.xls2json;


import java.util.Arrays;
import java.util.List;

/**
 * Created by Feliciano.Long on 6/1/2016.
 *
 * @author Feliciano.Long
 */
public enum ParsedCellType {
    /**
     * Basic type includes: String, Integer, Double and Boolean. See isBasicType() for detail
     */
    BASIC("Basic"),
    /**
     * Time type format: hh:mm:ss
     */
    TIME("Time"),
    /**
     * String array
     */
    ARRAY_STRING("Array<String>"),
    /**
     * Boolean array
     */
    ARRAY_BOOLEAN("Array<Boolean>"),
    /**
     * Double array
     */
    ARRAY_DOUBLE("Array<Double>"),
    /**
     * Reference to another JSONObject from another row
     */
    REFERENCE("Reference"),
    /**
     * JSONObject
     */
    OBJECT("Object");

    private static List<String> basicTypes = Arrays.asList(new String[]{"String", "Integer", "Float", "Double", "Boolean", "Basic"});
    private String stringValue;

    ParsedCellType(String stringValue) {
        this.stringValue = stringValue;
    }

    /***
     * Construct the enumeration value from a string, not case sensitive so better than valueOf()
     * @param stringValue reference the string value of the enum
     * @return the enumeration value of the string
     * @throws IllegalArgumentException if there is no match
     */
    protected static ParsedCellType fromString(String stringValue) {
        if (stringValue != null) {
            for (ParsedCellType type : ParsedCellType.values()) {
                if (stringValue.equalsIgnoreCase(type.toString())) {
                    return type;
                }
            }
            if (isBasicType(stringValue))
                return BASIC;
        }

        throw new IllegalArgumentException("No constant with name " + stringValue + " found");
    }

    /**
     * Judge the type name if it's a supported basic type
     *
     * @param typeName Type name
     * @return True if it's supported
     */
    protected static boolean isBasicType(String typeName) {
        for (String basicType : basicTypes) {
            if (typeName.equalsIgnoreCase(basicType))
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return stringValue;
    }

}

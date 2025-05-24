package com.ata.cpsat.framework.json;

import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSONParser {

    public JSONParser() {
        // To Do
    }

    /**
     * Read JSON from A File Path As JSONObject
     *
     * @param jsonFilePath String
     * @return JSONObject
     */
    public JSONObject parse(String jsonFilePath) {
        try {
            // Read Json File
            FileReader jsonFileReader;
            if (hasFileExtension(jsonFilePath)) {
                jsonFileReader = new FileReader(jsonFilePath);
            } else {
                jsonFileReader = new FileReader(jsonFilePath + ".json");
            }
            JSONTokener jsonTokener = new JSONTokener(jsonFileReader);

            return new JSONObject(jsonTokener);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Read JSON From a File As JSONObject
     *
     * @param jsonFileReader FileReader
     * @return JSONObject
     */
    public JSONObject parse(FileReader jsonFileReader) {
        // Create JSON Tokener to Read JSON
        JSONTokener jsonTokener = new JSONTokener(jsonFileReader);

        return new JSONObject(jsonTokener);

    }

    /**
     * Read JSON from A File Path as JSON String
     *
     * @param jsonFilePath String
     * @return JSONObject
     */
    public String parseAsString(String jsonFilePath) {
        try {
            // Read Json File
            FileReader jsonFileReader;
            if (hasFileExtension(jsonFilePath)) {
                jsonFileReader = new FileReader(jsonFilePath);
            } else {
                jsonFileReader = new FileReader(jsonFilePath + ".json");
            }

            JSONTokener jsonTokener = new JSONTokener(jsonFileReader);

            return (new JSONObject(jsonTokener)).toString();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Read JSON From a File As String
     *
     * @param jsonFileReader FileReader
     * @return String
     */
    public String parseAsString(FileReader jsonFileReader) {
        // Create JSON Tokener to Read JSON
        JSONTokener jsonTokener = new JSONTokener(jsonFileReader);

        return (new JSONObject(jsonTokener)).toString();

    }

    /**
     * To check whether the file path passed to the function has extension or not
     *
     * @param jsonFilePath Absolute file path of json file
     * @return true if has extension, false otherwise
     */
    private boolean hasFileExtension(String jsonFilePath) {
        Pattern xlsRegexPattern = Pattern.compile("[\\w\\s\\d`~!@$%^&*()-_=+{}|;:'\"?.>,<?]*.json$", Pattern.CASE_INSENSITIVE);
        Matcher xlsMatcher = xlsRegexPattern.matcher(jsonFilePath);

        // If testPackJsonFilePath contains .json then use full path else append .json
        if (xlsMatcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

}

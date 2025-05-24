package com.ata.cpsat.framework.xls2json;

import com.ata.cpsat.framework.json.JSONArray;
import com.ata.cpsat.framework.json.JSONObject;
import com.ata.cpsat.framework.utility.DateUtility;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.poi.ss.usermodel.CellType.*;

public class ExcelParser {

    public static String SIGN_HIDDEN_CELL_PREFIX = "$";

    public static String SIGN_ITEM_SPLITTER = ",";

    public static String SIGN_KEYVALUE_SPLITTER = ":";

    public static String SIGN_TABLE_REFERENCE_SPLITTER = "@";

    public static String SIGN_SHEETNAME_COLUMNNAME_SPLITTER = "#";

    private static Logger logger = LogManager.getLogger(ExcelParser.class.getName());


    /**
     * Parse the whole sheet of a workbook
     *
     * @param workbook
     * @param configName
     * @return
     */
    public static JSONArray parseSheet(Workbook workbook, String configName) {
        // Iterate through the rows.
        JSONArray rows = new JSONArray();
        try {


            // Get the Sheet by name.
            ParsedSheet parsedSheet = new ParsedSheet(workbook, configName);
            parsedSheet.parseSheet();

            Sheet sheet = parsedSheet.getSheet();

            // Parse each row.
            for (Iterator<Row> rowsIT = sheet.rowIterator(); rowsIT.hasNext(); ) {
                Row row = rowsIT.next();

                if (row.getRowNum() <= parsedSheet.nameRowIndex)
                    continue;

                // Iterate through the cells.
                JSONObject jsonRow = parseRow(row, parsedSheet);

                rows.put(jsonRow);
            }
        } catch (Exception ex) {
            logger.error(ex);
        }


        return rows;
    }

    /**
     * Find a row using the key and value in another sheet
     *
     * @param sheet The target sheet
     * @param key   One column's name of the row
     * @param value The column's value
     * @return Found row, or null if not found
     */
    public static Row findRowByColumn(ParsedSheet sheet, String key, String value) {
        try {
            int index = sheet.indexOfKey(key);

            if (index == -1)
                throw new IllegalArgumentException("Couldn't find key " + key + " in the provided sheet.");
            logger.error("Couldn't find key " + key + " in the provided sheet named " + sheet.getSheet().getSheetName());

            for (Iterator<Row> rowsIT = sheet.getSheet().rowIterator(); rowsIT.hasNext(); ) {
                Row row = rowsIT.next();

                Cell cell = row.getCell(index);

                switch (sheet.getType(index)) {
                    case BASIC:
                        if (cell == null)
                            continue;

                        if (cell.getCellType() == BLANK)
                            continue;

                        String cellValue = getCellStringValue(cell);

                        if (cellValue.equals(value))
                            return row;

                        break;

                    default:
                        logger.error("Reference search doesn't support the type " + sheet.getType(index) + " of key " + key + ".");
                        throw new IllegalArgumentException("Reference search doesn't support the type " + sheet.getType(index) + " of key " + key + ".");
                }
            }
        } catch (Exception ex) {
            logger.error(ex);
        }
        return null;
    }

    /**
     * Parse a row of the sheet
     *
     * @param row         The target row to parse
     * @param parsedSheet Parsed sheet to provide name and type information
     * @return A parsed JSONObject
     */
    public static JSONObject parseRow(Row row, ParsedSheet parsedSheet) {
        JSONObject jsonRow = new JSONObject();

        try {
            //Parse each cell
            for (int index = 0; index < parsedSheet.width; index++) {
                //System.out.println("Column: " + index);

                Cell cell = row.getCell(index);

                String key = parsedSheet.getKey(index);

                // Skip cells with empty key
                if (key.isEmpty())
                    continue;

                // Skip hidden cells
                if (key.startsWith(SIGN_HIDDEN_CELL_PREFIX))
                    continue;

                ParsedCellType type = parsedSheet.getType(index);

                // Null cell handler
                try {
                    switch (type) {
                        case BASIC:
                            // Handle "Null" string
                            if (cell != null && cell.getCellType() == STRING) {
                                if (cell.getStringCellValue().equalsIgnoreCase("null")) {
                                    jsonRow.put(key, JSONObject.NULL);
                                    continue;
                                }
                            }
                        case TIME:
                            if (cell != null && cell.getCellType() == STRING) {
                                if (cell.getStringCellValue().equalsIgnoreCase("null")) {
                                    jsonRow.put(key, JSONObject.NULL);
                                    continue;
                                }
                            }
                        case OBJECT:
                            if (cell == null || cell.getCellType() == BLANK) {
                                jsonRow.put(key, JSONObject.NULL);
                                continue;
                            }
                            break;
                        case REFERENCE:
                            if (cell == null || cell.getCellType() == BLANK) {
                                jsonRow.put(key.substring(0, key.indexOf("@")), JSONObject.NULL);
                                continue;
                            }
                            break;

                        case ARRAY_STRING:
                        case ARRAY_BOOLEAN:
                        case ARRAY_DOUBLE:
                            if (cell == null || cell.getCellType() == BLANK) {
                                jsonRow.put(key, new ArrayList());
                                continue;
                            }
                            break;

                        default:
                            logger.error("Unhandled empty cell of " + type + " type.");
                            throw new IllegalArgumentException("Unhandled empty cell of " + type + " type.");
                    }

                    String stringCellValue = "";

                    switch (type) { // Add single value support for the row
                        case ARRAY_STRING:
                        case ARRAY_BOOLEAN:
                        case ARRAY_DOUBLE: {
                            switch (cell.getCellType()) {
                                case BOOLEAN:
                                    stringCellValue = String.valueOf(cell.getBooleanCellValue());
                                    break;
                                case NUMERIC:
                                    stringCellValue = String.valueOf(convertToTwoDecimal(cell.getNumericCellValue()));
                                    break;
                                case FORMULA:
                                    XSSFCell xCell = (XSSFCell) cell;
                                    if (isNumeric(xCell.getRawValue())) {
                                        stringCellValue = String.valueOf(convertToTwoDecimal(xCell.getRawValue()));
                                    } else {
                                        stringCellValue = String.valueOf(((XSSFCell) cell).getRawValue());
                                    }
                                    break;
                                default:
                                    if (isNumeric(cell.getStringCellValue())) {
                                        stringCellValue = String.valueOf(convertToTwoDecimal(cell.getStringCellValue()));

                                    } else {
                                        stringCellValue = cell.getStringCellValue();
                                    }

                            }
                        }

                        default:
                    }

                    switch (type) {
                        case BASIC:
                            switch (cell.getCellType()) {
                                case NUMERIC:
                                    if (DateUtil.isCellDateFormatted(cell)) {
                                        jsonRow.put(key, DateUtility.getDateFormatted(cell.getDateCellValue(), "dd/MM/yyyy"));
                                    } else {
                                        if (isInteger(cell.getNumericCellValue())) {
                                            jsonRow.put(key, cell.getNumericCellValue());
                                        } else {
                                            jsonRow.put(key, convertToTwoDecimal(cell.getNumericCellValue()));
                                        }
                                    }
                                    break;
                                case BOOLEAN:
                                    jsonRow.put(key, cell.getBooleanCellValue());
                                    break;
                                case FORMULA:
                                    XSSFCell xCell = (XSSFCell) cell;
                                    if (xCell.getCachedFormulaResultType() == BOOLEAN) {
                                        jsonRow.put(key, false);
                                    } else if (isNumeric(xCell.getRawValue())) {
                                        jsonRow.put(key, convertToTwoDecimal(xCell.getRawValue()));
                                    } else {
                                        jsonRow.put(key, xCell.getRawValue());
                                    }
                                    break;
                                default:
                                    if (StringUtils.isBlank(cell.getStringCellValue())) {
                                        jsonRow.put(key, JSONObject.NULL);
                                    } else if (isNumeric(cell.getStringCellValue())) {
                                        if (NumberUtils.isDigits(cell.getStringCellValue())) {
                                            jsonRow.put(key, NumberUtils.toInt(cell.getStringCellValue()));
                                        } else {
                                            String cellValue = cell.getStringCellValue();

                                            // Get number of decimal
                                            int decimalPoints = cellValue
                                                    .split("\\.")[1]
                                                    .length();
                                            jsonRow.put(key, roundToNDecimal(cellValue, decimalPoints));
                                        }
                                    } else {
                                        jsonRow.put(key, cell.getStringCellValue().trim());
                                    }
                                    break;
                            }
                            ;
                            break;

                        case TIME:
                            if (cell.getCellType() == NUMERIC) {
                                Date time = cell.getDateCellValue();
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(time);
                                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                                String timeStr = sdf.format(calendar.getTime());
                                jsonRow.put(key, timeStr);
                            } else if (cell.getCellType() == STRING) {
                                jsonRow.put(key, cell.getStringCellValue());
                            } else {
                                logger.error("Unhandled cell of " + cell.getCellType() + " type at "
                                        + "row " + row.getRowNum()
                                        + "column " + index);
                                throw new IllegalArgumentException("Unhandled cell of " + cell.getCellType() + " type at "
                                        + "row " + row.getRowNum()
                                        + "column " + index);
                            }
                            break;

                        case ARRAY_STRING:

                        case ARRAY_DOUBLE:

                        case ARRAY_BOOLEAN:
                            ArrayList stringArray = ExcelParser.<ArrayList<String>>parseCellData(type, stringCellValue);
                            jsonRow.put(key, new JSONArray(stringArray));
                            break;

                        case OBJECT:
                            JSONObject jsonObject = ExcelParser.parseCellData(type, cell.getStringCellValue());
                            jsonRow.put(key, jsonObject);
                            break;

                        case REFERENCE:
                            // Split key to get real key, target sheet name and target column name
                            // Key example: monster@monsterSheet#monsterId
                            String[] keyAndTarget = key.split(SIGN_TABLE_REFERENCE_SPLITTER);
                            key = keyAndTarget[0];

                            // Split sheet name and column name
                            String[] realTarget = keyAndTarget[1].split(SIGN_SHEETNAME_COLUMNNAME_SPLITTER);
                            String targetSheetName = realTarget[0];
                            String targetKey = realTarget[1];
                            String targetValue = getCellStringValue(cell);

                            Sheet targetSheet = parsedSheet.getSheet(targetSheetName);
                            ParsedSheet parsedTargetSheet = new ParsedSheet(targetSheet.getWorkbook(), targetSheetName);
                            parsedTargetSheet.parseSheet();

                            Row targetRow = findRowByColumn(parsedTargetSheet, targetKey, targetValue);
                            jsonObject = parseRow(targetRow, parsedTargetSheet);

                            jsonRow.put(key, jsonObject);
                            break;

                        default:
                            logger.error("Unsupported type " + type + " found.");
                            throw new IllegalArgumentException("Unsupported type " + type + " found.");
                    }

                } catch (Exception ex) {
                    logger.error("Error getting value of cell at ["
                            + cell.getAddress().formatAsString()
                            + "] whose Cell Type is ["
                            + cell.getCellType()
                            + "] for Sheet ["
                            + cell.getSheet().getSheetName()
                            + "]");
                }
            }

        } catch (Exception ex) {
            logger.error(ex);

        }
        return jsonRow;
    }

    private static String getCellStringValue(Cell cell) {

        try {
            switch (cell.getCellType()) {
                case BLANK:
                    break;

                case NUMERIC:
                    return convertToTwoDecimal(cell.getNumericCellValue()) + "";

                case BOOLEAN:
                    return cell.getBooleanCellValue() + "";

                case FORMULA:
                    return ((XSSFCell) cell).getRawValue();

                default:
                    if (isNumeric(cell.getStringCellValue())) {
                        return String.valueOf(convertToTwoDecimal(cell.getStringCellValue()));
                    } else {
                        return cell.getStringCellValue();
                    }
            }
        } catch (Exception ex) {
            logger.error("Error getting value of cell at ["
                    + cell.getAddress().formatAsString()
                    + "] whose Cell Type is ["
                    + cell.getCellType()
                    + "] for Sheet ["
                    + cell.getSheet().getSheetName()
                    + "]");
        }
        return null;
    }

    /**
     * Parse a cell of the row
     *
     * @param type      The data type
     * @param cellValue The cell string to be parsed
     * @param <W>       The return data type
     * @return Parsed data
     * @throws NumberFormatException Numeric data parse failed
     */
    public static <W> W parseCellData(ParsedCellType type, String cellValue) throws NumberFormatException {
        Object object = null;

        String[] items = cellValue.split(SIGN_ITEM_SPLITTER);

        switch (type) {
            case ARRAY_STRING:
                ArrayList<String> arrayString = new ArrayList<>();
                for (String item : items) {
                    item = item.trim();
                    arrayString.add(item);
                }
                object = arrayString;
                break;

            case ARRAY_BOOLEAN:
                ArrayList<Boolean> arrayBoolean = new ArrayList<>();
                for (String item : items) {
                    item = item.trim();
                    arrayBoolean.add(Boolean.parseBoolean(item));
                }
                object = arrayBoolean;
                break;

            case ARRAY_DOUBLE:
                ArrayList<Double> arrayDouble = new ArrayList<>();
                for (String item : items) {
                    item = item.trim();
                    arrayDouble.add(Double.parseDouble(item));
                }
                object = arrayDouble;
                break;

            case OBJECT:
                JSONObject obj = new JSONObject();

                for (String item : items) {
                    String temp = item.trim();

                    String[] keyValue = item.split(SIGN_KEYVALUE_SPLITTER);
                    String key = keyValue[0], value = keyValue[1];
                    key = key.trim();
                    value = value.trim();

                    // Handle the null child
                    if (value.equalsIgnoreCase("null")) {
                        obj.put(key, JSONObject.NULL);
                        continue;
                    }

                    if (value.startsWith("\"")) {
                        obj.put(key, value.substring(1, value.length() - 1));
                        continue;
                    }

                    try {
                        obj.put(key, Double.parseDouble(value));
                    } catch (NumberFormatException e) {
                        if (Boolean.parseBoolean(value)) {
                            obj.put(key, true);
                        } else if (value.equalsIgnoreCase("false")) {
                            obj.put(key, false);
                        } else {
                            obj.put(key, value);
                        }
                    }
                }

                object = obj;
                break;
        }

        return (W) object;
    }

    /**
     * Read a workbook from the Excel file
     *
     * @param targetName File name
     * @return The workbook
     * @throws IOException
     * @throws InvalidFormatException
     */
    public static Workbook getWorkbook(String targetName) throws IOException, InvalidFormatException {

        Pattern xlsRegexPattern = Pattern.compile("[\\w\\s\\d`~!@$%^&*()-_=+{}|;:'\"?.>,<?]*.xls$", Pattern.CASE_INSENSITIVE);
        Pattern xlsxRegexPatter = Pattern.compile("[\\w\\s\\d`~!@$%^&*()-_=+{}|;:'\"?.>,<?]*.xlsx$", Pattern.CASE_INSENSITIVE);

        Matcher xlsMatcher = xlsRegexPattern.matcher(targetName);
        Matcher xlsxMatcher = xlsxRegexPatter.matcher(targetName);
        Workbook workbook = null;

        File excelFile;

        try {
            if (xlsMatcher.matches() || xlsxMatcher.matches()) {
                excelFile = new File(targetName);
            } else {
                excelFile = new File(targetName + ".xlsx");
            }

            FileInputStream inp = new FileInputStream(excelFile);
            workbook = WorkbookFactory.create(inp);
        } catch (Exception ex) {
            logger.error("Error getting Workbook for file <" + targetName + "> ");
            logger.error(ex);
        }

        return workbook;
    }

    /**
     * Construct a json object using the sheet list from the workbook
     *
     * @param workbook
     * @param sheetList Sheet names in the workbook
     * @return Constructed json object
     */
    public static JSONObject constructJsonObject(Workbook workbook, String[] sheetList) {
        // Start constructing JSON.
        JSONObject json = new JSONObject();

        try {
            // Create JSON
            for (String sheetName : sheetList) {
                JSONArray rows = ExcelParser.parseSheet(workbook, sheetName);
                json.put(sheetName, rows);
                logger.info("Sheet [" + sheetName + "] Parsed To JSONObject having count as : " + rows.length());
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return json;
    }

    /**
     * Construct a json array using the sheet list from the workbook
     *
     * @param workbook
     * @param sheetList
     * @return
     */
    public static JSONArray constructJsonArray(Workbook workbook, String[] sheetList) {
        JSONArray json = new JSONArray();

        try {
            // Create JSON
            for (String sheetName : sheetList) {
                JSONArray rows = ExcelParser.parseSheet(workbook, sheetName);
                for (int i = 0; i < rows.length(); i++) {
                    json.put(rows.get(i));
                }
            }
        } catch (Exception ex) {
            logger.error(ex);
        }

        return json;
    }

    /**
     * Save a string as a json file
     *
     * @param targetName
     * @param jsonText
     * @throws IOException
     */
    public static void saveJSONStringToFile(String targetName, String jsonText) throws IOException {
        Pattern xlsRegexPattern = Pattern.compile("[\\w\\s\\d`~!@$%^&*()-_=+{}|;:'\"?.>,<?]*.json$", Pattern.CASE_INSENSITIVE);
        Matcher xlsMatcher = xlsRegexPattern.matcher(targetName);
        Path path;
        try {
            if (xlsMatcher.matches()) {
                path = Paths.get(targetName);
            } else {
                path = Paths.get(targetName + ".json");
            }

            BufferedWriter writer = Files.newBufferedWriter(path);
            writer.write(jsonText);
            writer.close();
            logger.info("Json file " + path.getFileName() + " written at Path : " + path.toString());
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }

    /**
     * Parse the excel file and save as json
     *
     * @param targetName    Excel file name without suffix
     * @param showSheetName Whether show sheet name in result or not
     */
    public static String parseExcelFile(String targetName, boolean showSheetName) {
        try {
            /*
             * Zip bomb detected! The file would exceed the max.
             * ratio of compressed file size to the size of the expanded data.
             * This may indicate that the file is used to inflate memory usage and thus could pose a security risk.
             * You can adjust this limit via ZipSecureFile.setMinInflateRatio() if you need to work with files which exceed this limit.
             * Counter: 820224, cis.counter: 8192, ratio: 0.009987515605493134Limits: MIN_INFLATE_RATIO: 0.01
             *
             * Following line added to avoid ZipBomb Exception
             *
             * https://stackoverflow.com/questions/44897500/using-apache-poi-zip-bomb-detected
             */
            ZipSecureFile.setMinInflateRatio(0);

            Workbook workbook = getWorkbook(targetName);
            int totalSheets = workbook.getNumberOfSheets();
            String[] sheetList = new String[totalSheets];

            for (int i = 0; i < totalSheets; i++) {
                sheetList[i] = workbook.getSheetName(i);
            }
            String jsonText;

            if (showSheetName) {
                // Get the JSON text.
                JSONObject json = constructJsonObject(workbook, sheetList);
                jsonText = json.toString();
            } else {
                // Get the JSON text.
                JSONArray json = constructJsonArray(workbook, sheetList);
                jsonText = json.toString();
            }

            return jsonText;

            // saveStringToFile(targetName, jsonText);
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * To check if the string is Number or not
     *
     * @param strNum String
     * @return True if number, false otherwise
     */
    private static boolean isNumeric(String strNum) {
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

    private static double convertToTwoDecimal(double doubleValueAsString) {
        return (double) Math.round(doubleValueAsString * 100) / 100;
    }

    private static double roundToNDecimal(double valueToRound, int numberOfDecimalPlaces) {
        return Precision.round(valueToRound, numberOfDecimalPlaces);
    }

    private static double roundToNDecimal(String valueToRound, int numberOfDecimalPlaces) {
        return Precision.round(Double.parseDouble(valueToRound), numberOfDecimalPlaces);
    }

    private static double convertToTwoDecimal(String doubleValueAsString) {
        return (double) Math.round(Double.parseDouble(doubleValueAsString) * 100) / 100;
    }

    private static double convertToNDecimal(String doubleValueAsString, int numberOfDecimalPoints) {
        return (double) Math.round(Double.parseDouble(doubleValueAsString) * 100) / 100;
    }

    private static boolean isInteger(double number) {
        return Math.ceil(number) == Math.floor(number);
    }

}

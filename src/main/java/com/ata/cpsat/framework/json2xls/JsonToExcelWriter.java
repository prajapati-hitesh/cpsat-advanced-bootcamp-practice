package com.ata.cpsat.framework.json2xls;

import com.ata.cpsat.framework.json.JSONArray;
import com.ata.cpsat.framework.json.JSONObject;
import com.ata.cpsat.framework.utility.DateUtility;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.text.WordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonToExcelWriter {

    private static final Logger LOGGER = LogManager.getLogger(JsonToExcelWriter.class.getName());

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

    /***
     * To Convert CamelCase String to Sentence Case. i.e. IAmCamelCase --> I Am Camel Case
     * @param stringInCamelCase String
     * @return Sentence Case String
     */
    private static String splitCamelCase(String stringInCamelCase) {
        return stringInCamelCase.replaceAll(
                String.format("%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[^A-Za-z])"
                ),
                " "
        );
    }

    /***
     * To write JSON to Excel file.
     * @param jsonString String
     * @param excelFilePath String
     * @param fileExtension FileExtension
     */
    public void write(String jsonString, String excelFilePath, FileExtension fileExtension) {

        // Convert Json String to JSON Object
        JSONObject jsonObject = new JSONObject(jsonString);
        SXSSFWorkbook xssfWorkbook = new SXSSFWorkbook(5); // Updated max rows to be accessible from DEFAULT to 10

        // Initialize StopWatch Object
        StopWatch stopWatch = new StopWatch();

        try {
            // Start Stopwatch
            stopWatch.start();

            // Iterate through each key set. i.e Each Sheet
            jsonObject.keySet().forEach(keySet -> {
                final Boolean[] isHeaderAdded = {false};
                SXSSFSheet xssfSheet = xssfWorkbook.createSheet(keySet);

                AtomicInteger rowCounter = new AtomicInteger(0);

                // Get Object from Key. i.e. Sheet
                Object t = jsonObject.get(keySet);

                // Convert Object To JsonArray. i.e Sheet Data to JsonArray
                JSONArray jsonArray = (JSONArray) t;

                List<Integer> fixSizeColumnArray = new ArrayList<>();
                // int columnCount = jsonArray.getJSONObject(0).length();

                // Get total number of rows in a sheet
                int totalRows = jsonArray.length();

                // Iterate through Individual Object from Array
                jsonArray.forEach(array -> {

                    if (rowCounter.get() % 100 == 0) {
                        if (rowCounter.get() == totalRows) {
                            LOGGER.info("PRINTING SHEET [" + keySet + "] - (" + rowCounter.get() + "/" + totalRows + ") Rows Written To Sheet.");
                            LOGGER.info("SHEET PRINTING [" + keySet + "] HAS FINISHED WRITING INTO CURRENT SHEET.");
                        } else {
                            LOGGER.info("PRINTING SHEET [" + keySet + "] - (" + rowCounter.get() + "/" + totalRows + ") Rows Written To Sheet.");
                        }
                    }

                    // Convert each Object from Array to JsonObject
                    JSONObject t1 = (JSONObject) array;

                    Set<String> headers = t1.keySet();

                    int colNum = 0;
                    if (!isHeaderAdded[0]) {
                        CellStyle cellStyle = xssfWorkbook.createCellStyle();
                        Font xssfFont = xssfWorkbook.createFont();
                        xssfFont.setColor(IndexedColors.WHITE.getIndex());
                        xssfFont.setBold(true);
                        cellStyle.setFont(xssfFont);
                        cellStyle.setFillForegroundColor(IndexedColors.DARK_TEAL.getIndex());
                        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

                        SXSSFRow xssfRow = xssfSheet.createRow(rowCounter.getAndAdd(1));

                        for (Object header : headers) {
                            // If the value of a particular key is not instance of JSONArray
                            if (!(t1.get(header.toString()) instanceof JSONArray)) {
                                String headerValue = String.valueOf(header);
                                SXSSFCell xssfCell = xssfRow.createCell(colNum);
                                // Apply Cell Styles
                                xssfCell.setCellStyle(cellStyle);

                                // If the string has whitespace character
                                if (headerValue.trim().contains(" ")) {
                                    xssfCell.setCellValue(headerValue.trim());
                                } else {
                                        // Convert Key To CamelCase and set in cell
                                        xssfCell.setCellValue(splitCamelCase(headerValue.trim()));
                                }

                                xssfSheet.trackAllColumnsForAutoSizing();
                                xssfSheet.autoSizeColumn(colNum);
                                colNum++;
                            }
                        }
                        isHeaderAdded[0] = true;
                    }

                    SXSSFRow dataRow = xssfSheet.createRow(rowCounter.getAndAdd(1));

                    // Iterate Through JsonObject and Get It's Value to store in excel cells.t1.get(iKeySet) instanceof JSONArray
                    AtomicInteger colCounter = new AtomicInteger(0);

                    CellStyle unScannedCellStyle = xssfWorkbook.createCellStyle();
                    unScannedCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                    unScannedCellStyle.setWrapText(true);

                    t1.keySet().forEach(iKeySet -> {

                        // Get Value of JsonNode
                        Object value = t1.get(iKeySet);
                        SXSSFCell xssfDataCell;
                        int currentIndex;
                        boolean isLongCellData = value.toString().length() > 70;

                        // If the value of a particular key is not instance of JSONArray
                        if (!(value instanceof JSONArray)) {

                            if (value.toString().equals("null")) {              // if Node value of json is null
                                // Do Nothing
                                currentIndex = colCounter.getAndAdd(1);

                                // Create cell & set Style
                                xssfDataCell = dataRow.createCell(currentIndex);
                                xssfDataCell.setCellStyle(unScannedCellStyle);

                                // Set cell value
                                xssfDataCell.setCellType(CellType.BLANK);
                            } else if (isNumeric(value.toString())) {           // If current node value is Numeric
                                currentIndex = colCounter.getAndAdd(1);

                                // Create cell & Set Style
                                xssfDataCell = dataRow.createCell(currentIndex);
                                double d = Double.parseDouble(value.toString());
                                xssfDataCell.setCellStyle(unScannedCellStyle);

                                // Set Value
                                xssfDataCell.setCellValue(d);
                            } else if (isBoolean(value.toString())) {           // is current node value is boolean
                                currentIndex = colCounter.getAndAdd(1);

                                // Create cell & Set Style
                                xssfDataCell = dataRow.createCell(currentIndex);
                                xssfDataCell.setCellStyle(unScannedCellStyle);

                                // Set cell value
                                boolean bValue = Boolean.parseBoolean(value.toString());
                                xssfDataCell.setCellValue(bValue);
                            } else {                                            // In any other case
                                currentIndex = colCounter.getAndAdd(1);

                                // Create cell & set style
                                xssfDataCell = dataRow.createCell(currentIndex);
                                xssfDataCell.setCellStyle(unScannedCellStyle);

                                // Set cell value
                                xssfDataCell.setCellValue(value.toString());
                            }

                            // If cell has data > 90 length then wrap text
                            if (isLongCellData) {
                                xssfSheet.setColumnWidth(currentIndex, 80 * 256);
                                fixSizeColumnArray.add(currentIndex);
                            } else {
                                if (!fixSizeColumnArray.contains(currentIndex)) {
                                    xssfSheet.trackAllColumnsForAutoSizing();
                                    xssfSheet.autoSizeColumn(currentIndex);
                                }
                            }
                        }
                    });
                });
                System.out.println();
            });

            String fileAbsPathWithExtension;

            if (hasXLSExtensionInFile(excelFilePath)) {
                fileAbsPathWithExtension = FilenameUtils.removeExtension(excelFilePath)
                        .concat(" - ")
                        .concat(DateUtility.getCurrentTimeStamp())
                        .concat(".")
                        .concat(fileExtension.toString().toLowerCase());
            } else {
                fileAbsPathWithExtension = switch (fileExtension) {
                    case XLS -> excelFilePath.concat(" - ").concat(DateUtility.getCurrentTimeStamp()).concat(".xls");
                    case XLSX -> excelFilePath.concat(" - ").concat(DateUtility.getCurrentTimeStamp()).concat(".xlsx");
                };
            }

            FileOutputStream fileOutputStream = new FileOutputStream(fileAbsPathWithExtension);

            // Write Data to XSSFBook
            xssfWorkbook.write(fileOutputStream);

            // Flush Data
            LOGGER.info("Flushing data to excel...!!!");
            fileOutputStream.flush();

            // Close Output stream
            fileOutputStream.close();

            LOGGER.info("[Info] Disposing temporary files created by SXSSFWorkbook...!!");

            // dispose of temporary files backing this workbook on disk
            xssfWorkbook.dispose();

            LOGGER.info("Excel file written at : " + fileAbsPathWithExtension);
        } catch (Exception ex) {
            // Stop Stopwatch in case of exception
            if (!stopWatch.isStopped()) {
                stopWatch.stop();
            }

            LOGGER.error("[Exception Block] Disposing temporary files created by SXSSFWorkbook...!!!");
            // In case of exception dispose temporary files backing this workbook on disk
            xssfWorkbook.dispose();

            LOGGER.error(ex);
            ex.printStackTrace();
        }

        // Finish stop watch in case the stop watch is still running
        if (!stopWatch.isStopped()) {
            stopWatch.stop();
        }

        LOGGER.info("Time Taken To Write Excel File : " + stopWatch);
    }

    private boolean hasXLSExtensionInFile(String filePath) {
        Pattern xlsRegexPattern = Pattern.compile("[\\w\\s\\d`~!@$%^&*()-_=+{}|;:'\"?.>,<?]*.xls$", Pattern.CASE_INSENSITIVE);
        Pattern xlsxRegexPatter = Pattern.compile("[\\w\\s\\d`~!@$%^&*()-_=+{}|;:'\"?.>,<?]*.xlsx$", Pattern.CASE_INSENSITIVE);

        Matcher xlsMatcher = xlsRegexPattern.matcher(filePath);
        Matcher xlsxMatcher = xlsxRegexPatter.matcher(filePath);

        return xlsMatcher.matches() || xlsxMatcher.matches();
    }

    /***
     * To check if the String is Boolean or not
     * @param value String
     * @return True if boolean, false otherwise
     */
    private boolean isBoolean(String value) {
        return Arrays.stream(new String[]{"true", "false", "1", "0"})
                .anyMatch(b -> b.equalsIgnoreCase(value));
    }

    /***
     * Converts Key to Pascal Case and Remove all Special Characters
     * @param text
     */
    private String getFormattedKey(String text) {
        String pascalCase = WordUtils.capitalizeFully(text).trim();
        pascalCase = pascalCase.replaceAll("[\\s()]+", "");
        pascalCase = pascalCase.replaceAll("[^A-Za-z0-9]", "");
        return pascalCase.trim();
    }

    private CellStyle setCellStyle(CellStyle cellStyle, short indexedColor) {

        cellStyle.setFillForegroundColor(indexedColor);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Set Border
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);

        // Set Border Color
        cellStyle.setTopBorderColor(IndexedColors.GREY_40_PERCENT.getIndex());
        cellStyle.setBottomBorderColor(IndexedColors.GREY_40_PERCENT.getIndex());
        cellStyle.setLeftBorderColor(IndexedColors.GREY_40_PERCENT.getIndex());
        cellStyle.setRightBorderColor(IndexedColors.GREY_40_PERCENT.getIndex());
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setWrapText(true);
        return cellStyle;
    }

    /**
     * Enum for Excel File Extension
     */
    public enum FileExtension {
        XLS,
        XLSX
    }
}


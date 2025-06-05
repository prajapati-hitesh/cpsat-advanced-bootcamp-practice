package com.ata.cpsat.framework.utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class ExcelUtility {
    private static final Logger logger = LogManager.getLogger(ExcelUtility.class.getName());


    public static Object[][] readExcelAsArray(String filePath, String sheetName, boolean includeHeaderRow) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            int totalRows = sheet.getPhysicalNumberOfRows();
            int colCount = sheet.getRow(0).getPhysicalNumberOfCells();

            int startRow = includeHeaderRow ? 0 : 1;
            int effectiveRows = totalRows - startRow;

            Object[][] data = new Object[effectiveRows][colCount];

            for (int i = 0; i < effectiveRows; i++) {
                Row row = sheet.getRow(i + startRow);
                for (int j = 0; j < colCount; j++) {
                    Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    data[i][j] = getCellValue(cell);
                }
            }
            return data;

        } catch (IOException e) {
            logger.error(e.getStackTrace());
            return new Object[0][0]; // return empty on error
        }
    }


    public static void writeExcelFromMapList(List<LinkedHashMap<String, String>> dataList, String outputPath, String sheetName) {
        if (dataList == null || dataList.isEmpty()) {
            logger.warn("Data list is empty. Nothing to write.");
            return;
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(sheetName);

            // Extract headers from first map in list
            Set<String> headerSet = dataList.get(0).keySet();
            List<String> headers = new ArrayList<>(headerSet); // Maintain header order

            // Write header row
            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < headers.size(); col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(headers.get(col));
            }

            // Write data rows
            for (int rowIndex = 0; rowIndex < dataList.size(); rowIndex++) {
                LinkedHashMap<String, String> dataRow = dataList.get(rowIndex);
                Row row = sheet.createRow(rowIndex + 1); // +1 to skip header row

                for (int colIndex = 0; colIndex < headers.size(); colIndex++) {
                    String columnName = headers.get(colIndex);
                    String cellValue = dataRow.getOrDefault(columnName, "");
                    row.createCell(colIndex).setCellValue(cellValue);
                }
            }

            // auto size columns to fit to text
            for (int i = 0; i < headers.size(); i++) {
                sheet.autoSizeColumn(i);
            }

            FileUtility.createDirectoryPathIfNotExists(new File(outputPath).getParentFile().toString());
            // Write to file
            try (FileOutputStream fos = new FileOutputStream(outputPath)) {
                workbook.write(fos);
                logger.info("Excel written to: {}", outputPath);
            }

        } catch (IOException e) {
            logger.error(e.getStackTrace());
        }
    }

    private static Object getCellValue(Cell cell) {
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> DateUtil.isCellDateFormatted(cell)
                    ? cell.getDateCellValue()
                    : cell.getNumericCellValue();
            case BOOLEAN -> cell.getBooleanCellValue();
            case FORMULA -> cell.getCellFormula(); // or evaluate it
            case BLANK -> "";
            default -> null;
        };
    }
}
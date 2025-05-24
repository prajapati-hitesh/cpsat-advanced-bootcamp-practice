package com.ata.cpsat.framework.xls2json;


import org.apache.commons.text.WordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Feliciano on 6/2/2016.
 */
public class ParsedSheet {

    private static Logger logger = LogManager.getLogger(ParsedSheet.class.getName());
    public int typeRowIndex, nameRowIndex;
    public int width;
    private Workbook workbook;
    private Sheet sheet;
    private ArrayList<ParsedCellType> types;
    private ArrayList<String> keys;
    private boolean parsed;

    public ParsedSheet(Workbook workbook, String sheetName) {
        this.workbook = workbook;
        sheet = workbook.getSheet(sheetName);

        if (sheet == null)

            throw new IllegalArgumentException("Unable to find the sheet name " + sheetName + " in the workbook.");


        typeRowIndex = 0;
        nameRowIndex = 1;

        width = 0;

        parsed = false;

        types = new ArrayList<>();
        keys = new ArrayList<>();
    }

    protected ParsedSheet parseSheet() {
        if (parsed)
            return this;

        try {
            // Fetch the type row.
            String firstColumnValue = sheet.getRow(typeRowIndex).getCell(0).getStringCellValue();
            if (!ParsedCellType.isBasicType(firstColumnValue)) {
                // If the primary key doesn't have a type defined "Basic", then we'll think all the columns are basic type,
                // and the first row is name row.
                typeRowIndex = 0;
                nameRowIndex = 0;

                Row typeRow = sheet.getRow(typeRowIndex);
                for (Iterator<Cell> cellsIT = typeRow.cellIterator(); cellsIT.hasNext(); ) {
                    Cell cell = cellsIT.next();
                    types.add(ParsedCellType.BASIC);
                }
            } else {
                // Else read the type of each column
                Row typeRow = sheet.getRow(typeRowIndex);
                for (Iterator<Cell> cellsIT = typeRow.cellIterator(); cellsIT.hasNext(); ) {
                    Cell cell = cellsIT.next();
                    String cellType = cell.getStringCellValue();
                    types.add(ParsedCellType.fromString(cellType));
                }
            }

            // Fetch the name row.
            Row nameRow = sheet.getRow(nameRowIndex);
            for (Iterator<Cell> cellsIT = nameRow.cellIterator(); cellsIT.hasNext(); ) {
                Cell cell = cellsIT.next();
                switch (cell.getCellType()) {
                    case STRING:
                        keys.add(cell.getStringCellValue());
                        break;
                    case BOOLEAN:
                        keys.add(String.valueOf(cell.getNumericCellValue()));
                        break;
                    case FORMULA:
                        keys.add(String.valueOf(((XSSFCell) cell).getRawValue()));
                        break;
                }

                width++;
            }
        } catch (Exception e) {
            logger.error(e.getStackTrace());
        }

        parsed = true;

        return this;
    }

    protected Workbook getWorkbook() {
        return workbook;
    }

    protected Sheet getSheet() {
        return sheet;
    }

    protected Sheet getSheet(String sheetName) {
        Sheet sheet = workbook.getSheet(sheetName);
        try {
            if (sheet == null)
                throw new IllegalArgumentException();
        } catch (IllegalArgumentException ex) {
            logger.error("Unable to find the sheet name " + sheetName + " in the workbook.");
        }
        return sheet;
    }

    protected boolean isParsed() {
        return parsed;
    }

    protected ParsedCellType getType(int index) {
        try {
            if (!isParsed())
                throw new NullPointerException();
        } catch (NullPointerException ex) {
            logger.error("This sheet haven't been parsed, please call parseSheet() method first!");
        }

        return types.get(index);
    }

    protected String getKey(int index) {
        if (!isParsed())
            throw new NullPointerException("This sheet haven't been parsed, please call parseSheet() method first!");

        return getFormattedKey(keys.get(index));
    }

    protected int indexOfKey(String key) {
        if (!isParsed())
            throw new NullPointerException("This sheet haven't been parsed, please call parseSheet() method first!");

        return keys.indexOf(key);
    }

    /***
     * Converts Key to Pascal Case and Remove all Special Characters
     * @param text
     * @return
     */
    private String getFormattedKey(String text) {
        String pascalCase = WordUtils.capitalizeFully(text).trim();
        pascalCase = pascalCase.replaceAll("[\\s()]+", "");
        pascalCase = pascalCase.replaceAll("[^A-Za-z0-9]", "");
        return pascalCase.trim();
    }

}

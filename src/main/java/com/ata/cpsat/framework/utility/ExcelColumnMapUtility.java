package com.ata.cpsat.framework.utility;

public class ExcelColumnMapUtility {

    /**
     * To get the Column value i.e. A, B, C. ... from index
     *
     * @param columnIndex Excel column index in <code>int</code>
     * @return Column value in <code>String</code>
     */
    public static String getColumnNameFromIndex(int columnIndex) {
        // To store result (Excel column name)
        StringBuilder columnName = new StringBuilder();

        // Since the method considers column index from 1 i.e. A = 1, increment columnIndex
        columnIndex++;

        while (columnIndex > 0) {
            // Find remainder
            int rem = columnIndex % 26;

            // If remainder is 0, then a
            // 'Z' must be there in output
            if (rem == 0) {
                columnName.append("Z");
                columnIndex = (columnIndex / 26) - 1;
            } else // If remainder is non-zero
            {
                columnName.append((char) ((rem - 1) + 'A'));
                columnIndex = columnIndex / 26;
            }
        }

        // Reverse the string and return the result
        return columnName.reverse().toString();
    }
}

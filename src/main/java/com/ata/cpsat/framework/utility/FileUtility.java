package com.ata.cpsat.framework.utility;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class FileUtility {
    private static final Logger LOGGER = LogManager.getLogger(FileUtility.class.getName());

    /**
     * To check if file exists in file path provided as Parameter
     *
     * @param filePath String <p> Absolute file path </p>
     * @return boolean <p> True if exists, false otherwise. </p>
     */
    public static boolean isFileExist(String filePath) {
        boolean isPresent = false;

        if (!StringUtils.isBlank(filePath)) {
            try {
                File fileObj = new File(filePath);
                isPresent = fileObj.exists();

            } catch (Exception ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
        }
        return isPresent;
    }


    /**
     * To create Directory path including parents
     *
     * @param dirPath String <p> A Path to Directory </p>
     */
    public static void createDirectoryPathIfNotExists(String dirPath) {
        try {
            File dirFile = new File(dirPath);

            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }


    /**
     * To create Directory
     *
     * @param dirPath String <p>  A Path to Directory .</p>
     */
    public static void createDirectoryIfNotExists(String dirPath) {
        try {
            File dirFile = new File(dirPath);

            if (!dirFile.exists()) {
                dirFile.mkdir();
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }
}

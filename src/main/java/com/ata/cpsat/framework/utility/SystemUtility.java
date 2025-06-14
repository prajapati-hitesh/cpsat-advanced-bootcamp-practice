package com.ata.cpsat.framework.utility;

import java.io.File;
import java.nio.file.FileSystems;

public class SystemUtility {

    /**
     * @return User working directory
     */
    public static String getUserDirectory() {
        return System.getProperty("user.dir");
    }

    /**
     * @return Character that separates components of a file path. This is "/" on UNIX and "\" on Windows.
     */
    public static String getFileSeparator() {
        return FileSystems.getDefault().getSeparator();
    }

    /**
     * @return Operating system name
     */
    public static String getOperatingSystemName() {
        return System.getProperty("os.name");
    }

    /**
     * Operating system architecture
     *
     * @return
     */
    public static String getOperatingSystemArchitecture() {
        return System.getProperty("os.arch");
    }

    /**
     * @return Operating system version
     */
    public static String getOperatingSystemVersion() {
        return System.getProperty("os.version");
    }

    /**
     * @return Path separator character used in java.class.path
     */
    public static String getPathSeparator() {
        return File.pathSeparator;
    }

    /**
     * @return User home directory
     */
    public static String getUserHomeDirectory() {
        return System.getProperty("user.home");
    }

    /**
     * @return User account name
     */
    public static String getUserAccountName() {
        return System.getProperty("user.name");
    }

    /**
     * @return Sequence used by operating system to separate lines in text files
     */
    public static String getLineSeparator() {
        return System.lineSeparator();
    }

    /**
     * @return Default temp file path
     */
    public static String getDefaultTempFilePath() {
        return System.getProperty("java.io.tmpdir");
    }
}

package com.ata.cpsat.framework.utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class ResourceUtility {
    private static final Logger logger = LogManager.getLogger(ResourceUtility.class.getName());

    /**
     * To get Absolute File Path of a resource
     *
     * @param resourceFileName Name of Resource File
     * @return Absolute file path
     */
    public static File getResourceAsFile(String resourceFileName) throws URISyntaxException {
        // Get Resource as URL
        URL resource = ResourceUtility.class.getClassLoader().getResource(resourceFileName);

        if (resource == null) {
            throw new IllegalArgumentException("Resource file [" + resourceFileName + "] Not found in classpath");
        } else {
            return new File(resource.toURI());
        }
    }

    /**
     * To get Absolute File Path of a resource
     *
     * @param resourceFileName Name of Resource File
     * @return Absolute file path
     */
    public static URI getResourceFilePath(String resourceFileName) throws URISyntaxException {

        // Get Resource as URL
        URL resource = ResourceUtility.class.getClassLoader().getResource(resourceFileName);

        if (resource == null) {
            throw new IllegalArgumentException("Resource file [" + resourceFileName + "] Not found in classpath");
        } else {
            return resource.toURI();
        }
    }
}

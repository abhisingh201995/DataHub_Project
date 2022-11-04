package com.ukg.datahub.perf.helpers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SystemValueHandler {

    private static Logger log = LogManager.getLogger(SystemValueHandler.class);
    private static String CONF_FILE = "conf/testExecution.properties";

    /**
     * Fetches a property related to the execution.
     *
     * @param in_propertyName Name of the property to retrieve
     * @return null if property not found, or is empty
     */
    public static String fetchExecutionProperty(String in_propertyName) {
        return fetchExecutionProperty(in_propertyName, null);
    }

    /**
     * Fetches a property related to the execution or a default value of not
     * found.
     *
     * @param in_propertyName Name of the property to retrieve
     * @param in_defaultValue Default value to return if property is not found
     * @return in_defaultValue if property not found, or is empty
     */
    public static String fetchExecutionProperty(String in_propertyName, String in_defaultValue) {
        if (System.getenv(in_propertyName) != null) {
            return System.getenv(in_propertyName);
        } else if (CONF_FILE == null) {
            return in_defaultValue;
        } else {
            String value = null;
            value = fetchProperty(in_propertyName, CONF_FILE);
            if (value == null)
                return in_defaultValue;
            return value;
        }
    }

    /**
     * This method specifically fetches the property from the execution property files
     *
     * @param propertyName
     * @param propertyFile
     * @return null if file not found, property not found, or is empty
     */
    public static String fetchFileProperty(String propertyName, String propertyFile) {

        Properties prop = new Properties();
        InputStream input = null;
        String returnString = null;

        try {
        /*    input = new FileInputStream(propertyFile);
            prop.load(input);
            returnString = prop.getProperty(propertyName);*/

            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            try (InputStream resourceStream = loader.getResourceAsStream(propertyFile)) {
                prop.load(resourceStream);
                returnString = prop.getProperty(propertyName);
            }
        } catch (FileNotFoundException e) {
            log.error("ERROR : The property " + propertyName
                    + " cannot be resolved. It was not in the system properties, nor could it be found in the file "
                    + propertyFile + " in the standard directory "
                    + ". ");
            log.error(e);
        } catch (IOException e) {
            log.error("ERROR : Something in the property selection went wrong" + e);
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (Exception e) {
                log.error(e);
            }
        }
        if (returnString != null && returnString.isEmpty()) {
            return null;
        } else {
            return returnString;
        }
    }

    /**
     * This method retrieves a property given its file and name
     *
     * @param in_propertyName Name of the property to retrieve
     * @param in_propertyFile The file to retrieve the property from
     * @return null if file not found, property not found, or is empty
     */
    public static String fetchProperty(String in_propertyName, String in_propertyFile) {
        return fetchFileProperty(in_propertyName, in_propertyFile);
    }
}

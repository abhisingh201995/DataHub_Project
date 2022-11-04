package com.ukg.datahub.perf.utilities;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Properties;

public class ResourceUtil {

    private static Logger logger = LogManager.getLogger(ResourceUtil.class);

    /**
     * Get content of file filePath located inside resources folder.
     * Removes leading and trailing spaces.
     *
     * @param filePath
     * @return
     */
    public static String getResourceFileContentAsString(String filePath) {
        return getResourceFileContentAsString(filePath, true, false);
    }

    /**
     * Get content of file file filePath located inside resources folder.
     * Behaviour could be modified with flags (trimLines and addLineEndingSpaces)
     *
     * @param filePath
     * @param trimLines
     * @param addLineEndingSpaces
     * @return
     */
    public static String getResourceFileContentAsString(String filePath, Boolean trimLines,
                                                        Boolean addLineEndingSpaces) {
        return getFileContentAsString(filePath, true, trimLines, addLineEndingSpaces);
    }

    /**
     * Get content of file filePath
     * Behaviour could be modified with flags (trimLines and addLineEndingSpaces)
     *
     * @param filePath
     * @param trimLines
     * @param addLineEndingSpaces
     * @return
     */
    public static String getExternalFileContentAsString(String filePath, Boolean trimLines,
                                                        Boolean addLineEndingSpaces) {
        return getFileContentAsString(filePath, false, trimLines, addLineEndingSpaces);
    }

    private static String getFileContentAsString(String filePath, Boolean isResource,
                                                 Boolean trimLines, Boolean addLineEndingSpaces) {
        BufferedReader reader = null;
        InputStream inputStream = null;
        StringBuilder stringBuilder = null;
        if (isResource) {
            inputStream = ResourceUtil.class.getClassLoader().getResourceAsStream(filePath);
            try {
                reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                logger.error("File content reading failed : ", e);
            }
        } else {
            try {
                reader = new BufferedReader(new FileReader(filePath));
            } catch (FileNotFoundException e) {
                logger.error("File not exist on file system : ", e);
            }
        }

        stringBuilder = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                if (trimLines) {
                    line = line.trim();
                }
                if (addLineEndingSpaces) {
                    line = line + " ";
                }
                stringBuilder.append(line);
            }
            reader.close();
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException e) {
            logger.error("File content reading failed : ", e);
        }
        return stringBuilder.toString();
    }

    public static File loadResourceFile(String resourceFilePath, boolean mandatory) {
        if (StringUtils.isEmpty(resourceFilePath)) {
            throw new IllegalArgumentException("The given file path is empty/null.");
        }
        String errorMessage;
        ClassLoader classLoader = ResourceUtil.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(resourceFilePath);
        if (inputStream == null) {
            errorMessage = "The given resource " + resourceFilePath + " could not be found in the project.";
            logger.error(errorMessage);
        } else {
            try {
                File file = new File(resourceFilePath);
                FileUtils.copyInputStreamToFile(inputStream, file);
                return file;
            } catch (IOException e) {
                errorMessage = "The given resource " + resourceFilePath + " could not be read";
                logger.error(errorMessage, e);
            }
        }
        if(mandatory){
            throw new IllegalArgumentException(errorMessage);
        }
        return null;
    }
}

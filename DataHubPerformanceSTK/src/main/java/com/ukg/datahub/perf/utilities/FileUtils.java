package com.ukg.datahub.perf.utilities;

import org.apache.commons.io.IOUtils;
import org.apache.commons.text.StringSubstitutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtils {

    private static Logger log = LogManager.getLogger(FileUtils.class);
    private static String configPath = "conf";

    /**
     * This is the template that we load from a file. It contains a set of
     * replacement pointers referenced with the standard notation
     * '${replace-me}'
     */
    private String template;

    /**
     * The pool of replacements that need to be applied in the template.
     */
    private Map<String, String> replacements;

    /**
     * This is the current replacement source.
     */
    private String currentSource;


    public FileUtils() throws IOException {
        replacements = new HashMap<>();
    }

    /**
     * This method starts a replacement pattern by defining the source part of
     * the current replacement pattern.
     *
     * @param source Source of the current replacement pattern.
     * @return the current instance.
     */
    public FileUtils replacing(String source) {
        this.currentSource = source;
        return this;
    }

    /**
     * This method finishes the current replacement pattern by defining its
     * target part.
     *
     * @param target Target of the current replacement pattern.
     * @return the current instance.
     */
    public FileUtils by(String target) {
        if (this.currentSource == null) {
            throw new RuntimeException("Wrong replacement! "
                    + "You cannot call the 'by' method without calling the 'replacing' one just before. ");
        }
        replacements.put(this.currentSource, target);
        this.currentSource = null;
        return this;
    }

    /**
     * This method applies the replacement patterns to the initial template.
     *
     * @return The resolved version of the template in which the refactoring
     * patterns have been applied.
     */
    public String resolve() {
        StringSubstitutor substitutor = new StringSubstitutor(
                this.replacements);
        return substitutor.replace(this.template);
    }

    /**
     * This method applies the replacement patterns to the initial template.
     *
     * @return The resolved version of the template in which the refactoring
     * patterns have been applied.
     */
    public static String resolve(String fileForReplacing, Map<String, String> replacements) {
        StringSubstitutor substitutor = new StringSubstitutor(replacements);
        return substitutor.replace(fileForReplacing);
    }

    /**
     * This method loads an template from a file.
     *
     * @param path Path of the Query template containing the replacement pointers.
     * @return the current instance.
     * @throws IOException This method throws an exception if any error occcurs while
     *                     calling the ACC APIs.
     */
    public static FileUtils loadFile(String path) throws IOException {
        FileUtils template = new FileUtils();
        template.template = fileToString(path);
        log.debug("File with filepath: " + path + " is loaded.");
        return template;
    }

    /**
     * Method for reading text files and converting them to string
     *
     * @param filePath use relative path to the file
     * @return
     * @throws IOException
     * @author Dino Rac
     */
    public static String fileToString(String filePath) throws IOException {
        ClassLoader classLoader = FileUtils.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(filePath);

        assert inputStream != null;
        String queryBody = IOUtils.toString(inputStream,
                StandardCharsets.UTF_8);
        return queryBody;
    }

    /**
     * Retrieves all properties files available in the config path of the project
     * @return an {@link ArrayList < File >} containing all config files and null if none are found.
     */
    public static ArrayList<File> getConfFiles() {
        File[] files = fetchFile(getConfigPath()).listFiles();
        ArrayList<File> confFiles = new ArrayList<>();
        try {
            for (File file:files) {
                if (fetchFileExtension(file).equalsIgnoreCase("properties")) {
                    confFiles.add(file);
                }
            }
            return confFiles;
        }
        catch (NullPointerException npe) {
            log.error("No configuration file was found.");
            return null;
        }
    }

    /**
     * Returns the configuration path of the current execution.
     * @return the configPath
     */
    public static String getConfigPath() {
        return configPath;
    }

    /**
     * This method fetches an existing file in the source hierarchy. If the file
     * does not exist it creates a new file object.
     *
     * @param in_filePath
     *        The path of the file.
     * @return The fetched file.
     */
    public static File fetchFile(String in_filePath) {
        URL l_resourceUrl = Thread.currentThread().getContextClassLoader().getResource(in_filePath);

        // Related to the transformation of @ to %40 - System.out.println("DEBUG : AFTER "+l_resourceUrl.toString());
        if (l_resourceUrl == null) {
            log.error("The given resource " + in_filePath + " could not be found in the project code. Will be returning a newly created file.");
            return new File(in_filePath);
        }

        // Related to the transformation of @ to %40 System.out.println("DEBUG : "+l_resourceUrl.toString());
        try {
            l_resourceUrl = decodeURLPath(l_resourceUrl);
        } catch (MalformedURLException e) {
            log.error(e);
        }

        return new File(l_resourceUrl.getFile());
    }


    /**
     * Fetch file extension.
     *
     * @param file
     *        File to fetch extension
     * @return Extension of the file
     */
    public static String fetchFileExtension(File file) {
        if (file == null) {
            throw new IllegalArgumentException("[integro-core] The give file was null");
        }

        String name = file.getName();
        try {
            return name.substring(name.lastIndexOf('.') + 1);
        } catch (IndexOutOfBoundsException e) {
            log.error("fetchFileExtension: " + e);
            return "";
        }
    }

    /**
     * Escapes the URL path if it has codings such as %20 and %40
     *
     * @param in_url
     * @return a decoded string
     * @throws MalformedURLException
     */
    public static URL decodeURLPath(URL in_url) throws MalformedURLException {

        return new URL(decodeURLPath(in_url.toString()));
    }

    /**
     * Escapes the URL path if it has codings such as %20 and %40
     *
     * @param in_string
     * @return a decoded string
     */
    public static String decodeURLPath(String in_string) {
        String lr_decodedString = "";
        try {
            lr_decodedString = URLDecoder.decode(in_string, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error(e);
        }
        return lr_decodedString;
    }

}

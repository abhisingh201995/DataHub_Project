package com.ukg.datahub.perf.utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommonUtils {

    /**
     * This method will return a random string of given length
     *
     * @param targetStringLength
     * @return
     */
    public static String generateRandomString(int targetStringLength) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();
        return random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    /**
     * This method will generate a random integer
     *
     * @return
     */
    public static int generateRandomNumber() {
        // Generate random integers in range 0 to 999
        return ThreadLocalRandom.current().nextInt();
    }

    public static void sleep(long in_waitTimeMs) {
        try {
            Thread.sleep(in_waitTimeMs);
        } catch (InterruptedException var3) {
            Thread.currentThread().interrupt();
        }
    }

    public static String formatSeconds(int timeInSeconds)
    {
        int secondsLeft = timeInSeconds % 3600 % 60;
        int minutes = (int) Math.floor(timeInSeconds % 3600 / 60);
        int hours = (int) Math.floor(timeInSeconds / 3600);

        String HH = ((hours       < 10) ? "0" : "") + hours;
        String MM = ((minutes     < 10) ? "0" : "") + minutes;
        String SS = ((secondsLeft < 10) ? "0" : "") + secondsLeft;

        return HH + " Hours : " + MM + " Minutes : " + SS + " Seconds";
    }

    /**
     * This method will convert delimited string to list
     * @param maxConcurrentApiCalls
     * @return
     */
    public static List convertDelimitedStringToList(String maxConcurrentApiCalls , String delimiter) {
        return Stream.of(maxConcurrentApiCalls.split(delimiter))
                .collect(Collectors.toList());
    }


    /**
     * This method will return the String format of the date
     * @param date
     * @return
     */
    public static String getDateInStringFormat(Date date) {
        StringBuffer retBuf = new StringBuffer();
        if (date == null) {
            date = new Date();
        }
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        retBuf.append(df.format(date));
        return retBuf.toString();
    }

    public static String removeSpecialChars(String str){
        return str.replaceAll("[^\\\\A-Za-z0-9]", "");
    }

    /**
     * This method will return the simple date format
     * of the required pattern
     * @param pattern
     * @return
     */
    public static SimpleDateFormat getSimpleDateFormat(String pattern){
        return  new SimpleDateFormat(pattern);
    }

}

package com.Utilities;

import org.apache.log4j.Logger;

public class Log extends SuiteInitializer{

    private static Logger Log = Logger.getLogger(Log.class.getName());

    public static void info (String message) {
        Log.info(message);
    }

    public static void error (String message) {
        Log.error(message);
    }

    public static void debug (String message) {
        Log.debug(message);
    }
}

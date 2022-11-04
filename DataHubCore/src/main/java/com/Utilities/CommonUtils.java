package com.Utilities;

public class CommonUtils {

    public static void sleep(long in_waitTimeMs) {
        try {
            Thread.sleep(in_waitTimeMs);
        } catch (InterruptedException var3) {
            Thread.currentThread().interrupt();
        }
    }

}

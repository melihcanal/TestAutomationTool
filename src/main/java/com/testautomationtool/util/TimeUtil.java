package com.testautomationtool.util;

public class TimeUtil {

    public static void waitForTime(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

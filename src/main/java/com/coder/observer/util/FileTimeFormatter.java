package com.coder.observer.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class FileTimeFormatter {
    private final static DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss").withZone(ZoneId.systemDefault());

    public static String format(LocalDateTime localDateTime) {
        return convertLocalDateTimeToString(localDateTime);
    }

    private static String convertLocalDateTimeToString(LocalDateTime localDateTime) {
        return dateTimeFormatter.format(localDateTime);
    }
}

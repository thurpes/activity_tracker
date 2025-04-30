package com.web.activity_tracker.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtil {

    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Convert a string to LocalDateTime
     * @param dateStr Date string
     * @return LocalDateTime object
     */
    public static LocalDateTime parseDateTime(String dateStr) {
        try {
            return LocalDateTime.parse(dateStr, DEFAULT_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected: " + DEFAULT_FORMATTER.toString());
        }
    }

    /**
     * Format LocalDateTime to string
     * @param dateTime LocalDateTime object
     * @return Formatted date string
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DEFAULT_FORMATTER);
    }

    /**
     * Get current date time
     * @return Current LocalDateTime
     */
    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }

    /**
     * Get start of day for a given date
     * @param dateTime LocalDateTime
     * @return LocalDateTime at start of day
     */
    public static LocalDateTime getStartOfDay(LocalDateTime dateTime) {
        return dateTime.toLocalDate().atStartOfDay();
    }

    /**
     * Get end of day for a given date
     * @param dateTime LocalDateTime
     * @return LocalDateTime at end of day
     */
    public static LocalDateTime getEndOfDay(LocalDateTime dateTime) {
        return dateTime.toLocalDate().atTime(23, 59, 59, 999999999);
    }
}
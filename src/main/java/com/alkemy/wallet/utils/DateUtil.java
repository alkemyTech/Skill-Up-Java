package com.alkemy.wallet.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static java.time.temporal.ChronoUnit.DAYS;

public class DateUtil {

    public static final int MIN_DAYS = 30;

    public static LocalDate string2LocalDate(String dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        return LocalDate.parse(dateTime, formatter);
    }

    public static Long daysBetween2Dates(LocalDate date1, LocalDate date2) {
        return DAYS.between(date1, date2);
    }
}

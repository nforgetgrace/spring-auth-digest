package com.ktds.smartx.digest.api.common.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * packageName    : com.ktds.smartx.digest.api.common.utils
 * fileName       : DateTimeUtils
 * author         : Jae Gook Jung
 * date           : 2023/08/22
 * description    : 시간 포멧 마추는 기능
 * project name   : digest
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/08/22        Jae Gook Jung       최초 생성
 */
public class DateTimeUtils {
    public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static final DateTimeFormatter hyphenDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static final DateTimeFormatter noDelimiterDateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSS");

    public static String format(LocalDate date) {
        return dateFormatter.format(date);
    }

    public static String formatHyphen(LocalDate date) {
        return hyphenDateFormatter.format(date);
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTimeFormatter.format(dateTime);
    }

    public static String formatNoDelimiter(LocalDateTime dateTime) {
        return noDelimiterDateTimeFormatter.format(dateTime);
    }
}

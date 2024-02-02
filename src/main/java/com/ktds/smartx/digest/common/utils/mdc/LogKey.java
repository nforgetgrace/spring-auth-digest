package com.ktds.smartx.digest.common.utils.mdc;

import org.slf4j.MDC;

import java.util.UUID;

/**
 * packageName    : com.ktds.smartx.digest.common.utils.mdc
 * fileName       : LogKey
 * author         : Jae Gook Jung
 * date           : 2023/08/23
 * description    : MDC 로그키 관리 클래스
 * project name   : digest
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/08/23        Jae Gook Jung       최초 생성
 */

public final class LogKey {

    protected static final String HEADER_LOG_KEY = "logKey";

    private LogKey() {
    }

    public static String getLogKeyName() {
        return HEADER_LOG_KEY;
    }

    public static String get() {
        return MDC.get(HEADER_LOG_KEY);
    }

    public static void put(String keyValue) {
        MDC.put(HEADER_LOG_KEY, keyValue);
    }

    public static void remove() {
        MDC.remove(HEADER_LOG_KEY);
    }

    public static String createLogKey() {
        return UUID.randomUUID().toString();
    }
}


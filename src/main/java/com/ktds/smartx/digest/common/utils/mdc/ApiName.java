package com.ktds.smartx.digest.common.utils.mdc;

import org.slf4j.MDC;

/**
 * packageName    : com.ktds.smartx.digest.common.utils.mdc
 * fileName       : ApiName
 * author         : Jae Gook Jung
 * date           : 2023/08/23
 * description    : MDC 로깅
 * project name   : digest
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/08/23        Jae Gook Jung       최초 생성
 */

public final class ApiName {

    protected static final String HEADER_API_NAME = "apiName";

    private ApiName() {
    }

    public static String getApiName() {
        return HEADER_API_NAME;
    }

    public static String get() {
        return MDC.get(HEADER_API_NAME);
    }

    public static void put(String keyValue) {
        MDC.put(HEADER_API_NAME, keyValue);
    }

    public static void remove() {
        MDC.remove(HEADER_API_NAME);
    }
}

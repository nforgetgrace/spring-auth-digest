package com.ktds.smartx.digest.common.utils.mdc;

import org.slf4j.MDC;

/**
 * packageName    : com.ktds.smartx.digest.common.utils.mdc
 * fileName       : Hello
 * author         : Jae Gook Jung
 * date           : 2023/08/23
 * description    : MDC 로깅시 MDC에 추가할 샘플 파일
 * project name   : digest
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/08/23        Jae Gook Jung       최초 생성
 */

public final class Hello {

    protected static final String HEADER_API_NAME = "hello";

    private Hello() {
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

package com.ktds.smartx.digest.common.exception.messege;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * packageName    : com.ktds.smartx.digest.common.exception.messege
 * fileName       : ApiErrorCode
 * author         : HONG IL SON
 * date           : 2023-11-02
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-11-02        HONG IL SON       최초 생성
 */

@Getter
@AllArgsConstructor
public enum ApiErrorCode implements ErrorCode {

    DB_ERROR("API_000", "데이터베이스 문제가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR)
    ;

    String code;
    String message;
    HttpStatus httpStatus;

    ApiErrorCode(String code, String message) {
        this(code, message, HttpStatus.BAD_REQUEST);
    }
}

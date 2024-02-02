package com.ktds.smartx.digest.common.exception.messege;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


/**
 * packageName    : com.ktds.smartx.digest.common.exception.messege
 * fileName       : MovieErrorCode
 * author         : Jae Gook Jung
 * date           : 2023/08/23
 * description    : errorCode 인터페이스를 받았으니 @Getter를 사용해야함.
 * project name   : digest
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/08/23        Jae Gook Jung       최초 생성
 */

@Getter
@AllArgsConstructor
public enum MovieErrorCode implements ErrorCode{

    MOVIE_SEARCH_COMMON("AIRPAY_COMMON","죄송합니다. 블라블라\n" +
            "자세한 문의는 관리자를 통해 접수 바랍니다.\n");

    String code;
    String message;
    HttpStatus httpStatus;

    MovieErrorCode(String code, String message) {
        this(code, message, HttpStatus.SERVICE_UNAVAILABLE);
    }
}

package com.ktds.smartx.digest.common.exception.messege;

import org.springframework.http.HttpStatus;

/**
 * packageName    : com.ktds.smartx.digest.common.exception.messege
 * fileName       : ErrorCode
 * author         : Jae Gook Jung
 * date           : 2023/08/23
 * description    : 에러코드 인터페이스
 * project name   : digest
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/08/23        Jae Gook Jung       최초 생성
 */

public interface ErrorCode {
    String getCode();
    String getMessage();
    HttpStatus getHttpStatus();
}

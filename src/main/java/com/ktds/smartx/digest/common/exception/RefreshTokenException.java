package com.ktds.smartx.digest.common.exception;

import com.ktds.smartx.digest.common.exception.messege.ErrorCode;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * packageName    : com.ktds.smartx.digest.common.exception
 * fileName       : RefreshTokenException
 * author         : Jae Gook Jung
 * date           : 2023/08/23
 * description    :
 * project name   : digest
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/08/23        Jae Gook Jung       최초 생성
 */


@Getter
public class RefreshTokenException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final ErrorCode errorCode;
    private final List<String> detail;

    public RefreshTokenException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.detail = null;
    }

    public RefreshTokenException(ErrorCode errorCode, String detail) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.detail = Arrays.asList(detail);
        //this.detail = Collections.singletonList(detail);
    }

}

package com.ktds.smartx.digest.common.response.dto;

import java.time.LocalDate;

import com.ktds.smartx.digest.common.utils.mdc.LogKey;
import com.ktds.smartx.digest.api.common.utils.DateTimeUtils;

import lombok.Getter;


/**
 * packageName    : com.ktds.smartx.digest.common.response.advice
 * fileName       : ApiCommonResponse
 * author         : Jae Gook Jung
 * date           : 2023/08/23
 * description    : 공통응답 객체 data안에는 API메시지
 * project name   : digest
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/08/23        Jae Gook Jung       최초 생성
 */


@Getter
public final class ApiCommonResponse {
    /**/
    private String logKey;

    private Object data;

    private String serverDt;

    public ApiCommonResponse(Object data) {
        this.logKey = LogKey.get();
        this.data = data; //API
        this.serverDt = DateTimeUtils.formatHyphen(LocalDate.now());
    }
}

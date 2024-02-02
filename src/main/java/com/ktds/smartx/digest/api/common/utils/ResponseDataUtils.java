package com.ktds.smartx.digest.api.common.utils;

import java.util.Collection;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@Service
public class ResponseDataUtils {

    private String code;
    private String message;
    private int count;
    private Object data;

    public ResponseDataUtils selectResponse(Object data) {
        // data가 Null 일때 코드
        if (Objects.isNull(data)) {
            this.code = CommonResponseCode.SELECT_NULL_COMPLETE.code;
            this.message = CommonResponseCode.SELECT_NULL_COMPLETE.message;
            this.data = null;
            this.count = 0;
        }
        else {
            this.code = CommonResponseCode.SELECT_COMPLETE.code;
            this.message = CommonResponseCode.SELECT_COMPLETE.message;
            this.data = data;
            this.count = 0;
        }
        return this;
    }
    public ResponseDataUtils pagingSelectResponse(Object data, int count) {

        // data가 Null 일때 코드
        if (CollectionUtils.isEmpty((Collection<?>) data)) {
            this.code = CommonResponseCode.SELECT_NULL_COMPLETE.code;
            this.message = CommonResponseCode.SELECT_NULL_COMPLETE.message;
            this.data = null;
            this.count = 0;
        }
        else {

            this.code = CommonResponseCode.SELECT_COMPLETE.code;
            this.message = CommonResponseCode.SELECT_COMPLETE.message;
            this.data = data;
            this.count = count;
        }
        return this;
    }

    public ResponseDataUtils updateResponse(Object data) {

        this.data = data;
        this.code = CommonResponseCode.UPDATE_COMPLETE.code;
        this.message = CommonResponseCode.UPDATE_COMPLETE.message;
        this.count = 0;

        return this;
    }

    public ResponseDataUtils insertResponse(Object data) {

        this.data = data;
        this.code = CommonResponseCode.INSERT_COMPLETE.code;
        this.message = CommonResponseCode.INSERT_COMPLETE.message;
        this.count = 0;

        return this;
    }

    public ResponseDataUtils deleteResponse(Object data) {

        this.data = data;
        this.code = CommonResponseCode.DELETE_COMPLETE.code;
        this.message = CommonResponseCode.DELETE_COMPLETE.message;
        this.count = 0;

        return this;
    }

    public ResponseDataUtils checkResponse(Object data) {

        this.data = data;
        this.code = CommonResponseCode.SELECT_COMPLETE.code;
        this.message = CommonResponseCode.SELECT_COMPLETE.message;
        this.count = 0;

        return this;
    }

    @Getter
    @AllArgsConstructor
    public enum CommonResponseCode implements ResponseCode {

        SELECT_COMPLETE("SUCCESS_000", "조회 완료"),
        INSERT_COMPLETE("SUCCESS_001", "INSERT 완료"),
        UPDATE_COMPLETE("SUCCESS_002", "Update 완료"),
        SELECT_NULL_COMPLETE("SUCCESS_003", "조회된 데이터가 없습니다."),
        DELETE_COMPLETE("SUCCESS_004", "DELETE 완료");

        String code;
        String message;
    }
}

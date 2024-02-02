package com.ktds.smartx.digest.api.common.utils;

import org.springframework.stereotype.Component;

/**
 * packageName : com.ktds.smartx.digest.api.common.utils
 * fileName : CommonUtils
 * author : seong hun Pyo
 * date : 2023/10/11
 * description : 공통함수 관리
 * project name : digest
 * ===========================================================
 * DATE AUTHOR NOTE
 * -----------------------------------------------------------
 * 2023/08/22 seong hun Pyo 최초 생성
 */
@Component
public class CommonUtils {

    // null 체크
    public boolean checkNullVal(String val) {

        boolean ret = false;

        if (val.isEmpty() || val == null) {
            ret = true;
        }

        return ret;
    }
}

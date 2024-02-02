package com.ktds.smartx.digest.api.common.utils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * packageName    : com.ktds.smartx.digest.api.common.utils
 * fileName       : ConvertUnitData
 * author         : Jae Gook Jung
 * date           : 12/6/23
 * description    :
 * project name   : digest-backend
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 12/6/23        Jae Gook Jung       최초 생성
 * 12/
 */
public class ConvertUnitData {

    public Map<String, String> extractFromDevId(String sysDevId){

        // 정규식 패턴
        Pattern pattern = Pattern.compile("(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})([A-Z]+)");
        Matcher matcher = pattern.matcher(sysDevId);

        // 매칭
        if (matcher.matches()) {
            // 매칭된 그룹 추출
            Map<String, String> resultMap = new LinkedHashMap<>();
            resultMap.put("mltr_rgn", matcher.group(1));
            resultMap.put("mltr_crps", matcher.group(2));
            resultMap.put("mltr_div", matcher.group(3));
            resultMap.put("mltr_bde", matcher.group(4));
            resultMap.put("mltr_bn", matcher.group(5));
            resultMap.put("sys_cd", matcher.group(6));

            return resultMap;
        } else {
            return null;
        }
    }

    public String createDevId(Map<String, String> systemInfo, int nextSeq) {
        systemInfo.put("idtf_cd", String.format("%05d", nextSeq));

        return String.join("", systemInfo.values());
    }
}

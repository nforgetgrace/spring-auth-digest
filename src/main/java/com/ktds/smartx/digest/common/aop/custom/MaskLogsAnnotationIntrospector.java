package com.ktds.smartx.digest.common.aop.custom;

import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;
import com.ktds.smartx.digest.common.aop.custom.annotation.MaskLogs;

/**
 * packageName    : com.ktds.smartx.digest.common.aop.custom
 * fileName       : MaskLogsAnnotationIntrospector_
 * author         : Jae Gook Jung
 * date           : 2023/08/23
 * description    :
 * project name   : digest
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/08/23        Jae Gook Jung       최초 생성
 */
public class MaskLogsAnnotationIntrospector extends NopAnnotationIntrospector {
    private static final long serialVersionUID = 1L;

    @Override
    public Object findSerializer(Annotated am) {
        MaskLogs maskLogs = am.getAnnotation(MaskLogs.class);
        if (maskLogs != null) {
            return MaskLogsSerializer.class;
        }
        return null;
    }
}

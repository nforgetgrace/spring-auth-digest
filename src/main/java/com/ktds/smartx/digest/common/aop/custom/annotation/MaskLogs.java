package com.ktds.smartx.digest.common.aop.custom.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * packageName    : com.ktds.smartx.digest.common.aop.custom.annotation
 * fileName       : MaskLogs
 * author         : Jae Gook Jung
 * date           : 2023/08/23
 * description    :
 * project name   : digest
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/08/23        Jae Gook Jung       최초 생성
 */
@Target({ ElementType.ANNOTATION_TYPE, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotation
public @interface MaskLogs {
    String value() default "******";

    String defaultValue() default "******";
}


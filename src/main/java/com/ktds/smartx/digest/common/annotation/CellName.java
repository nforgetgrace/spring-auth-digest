package com.ktds.smartx.digest.common.annotation;

import java.lang.annotation.*;

/**
 * packageName    : com.ktds.smartx.digest.common.annotation
 * fileName       : CellName
 * author         : HONG IL SON
 * date           : 2023-12-11
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-12-11        HONG IL SON       최초 생성
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface CellName {

    String value();

    boolean mandatoryHeader() default false;

    boolean mandatoryCell() default false;
}

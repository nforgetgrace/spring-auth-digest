package com.ktds.smartx.digest.common.response.advice;

import com.ktds.smartx.digest.common.response.dto.ApiCommonResponse;
import com.ktds.smartx.digest.common.aop.custom.annotation.EnableCommonApiResponse;
import com.ktds.smartx.digest.common.response.CommonResponseNoUesList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.apache.commons.lang3.EnumUtils;

/**
 * packageName : com.ktds.smartx.digest.common.response.advice
 * fileName : ResponseAdvice
 * author : Jae Gook Jung
 * date : 2023/08/23
 * description :
 *
 * @RestControllerAdvice를 활용하여 Resp Body를 처리할 수 있다.
 *                        공통응답을 처리하기 위해 공통 body값을 만든다.
 *                        ResponseBodyAdvice를 사용하기 위해서 supports와
 *                        beforeBodyWrite을 오버라이딩
 *                        support에서 Controller작업이 끝난 response beforeBodyWrite로
 *                        보낼 것인지 판단
 *
 *                        supports로 판단해서 들어온 body를 활용 사용자가 원하는 작업을 수행
 *                        project name : digest
 *                        ===========================================================
 *                        DATE AUTHOR NOTE
 *                        -----------------------------------------------------------
 *                        2023/08/23 Jae Gook Jung 최초 생성
 */

@Slf4j
@RestControllerAdvice
@ConditionalOnBean(annotation = EnableCommonApiResponse.class)
public class CommonResponseAdvice implements ResponseBodyAdvice {

    // 공통응답을 사용할지 말지 결정 후 불리언값을 리턴 --> beforeBodyWrite 함수에서 공통 리스폰드 바디로 써지고 외부로 리턴
    @Override
    public boolean supports(final MethodParameter returnType, final Class converterType) {
        log.info("[ResponseAdvice] supports method");
        String methodName = returnType.getMethod().getName();

        // 공통 응답을 사용하지 않는 경우 ResponseBodyNoneUse에 정의

        // CommonResponseNoUesList 로 정의 된 Enum 값에 isValidEnum 함수를 돌려 해당 메소드가 있다면 사용하지 않는
        // 것으로 판단해서 False 반환
        if (EnumUtils.isValidEnum(CommonResponseNoUesList.class, methodName)) {

            if (CommonResponseNoUesList.valueOf(methodName).getClassName()
                    .equals(returnType.getDeclaringClass().getSimpleName())) { // 사용하지않는 클래스명 일치
                return false;
            }
        }
        return true;
    }

    @Override
    public Object beforeBodyWrite(final Object body, final MethodParameter returnType,
            final MediaType selectedContentType, final Class selectedConverterType, final ServerHttpRequest request,
            final ServerHttpResponse response) {
        log.info("[ResponseAdvice] beforeBodyWrite method");

        // Json 변환 추가
        ApiCommonResponse apiResponse = new ApiCommonResponse(body);
        if (response instanceof ServletServerHttpResponse) {
            ServletServerHttpResponse servletResponse = (ServletServerHttpResponse) response;
            servletResponse.getServletResponse().setHeader("Content-Type", "application/json;charset=UTF-8");
        }
        return apiResponse;
    }

}

package com.ktds.smartx.digest.common.aop;


import com.ktds.smartx.digest.common.utils.CustomObjectMapper;
import com.ktds.smartx.digest.common.utils.mdc.LogKey;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * packageName    : com.ktds.smartx.digest.common.aop
 * fileName       : ServiceLogsAspect
 * author         : Jae Gook Jung
 * date           : 2023/08/23
 * description    :
 * project name   : digest
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/08/23        Jae Gook Jung       최초 생성
 */

@RequiredArgsConstructor
@Slf4j
@Aspect
@Component
public class ServiceLogsAspect {
    /**/
    enum PRESENT {
        REQUEST, RESPONSE
    }

    private final CustomObjectMapper objectMapper;

    @Pointcut("within(com.ktds.smartx.digest.api.*..*ServiceImpl)") // service 발생 오류 전체
    private void serviceLogsAspect() {
    }

    /* 어노테이션 정의 하지 않음 주석 풀면 Bean 에러남
    @Pointcut("@annotation(com.gooks.black-label.api.servlet.aop.custom.annotation.ServiceLogs)") // ServiceLogs 어노테이션 사용 클래스,메소드
    private void serviceLogs() {
    }
    */
    @Pointcut("within(com.ktds.smartx.digest.api.*..*Controller)") // controller debug 로그
    private void controllerLogsAspect() {
    }

    @Around("serviceLogsAspect()")
    private Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object proceed = null;
        try {

            proceed = joinPoint.proceed();

        } catch(Exception e) {
            if(log.isDebugEnabled()) {
                log.debug("서비스 에러 발생 - {}", e.getMessage());
            }

            throw e;
        }
        return proceed;
    }

    @Around("controllerLogsAspect()")
    private Object aroundController(ProceedingJoinPoint joinPoint) throws Throwable {
        Object proceed = null;

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Class<? extends Object> clazz = joinPoint.getTarget().getClass();
        String className = (String) clazz.getSimpleName();
        String uri = request.getRequestURL().toString();
        Method method = ((MethodSignature)joinPoint.getSignature()).getMethod();
        //ApiName.put(joinPoint.getSignature().getName() + "_API(" + (!Objects.isNull(method) && !Objects.isNull(method.getAnnotation(Description.class)) ? method.getAnnotation(Description.class).value() : "API 정보가 null 입니다. Description 작성 요망") + "), REFERER : " + request.getHeader("REFERER"));
        try {
            // 메소드 시작
            fieldLoggerForController(joinPoint, className, uri, request, PRESENT.REQUEST, null);
            proceed = joinPoint.proceed();
            fieldLoggerForController(joinPoint, className, uri, request, PRESENT.RESPONSE, proceed);
        } catch(Exception e) {
            if(log.isDebugEnabled()) {
                log.debug("API 에러 발생 - {}", e.getMessage());
            }
            throw e;
        }
        return proceed;
    }

    private void fieldLoggerForController(JoinPoint joinPoint, String className, String uri, HttpServletRequest request, PRESENT present, Object proceed){
        Class clazz = joinPoint.getTarget().getClass();
        if("encryption".equals(joinPoint.getSignature().getName()) ||
                "decryption".equals(joinPoint.getSignature().getName()) ||
                "fare".equals(joinPoint.getSignature().getName())) {
            log.info("***** [LOGKEY {}] ***** [CLIENT {}[{}] ***** [URL : {}, {}] *****", LogKey.get() ,present, getRequestMethod(joinPoint), uri, present);
        } else {
            log.info("***** [LOGKEY {}] ***** [CLIENT {}[{}] ***** [URL : {}, {} : {}] *****", LogKey.get() ,present, getRequestMethod(joinPoint), uri, present, PRESENT.REQUEST == present ? makeParameterMap(joinPoint) : objectMapper.writeValueAsString(proceed));
        }
    }

    private String getRequestMethod(JoinPoint joinPoint) {
        if(Objects.isNull(joinPoint)) {
            return null;
        }

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        //mappingClass.getSimpleName().replace("Mapping") Stream에 선언된 @Annotaion 이름을 Mapping 을 제거한 후 메소드 방법을 찾는 ㅋㅋㅋㅋㅋ이런이런....
        //다른 방법도 있을거임.
        String httpMethod = Stream.of(GetMapping.class, PutMapping.class, PostMapping.class, PatchMapping.class, DeleteMapping.class, RequestMapping.class)
                .filter(mappingClass -> method.isAnnotationPresent(mappingClass)).map(mappingClass -> (mappingClass.getSimpleName().replace("Mapping", "")).toUpperCase())
                .findFirst().orElse(null);

        return httpMethod;
    }

    /**/
    private Map<String, Object> makeParameterMap(JoinPoint joinPoint) {
        Map<String, Object> params = new HashMap<>();
        // joinPoint 가 null 이라면 리턴 빠이
        if(Objects.isNull(joinPoint)) {
            return params;
        }
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
        String[] parameterNames = codeSignature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < parameterNames.length; i++) {
            // string 인 경우 / 파라미터 자체가 multipart인경우 / 파라미터 리스트 중 multipart가 있는 경우 / 파라미터 객체 내에 multipart 가 있는 경우
            if (args[i] instanceof String || isControllingFile(args[i])) {
                params.put(parameterNames[i], args[i]);
            } else {
                params.put(parameterNames[i], "POST".equals(getRequestMethod(joinPoint)) ? objectMapper.readValue(objectMapper.writeValueAsMaskString(args[i]), Map.class) : objectMapper.writeValueAsMaskString(args[i]));
            }
        }
        return params;
    }

    // multipart 파일 객체를 다루는 요청이 있는 경우 로그에 영향이 없도록 처리한다.
    private boolean isControllingFile(Object parameter) {
        if(Objects.isNull(parameter)) {
            return false;
        }

        if(parameter.getClass().getCanonicalName().contains("MultipartFile")) {
            return true;
        }

        //파라미터가 멀티파트라서 arrayList 로 넘어오는 경우에 대한 처리.
        if(parameter.getClass().getCanonicalName().contains("java.util.ArrayList") || parameter.getClass().getCanonicalName().contains("java.util.List")) {
            for(Object listParameter : (List<?>)parameter) {
                // 리스트 순회하면서 멀티파트라는것이 확인되면 내용확인을 위한 converting 하지 않는다.
                if(listParameter.getClass().getCanonicalName().contains("MultipartFile")) {
                    return true;
                }
            }
        }

        // 객체 내부에 멀티파트가 있는 경우 필드를 순회해서 리스트로 받은멀티파트를 찾는다. 찾는다면 converting 하지 않는다.
        for(Field parameterField : parameter.getClass().getDeclaredFields()) {
            parameterField.setAccessible(true);
            Class<?> parameterFieldClass = null;
            if(parameterField.getGenericType().toString().contains("java.util.List")) {
                parameterFieldClass = (Class<?>) ((ParameterizedType) parameterField.getGenericType()).getActualTypeArguments()[0];
            } else {
                parameterFieldClass = parameterField.getType();
            }

            if(parameterFieldClass.getName().contains("MultipartFile")) {
                return true;
            }
        }

        return false;
    }
}

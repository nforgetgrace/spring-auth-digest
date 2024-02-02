package com.ktds.smartx.digest.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktds.smartx.digest.common.exception.messege.CommonErrorCode;
import com.ktds.smartx.digest.common.utils.mdc.LogKey;
import com.ktds.smartx.digest.common.exception.messege.ErrorCode;
import lombok.Getter;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.List;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * packageName    : com.ktds.smartx.digest.common.exception
 * fileName       : GlobalExceptionHandler
 * author         : Jae Gook Jung
 * date           : 2023/08/23
 * description    : 전역 에러처리 핸들러 @RestControllerAdvice = Controller 전역에서 발생하는 익셉션을 처리하고 응답처리
 * project name   : digest
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/08/23        Jae Gook Jung       최초 생성
 */


@Slf4j(topic = "GLOBAL_EXCEPTION_HANDLER")
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 어플리케이션 표준 에러 처리
     *
     * @see ApiException
     */
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleException(ApiException e) {
        return buildResponse(e, e.getErrorCode(), e.getDetail(), e.getMessage());
    }

    /**
     * 핸들링 되지 않은 에러 발생 대비 여기로 호출된 에러는 메서드 추가하여 핸들링하도록 해야 한다.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        return buildResponse(e, CommonErrorCode.UNHANDLED_ERROR, e.getMessage());
    }


    /**
     * request body 필드 검증 에러 처리 (MethodValidationInterceptor 에서 발생)
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleException(ConstraintViolationException e) {
        List<String> detail = e.getConstraintViolations().stream().map(cv -> {
            ConstraintViolation<?> violation = cv.unwrap(ConstraintViolation.class);
            return makeConstraintMessage(violation);
        }).collect(Collectors.toList());

        return buildResponse(e, CommonErrorCode.INVALID_REQUEST_PARAM, detail);
    }

    /**
     * request body 필드 검증 에러 처리 (SpringValidatorAdapter 에서 발생)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException e) {
        List<String> detail;

        if (e.getBindingResult().getFieldErrors() == null) {
            detail = Arrays.asList(e.getMessage());
        } else {
            detail = e.getFieldErrors().stream().map(fe -> {
                ConstraintViolation<?> violation = fe.unwrap(ConstraintViolation.class);
                return makeConstraintMessage(violation);
            }).collect(Collectors.toList());
        }
        return buildResponse(e, CommonErrorCode.INVALID_REQUEST_PARAM, detail);
    }

    /**
     * request param 검증 에러 처리
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleException(BindException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();

        List<String> detail = fieldErrors.stream().map(fe -> {
            if (fe.contains(ConstraintViolation.class)) {
                ConstraintViolation<?> violation = fe.unwrap(ConstraintViolation.class);
                return makeConstraintMessage(violation);
            } else {
                return fe.getDefaultMessage();
            }
        }).collect(Collectors.toList());

        if (detail.isEmpty()) {
            return buildResponse(e, CommonErrorCode.INVALID_REQUEST_PARAM, e.getMessage());
        }
        return buildResponse(e, CommonErrorCode.INVALID_REQUEST_PARAM, detail);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleException(HttpMessageNotReadableException e) {
        return buildResponse(e, CommonErrorCode.INVALID_REQUEST_PARAM, e.getMessage());
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<ErrorResponse> handleException(HttpServerErrorException e) {
        return buildResponse(e, CommonErrorCode.CONNECTION_RESET, e.getMessage());
    }

    /* 서블릿 파라미터 요청값이 없거나 이름이 다른 경우 */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleException(MissingServletRequestParameterException e) {
        String message = String.format("[%s] 필수값입니다.", e.getParameterName());
        return buildResponse(e, CommonErrorCode.INVALID_REQUEST_PARAM, message);
    }

    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleException(TypeMismatchException e) {
        return buildResponse(e, CommonErrorCode.INVALID_REQUEST_PARAM, e.getMessage());
    }

    /* 지원하지 않는 http method 인 경우 */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleException(HttpRequestMethodNotSupportedException e) {
        String requestMethod = e.getMethod();
        String supportedMethod = Optional.ofNullable(e.getSupportedHttpMethods()).map(Objects::toString).orElse("");
        String message = String.format("지원하지않는 요청 방식(%s). 지원가능%s", requestMethod, supportedMethod);

        return buildResponse(e, CommonErrorCode.INVALID_REQUEST, message);
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<ErrorResponse> handleException(ResourceAccessException e) {
        return buildResponse(e, CommonErrorCode.CONNECTION_RESET, e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleException(IllegalArgumentException e) {
        return buildResponse(e, CommonErrorCode.ILLEGAL_ARGUMENT, e.getMessage());
    }

    @ExceptionHandler(IndexOutOfBoundsException.class)
    public ResponseEntity<ErrorResponse> handleException(IndexOutOfBoundsException e) {
        return buildResponse(e, CommonErrorCode.INTERNAL_ERROR, e.getMessage());
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ErrorResponse> handleException(DuplicateKeyException e) {
        return buildResponse(e, CommonErrorCode.SQL_VIOLATION, CommonErrorCode.SQL_VIOLATION.getMessage());
    }

    /* 연동 요청으로부터 응답을 받지 못하거나 예외 발생했을 때 */
    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<ErrorResponse> handleException(RestClientException e) {
        return buildResponse(e, CommonErrorCode.CONNECTION_RESET, e.getMessage());
    }

    /*커스텀 익셉션 처리*/
    @ExceptionHandler(RefreshTokenException.class)
    public ResponseEntity<ErrorResponse> handleException(RefreshTokenException e) {
        return buildResponse(e, e.getErrorCode(), e.getDetail(), e.getMessage());
    }

    /**
     * ConstraintViolation 메시지 생성
     */
    private String makeConstraintMessage(ConstraintViolation<?> violation) {
        String message = violation.getMessage();
        String[] fieldNameWithObject = violation.getPropertyPath().toString().split("[.]");
        String fieldName = fieldNameWithObject[fieldNameWithObject.length - 1];
        return String.format("[%s] %s", fieldName, message);
    }

    private ResponseEntity<ErrorResponse> buildResponse(Throwable e, ErrorCode ec) {
        log.error("API_ERROR : {}:{}", ec.getHttpStatus(), e.getMessage(), e);

        return ResponseEntity.status(ec.getHttpStatus()).body(new ErrorResponse(ec));
    }

    private ResponseEntity<ErrorResponse> buildResponse(Throwable e, ErrorCode ec, String message) {
        return buildResponse(e, ec, Arrays.asList(message, e.getClass().getName()));
    }

    private ResponseEntity<ErrorResponse> buildResponse(Throwable e, ErrorCode ec, String message, String clientMessage) {
        return buildResponse(e, ec, Arrays.asList(message), clientMessage);
    }

    private ResponseEntity<ErrorResponse> buildResponse(Throwable e, ErrorCode ec, List<String> detail) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String uri = request.getRequestURI();

        ResponseEntity<ErrorResponse> responseEntity = ResponseEntity.status(ec.getHttpStatus()).body(new ErrorResponse(ec, detail));
        try {
            log.error("***** [LOGKEY {}] ***** [CLIENT RESPONSE[API_ERROR] ***** [URL : {}, REASON({}) : {}({}), RESPONSE : {}]", LogKey.get(), uri, ec.getHttpStatus(), ec.getMessage(),
                    e.getMessage(), objectMapper.writeValueAsString(responseEntity), e);
        } catch (JsonProcessingException e1) {
            log.error("***** [LOGKEY {}] ***** [CLIENT RESPONSE[API_ERROR] ***** [URL : {}, REASON({}) : {}({}), RESPONSE : {}]", LogKey.get() , uri, ec.getHttpStatus(), ec.getMessage(),
                    e.getMessage(), e1.getMessage(), e);
        }

        return responseEntity;
    }

    private ResponseEntity<ErrorResponse> buildResponse(Throwable e, ErrorCode ec, List<String> detail, String clientMessage) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String uri = request.getRequestURI();

        ResponseEntity<ErrorResponse> responseEntity = ResponseEntity.status(ec.getHttpStatus()).body(new ErrorResponse(ec, detail, clientMessage));
        try {
            log.error("***** [LOGKEY {}] ***** [CLIENT RESPONSE[API_ERROR] ***** [URL : {}, REASON({}) : {}({}), RESPONSE : {}]", LogKey.get(), uri, ec.getHttpStatus(), ec.getMessage(),
                    e.getMessage(), objectMapper.writeValueAsString(responseEntity), e);
        } catch (JsonProcessingException e1) {
            log.error("***** [LOGKEY {}] ***** [CLIENT RESPONSE[API_ERROR] ***** [URL : {}, REASON({}) : {}({}), RESPONSE : {}]", LogKey.get() , uri, ec.getHttpStatus(), ec.getMessage(),
                    e.getMessage(), e1.getMessage(), e);
        }
        return responseEntity;
    }


    /**
     * 에러 응답 표준 객체
     */
    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL) // Todo Null 값을 포함시킬지 논의 필요
    class ErrorResponse {
        private String code;
        private String message;
        //클라에서 사용할 메시지
        private String clientMessage;
        private List<String> detail;

        public ErrorResponse(ErrorCode ec) {
            this(ec, null);
        }

        public ErrorResponse(ErrorCode ec, List<String> detail) {
            this.code = "FAILED";
            this.message = ec.getMessage();
            this.detail = detail;
            //클라언트메시지는 클라이언트에서 사용할 필드인데... 점점점
            this.clientMessage = "해당 요청을 처리하는 중에 오류가 발생하였습니다."; 
        }

        public ErrorResponse(ErrorCode ec, List<String> detail, String clientMessage) {
            this.code = "FAILED";
            this.message = ec.getMessage();
            this.detail = detail;
            this.clientMessage = clientMessage;
        }
    }
}

package com.ktds.smartx.digest.common.filter;

import com.ktds.smartx.digest.common.utils.mdc.ApiName;
import com.ktds.smartx.digest.common.utils.mdc.Hello;
import com.ktds.smartx.digest.common.utils.mdc.LogKey;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


/**
 * packageName    : com.ktds.smartx.digest.common.filter
 * fileName       : LoggingFilter
 * author         : Jae Gook Jung
 * date           : 2023/08/23
 * description    : 로깅필터는 최종적으로
 * project name   : digest
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/08/23        Jae Gook Jung       최초 생성
 */

@Slf4j
@Component
public class LoggingFilter implements Filter {

    public static final String HEADER_ORIGIN = "Origin";
    public static final String HEADER_X_FORWARDED_FOR = "X-Forwarded-For";

    @Override
    public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        //chain.doFilter(req, res);


        // MDC 쓰레드로컬에 추가하고 싶으면 여기에 추가하고 반드시 filter에서 나갈때는
        // remove 필수 나중에 쓰레드 다른 요청이 사용할 경우 데이터 혼합을 방지하기 위함

        addXForwardedFor(request);
        addLogKey(request);
        addHello(request);
        addApiName(request);

        chain.doFilter(req, res);

        removeXForwardedFor();
        removeLogKey();
        removeHello();
        removeApiName();

    }

    private void addXForwardedFor(final HttpServletRequest request) {
        String ip = request.getHeader(HEADER_X_FORWARDED_FOR);
        if (!StringUtils.hasLength(ip)) {
            ip = "";
        }
        MDC.put(HEADER_X_FORWARDED_FOR, ip);
    }

    private void removeXForwardedFor() {
        MDC.remove(HEADER_X_FORWARDED_FOR);
    }

    private void addLogKey(final HttpServletRequest request) {
        LogKey.put(getLogKey(request));
    }

    private void addHello(final HttpServletRequest request) {
        Hello.put("hi nice to meet you");
    }

    private void addApiName(final HttpServletRequest request) {
        ApiName.put(getApiName(request));
    }

    private String getLogKey(final HttpServletRequest request) {
        String logKey = request.getHeader(LogKey.getLogKeyName());
        // 로그키가 없을 경우 생성해서 셋팅
        if (!StringUtils.hasLength(logKey)) {
            logKey = LogKey.createLogKey();
        }
        return logKey;
    }


    private String getApiName(final HttpServletRequest request) {
        String apiName = request.getHeader(ApiName.getApiName());
        if (!StringUtils.hasLength(apiName)) {
            apiName = "";
        }
        return apiName;
    }

    private void removeLogKey() {
        LogKey.remove();
    }
    private void removeHello() {
        Hello.remove();
    }
    private void removeApiName() {
        ApiName.remove();
    }


    private Map<String, Object> getHeaders(HttpServletRequest request) {
        Map<String, Object> headerMap = new HashMap<>();

        Enumeration<String> headerArray = request.getHeaderNames();
        while (headerArray.hasMoreElements()) {
            String headerName = headerArray.nextElement();
            headerMap.put(headerName, request.getHeader(headerName));
        }
        return headerMap;
    }

    private String getRequestBody(ContentCachingRequestWrapper request) {
        ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                try {
                    return new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
                } catch (UnsupportedEncodingException e) {
                    return " - ";
                }
            }
        }
        return " - ";
    }

    private String getResponseBody(final HttpServletResponse response) throws IOException {
        String payload = null;
        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (wrapper != null) {
            wrapper.setCharacterEncoding("UTF-8");
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                payload = new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
                wrapper.copyBodyToResponse();
            }
        }
        return null == payload ? " - " : payload;
    }
}


/*
* @Component
public class LoggingFilter implements Filter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        if (servletRequest instanceof HttpServletRequest && servletResponse instanceof HttpServletResponse) {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            HttpServletResponse response = (HttpServletResponse) servletResponse;

            HttpServletRequest requestToCache = new ContentCachingRequestWrapper(request);
            HttpServletResponse responseToCache = new ContentCachingResponseWrapper(response);

            chain.doFilter(requestToCache, responseToCache);

            logger.info("request header: {}", getHeaders(requestToCache));
            logger.info("request body: {}", getRequestBody((ContentCachingRequestWrapper) requestToCache));
            logger.info("response body: {}", getResponseBody(responseToCache));

        } else {
            chain.doFilter(servletRequest, servletResponse);
        }
    }

    private Map<String, Object> getHeaders(HttpServletRequest request) {
        Map<String, Object> headerMap = new HashMap<>();

        Enumeration<String> headerArray = request.getHeaderNames();
        while (headerArray.hasMoreElements()) {
            String headerName = headerArray.nextElement();
            headerMap.put(headerName, request.getHeader(headerName));
        }
        return headerMap;
    }

    private String getRequestBody(ContentCachingRequestWrapper request) {
        ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                try {
                    return new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
                } catch (UnsupportedEncodingException e) {
                    return " - ";
                }
            }
        }
        return " - ";
    }

    private String getResponseBody(final HttpServletResponse response) throws IOException {
        String payload = null;
        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (wrapper != null) {
            wrapper.setCharacterEncoding("UTF-8");
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                payload = new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
                wrapper.copyBodyToResponse();
            }
        }
        return null == payload ? " - " : payload;
    }
}


/*
2022-03-01 01:37:04.049  INFO 16344 --- [nio-8080-exec-6] com.example.api.filter.LoggingFilter     : request header: {content-length=51, postman-token=d4e2bfa4-1...
2022-03-01 01:37:04.050  INFO 16344 --- [nio-8080-exec-6] com.example.api.filter.LoggingFilter     : request body: {
    "param1": "test1",
    "param2": "test2"
}
2022-03-01 01:37:04.051  INFO 16344 --- [nio-8080-exec-6] com.example.api.filter.LoggingFilter     : response body: {"res1":"test1","res2":"test2","res3":"test3"}
*/

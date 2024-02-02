package com.ktds.smartx.digest.api.common.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

/**
 * packageName    : com.ktds.smartx.digest.api.common.utils
 * fileName       : JwtHeaderHelper
 * author         : Jae Gook Jung
 * date           : 2023/08/22
 * description    : aop 로 jwt 토큰을 헤더에 설정 후 HttpHeaders 타입으로 반환한다.
 * project name   : digest
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/08/22        Jae Gook Jung       최초 생성
 */
@RequiredArgsConstructor
@Service
public class JwtHeaderHelper {
    
    public HttpHeaders header(HttpHeaders header) {

        if (header == null) {
            header = new HttpHeaders();
        }
        header.setContentType(MediaType.APPLICATION_JSON);

        return header;
    }

    public HttpHeaders header() {
        HttpHeaders httpHeaders = new HttpHeaders();
        return header(httpHeaders);
    }

    public HttpEntity<String> entity() {
        return this.entity(null);
    }


    public HttpEntity<String> entityFromHeader(HttpHeaders httpHeaders) {
        return this.entityFromHeader(null, httpHeaders);
    }


    public HttpEntity<String> entity(String body) {
        return this.entity(body, true, null);
    }


    public HttpEntity<String> entityFromHeader(String body, HttpHeaders httpHeaders) {
        return this.entity(body, true, httpHeaders);
    }


    public HttpEntity<String> entity(boolean isToken) {
        return this.entity(null, isToken, null);
    }


    public HttpEntity<String> entityFromHeader(boolean isToken, HttpHeaders httpHeaders) {
        return this.entity(null, isToken, httpHeaders);
    }


    public HttpEntity<String> entity(String body, boolean isToken) {
        return this.entity(body, isToken, null);
    }


    public HttpEntity<String> entity(String body, boolean isToken, HttpHeaders httpHeaders) {
        if(isToken) {
            httpHeaders = header(httpHeaders);
        } else {
            if(httpHeaders == null) {
                httpHeaders = new HttpHeaders();
            }
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        }

        HttpEntity<String> request = null;

        if(body != null) {
            request = new HttpEntity<>(body, httpHeaders);
        } else {
            request = new HttpEntity<>(httpHeaders);
        }

        return request;
    }
}

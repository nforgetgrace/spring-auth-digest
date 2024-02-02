package com.ktds.smartx.digest.api.common.handler;

import com.ktds.smartx.digest.common.utils.CustomObjectMapper;
import com.ktds.smartx.digest.api.common.utils.JwtHeaderHelper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
* @package : com.gooks.blacklabel.api.common.handler
* @name : RestTempleteHandler.java
* @date : 2022-12-17 오후 3:59
* @author : Jung Jae gook
* @version : 1.0.0
* @modifyed :
* @description : RestTemlate 추상화 시킨 클래스 해당 클래스를 상속 받아 비지니스 로직 구현하면 됨.
* https://beanbroker.github.io/2019/07/28/Spring/restTemplate/ 참고함.
**/ 

@Component
@AllArgsConstructor
@Slf4j(topic = "API_DATA_EXCHANGE")
public class RestTemplateHandler {

    protected final RestTemplate restTemplate;
    protected final JwtHeaderHelper jwtHeaderHelper;
    protected final CustomObjectMapper objectMapper;


    /**
     * <pre>
     *  ApiHandler.get GET 호출 메소드
     * </pre>
     *
     * @param url
     * @param returnDto
     * @return returnDto
     * @설명 url 과 returnDto 클래스타입을 받아 request/response를 returnDto로 리턴한다.
     */
    public <D> D get(Object url, final Class<D> returnDto) {
        return this.exchange(url, returnDto, null, false, HttpMethod.GET);
    }

    /**
     * <pre>
     *  ApiHandler.get GET 호출 메소드
     * </pre>
     *
     * @param url
     * @param returnDto
     * @param isJwtToken
     * @return returnDto
     * @설명 url 과 returnDto 클래스타입, jwt token 필요여부를 받아 request/response를 returnDto로
     *     리턴한다.
     */
    public <D> D get(Object url, final Class<D> returnDto, boolean isJwtToken) {
        return this.exchange(url, returnDto, null, isJwtToken, HttpMethod.GET);
    }

    /**
     * <pre>
     *  ApiHandler.get GET 호출 메소드
     * </pre>
     *
     * @param url
     * @param returnDto
     * @param body
     * @param isJwtToken
     * @return returnDto
     * @설명 url 과 returnDto 클래스타입, request DTO object, jwt token 필요여부를 받아
     *     request/response를 returnDto로 리턴한다.
     */
    public <D> D get(Object url, final Class<D> returnDto, Object body, boolean isJwtToken) {
        return this.exchange(url, returnDto, body, isJwtToken, HttpMethod.GET);
    }

    /**
     * <pre>
     *  ApiHandler.getForEntity GET 호출 메소드
     * </pre>
     *
     * @param url
     * @return ResponseEntity<String>
     * @설명 url 을 받아 request/response entity를 리턴한다.
     */
    public ResponseEntity<String> getForEntity(Object url) {
        return this.exchangeForEntity(url, null, false, HttpMethod.GET);
    }

    /**
     * <pre>
     *  ApiHandler.getForEntity GET 호출 메소드
     * </pre>
     *
     * @param url
     * @param isJwtToken
     * @return ResponseEntity<String>
     * @설명 url jwt token 필요여부를 받아 request/response entity를 리턴한다.
     */
    public ResponseEntity<String> getForEntity(Object url, boolean isJwtToken) {
        return this.exchangeForEntity(url, null, isJwtToken, HttpMethod.GET);
    }

    /**
     * <pre>
     *  ApiHandler.getForEntity getForEntity 호출 메소드
     * </pre>
     *
     * @param url
     * @param body
     * @param isJwtToken
     * @return ResponseEntity<String>
     * @설명 url 과 request DTO object, jwt token 필요여부를 받아 request/response entity를
     *     리턴한다.
     */
    public ResponseEntity<String> getForEntity(Object url, Object body, boolean isJwtToken) {
        return this.exchangeForEntity(url, body, isJwtToken, HttpMethod.GET);
    }

    /**
     * <pre>
     *  ApiHandler.post POST 호출 메소드
     * </pre>
     *
     * @param url
     * @param returnDto
     * @return returnDto
     * @설명 url 과 DTO 클래스타입을 받아 request/response 한다.
     */
    public <D> D post(Object url, final Class<D> returnDto) {
        return this.exchange(url, returnDto, null, false, HttpMethod.POST);
    }

    /**
     * <pre>
     *  ApiHandler.post POST 호출 메소드
     * </pre>
     *
     * @param url
     * @param returnDto
     * @param isJwtToken
     * @return returnDto
     * @설명 url 과 DTO 클래스타입, jwt token 필요여부를 받아 request/response 한다.
     */
    public <D> D post(Object url, final Class<D> returnDto, boolean isJwtToken) {
        return this.exchange(url, returnDto, null, isJwtToken, HttpMethod.POST);
    }

    /**
     * <pre>
     *  ApiHandler.post POST 호출 메소드
     * </pre>
     *
     * @param url
     * @param returnDto
     * @param body
     * @param isJwtToken
     * @return returnDto
     * @설명 url 과 DTO 클래스타입, request DTO object, jwt token 필요여부를 받아 request/response
     *     한다.
     */
    public <D> D post(Object url, final Class<D> returnDto, Object body, boolean isJwtToken) {
        return this.exchange(url, returnDto, body, isJwtToken, HttpMethod.POST);
    }

    /**
     * <pre>
     *  ApiHandler.postForEntity POST 호출 메소드
     * </pre>
     *
     * @param url
     * @return ResponseEntity<String>
     * @설명 url 을 받아 request/response entity를 리턴한다.
     */
    public ResponseEntity<String> postForEntity(Object url) {
        return this.exchangeForEntity(url, null, false, HttpMethod.POST);
    }

    /**
     * <pre>
     *  ApiHandler.postForEntity POST 호출 메소드
     * </pre>
     *
     * @param url
     * @param isJwtToken
     * @return ResponseEntity<String>
     * @설명 url 과 jwt token 필요여부를 받아 request/response entity를 리턴한다.
     */
    public ResponseEntity<String> postForEntity(Object url, boolean isJwtToken) {
        return this.exchangeForEntity(url, null, isJwtToken, HttpMethod.POST);
    }

    /**
     * <pre>
     *  ApiHandler.postForEntity POST 호출 메소드
     * </pre>
     *
     * @param url
     * @param body
     * @param isJwtToken
     * @return ResponseEntity<String>
     * @설명 url 과 request DTO object, jwt token 필요여부를 받아 request/response entity를
     *     리턴한다.
     */
    public ResponseEntity<String> postForEntity(Object url, Object body, boolean isJwtToken) {
        return this.exchangeForEntity(url, body, isJwtToken, HttpMethod.POST);
    }

    /**
     * <pre>
     *  ApiHandler.put PUT 호출 메소드
     * </pre>
     *
     * @param url
     * @param returnDto
     * @return returnDto
     * @설명 url 과 DTO 클래스타입을 받아 request/response 한다.
     */
    public <D> D put(Object url, final Class<D> returnDto) {
        return this.exchange(url, returnDto, null, false, HttpMethod.PUT);
    }

    /**
     * <pre>
     *  ApiHandler.put PUT 호출 메소드
     * </pre>
     *
     * @param url
     * @param returnDto
     * @param isJwtToken
     * @return returnDto
     * @설명 url 과 DTO 클래스타입, jwt token 필요여부를 받아 request/response 한다.
     */
    public <D> D put(Object url, final Class<D> returnDto, boolean isJwtToken) {
        return this.exchange(url, returnDto, null, isJwtToken, HttpMethod.PUT);
    }

    /**
     * <pre>
     *  ApiHandler.put PUT 호출 메소드
     * </pre>
     *
     * @param url
     * @param returnDto
     * @param body
     * @param isJwtToken
     * @return returnDto
     * @설명 url 과 DTO 클래스타입, request DTO object, jwt token 필요여부를 받아 request/response
     *     한다.
     */
    public <D> D put(Object url, final Class<D> returnDto, Object body, boolean isJwtToken) {
        return this.exchange(url, returnDto, body, isJwtToken, HttpMethod.PUT);
    }

    /**
     * <pre>
     *  ApiHandler.putForEntity PUT 호출 메소드
     * </pre>
     *
     * @param url
     * @return ResponseEntity<String>
     * @설명 url 을 받아 request/response entity를 리턴한다.
     */
    public ResponseEntity<String> putForEntity(Object url) {
        return this.exchangeForEntity(url, null, false, HttpMethod.PUT);
    }

    /**
     * <pre>
     *  ApiHandler.putForEntity PUT 호출 메소드
     * </pre>
     *
     * @param url
     * @param isJwtToken
     * @return ResponseEntity<String>
     * @설명 url 과 jwt token 필요여부를 받아 request/response entity를 리턴한다.
     */
    public ResponseEntity<String> putForEntity(Object url, boolean isJwtToken) {
        return this.exchangeForEntity(url, null, isJwtToken, HttpMethod.PUT);
    }

    /**
     * <pre>
     *  ApiHandler.putForEntity PUT 호출 메소드
     * </pre>
     *
     * @param url
     * @param body
     * @param isJwtToken
     * @return ResponseEntity<String>
     * @설명 url 과 request DTO object, jwt token 필요여부를 받아 request/response entity를
     *     리턴한다.
     */
    public ResponseEntity<String> putForEntity(Object url, Object body, boolean isJwtToken) {
        return this.exchangeForEntity(url, body, isJwtToken, HttpMethod.PUT);
    }

    /**
     * <pre>
     *  ApiHandler.delete DELETE 호출 메소드
     * </pre>
     *
     * @param url
     * @param returnDto
     * @return returnDto
     * @설명 url 과 DTO 클래스타입을 받아 request/response 한다.
     */
    public <D> D delete(Object url, final Class<D> returnDto) {
        return this.exchange(url, returnDto, null, false, HttpMethod.DELETE);
    }

    /**
     * <pre>
     *  ApiHandler.delete DELETE 호출 메소드
     * </pre>
     *
     * @param url
     * @param returnDto
     * @param isJwtToken
     * @return returnDto
     * @설명 url 과 DTO 클래스타입, jwt token 필요여부를 받아 request/response 한다.
     */
    public <D> D delete(Object url, final Class<D> returnDto, boolean isJwtToken) {
        return this.exchange(url, returnDto, null, isJwtToken, HttpMethod.DELETE);
    }

    /**
     * <pre>
     *  ApiHandler.delete DELETE 호출 메소드
     * </pre>
     *
     * @param url
     * @param returnDto
     * @param body
     * @param isJwtToken
     * @return returnDto
     * @설명 url 과 DTO 클래스타입, request DTO object, jwt token 필요여부를 받아 request/response
     *     한다.
     */
    public <D> D delete(Object url, final Class<D> returnDto, Object body, boolean isJwtToken) {
        return this.exchange(url, returnDto, body, isJwtToken, HttpMethod.DELETE);
    }

    /**
     * <pre>
     *  ApiHandler.deleteForEntity DELETE 호출 메소드
     * </pre>
     *
     * @param url
     * @return ResponseEntity<String>
     * @설명 url 을 받아 request/response entity를 리턴한다.
     */
    public ResponseEntity<String> deleteForEntity(Object url) {
        return this.exchangeForEntity(url, null, false, HttpMethod.DELETE);
    }

    /**
     * <pre>
     *  ApiHandler.deleteForEntity DELETE 호출 메소드
     * </pre>
     *
     * @param url
     * @param isJwtToken
     * @return ResponseEntity<String>
     * @설명 url 과 jwt token 필요여부를 받아 request/response entity를 리턴한다.
     */
    public ResponseEntity<String> deleteForEntity(Object url, boolean isJwtToken) {
        return this.exchangeForEntity(url, null, isJwtToken, HttpMethod.DELETE);
    }

    /**
     * <pre>
     *  ApiHandler.deleteForEntity DELETE 호출 메소드
     * </pre>
     *
     * @param url
     * @param body
     * @param isJwtToken
     * @return ResponseEntity<String>
     * @설명 url 과 request DTO object, jwt token 필요여부를 받아 request/response entity를
     *     리턴한다.
     */
    public ResponseEntity<String> deleteForEntity(Object url, Object body, boolean isJwtToken) {
        return this.exchangeForEntity(url, body, isJwtToken, HttpMethod.DELETE);
    }

    /**
     * <pre>
     * ApiHandler.exchange
     * </pre>
     *
     * @param url
     * @param returnDto
     * @param body
     * @param isJwtToken
     * @param httpMethod
     * @return returnDto
     * @설명 호출 url, 응답으로 사용할 DTO class, request DTO object, jwt token 필요여부, http
     *     method 타입을 입력받아 request/response 한다.
     */
    public <D> D exchange(Object url, final Class<D> returnDto, final Object body, boolean isJwtToken, final HttpMethod httpMethod) {
        String responseExchange = this.exchangeForEntity(url, body, isJwtToken, httpMethod).getBody();

        return this.objectMapper.readValue(responseExchange, returnDto);
    }

    /**
     * <pre>
     * ApiHandler.exchange
     * </pre>
     *
     * @param url
     * @param returnDto
     * @param body
     * @param isJwtToken
     * @param httpMethod
     * @param httpHeaders
     * @return returnDto
     * @설명 호출 url, 응답으로 사용할 DTO class, request DTO object, jwt token 필요여부, http
     *     method 타입, httpHeaders 을 입력받아 request/response 한다.
     */
    public <D> D exchange(Object url, final Class<D> returnDto, final Object body, boolean isJwtToken, final HttpMethod httpMethod, HttpHeaders httpHeaders) {
        String responseExchange = this.exchangeForEntity(url, body, isJwtToken, httpMethod, httpHeaders).getBody();

        return this.objectMapper.readValue(responseExchange, returnDto);
    }

    /**
     * <pre>
     * ApiHandler.exchangeForEntity
     * </pre>
     *
     * @param url
     * @param body
     * @param isJwtToken
     * @param httpMethod
     * @리턴타입 ResponseEntity<String>
     * @설명 호출 url, request DTO object, jwt token 필요여부, http method 타입을 입력받아
     *     ResponseEntity 를 응답한다.
     */
    public ResponseEntity<String> exchangeForEntity(Object url, final Object body, boolean isJwtToken, final HttpMethod httpMethod) {
        String bodyString = body != null ? this.objectMapper.writeValueAsString(body) : null;
        log.info(">>>>> API    REQUEST[{}]  >>>>> [REQUEST URL : {}, isJwtToken : {}, body : {}]", httpMethod, url, isJwtToken, bodyString);
        HttpEntity<String> httpEntity = this.jwtHeaderHelper.entity(bodyString, isJwtToken);

        ResponseEntity<String> response = null;
        if (url instanceof URI) {
            response = this.restTemplate.exchange((URI) url, httpMethod, httpEntity, String.class);
        } else if (url instanceof String) {
            response = this.restTemplate.exchange((String) url, httpMethod, httpEntity, String.class);
        } else {
            log.error("String, URI 형식으로만 호출 가능합니다. 전달받은 형식 : [[ {} ]]", url.getClass().getName());
        }

        log.info("<<<<< API    RESPONSE[{}] <<<<< [RESPONSE URL : {}, RESPONSE : {}]", httpMethod, url, response.getBody());

        /*
        ExchangeResult.remove();
        ExchangeMessage.remove();
        TargetUrlDescription.remove();
        */
        return response;
    }

    /**
     * <pre>
     * ApiHandler.exchangeForEntity
     * </pre>
     *
     * @param url
     * @param body
     * @param isJwtToken
     * @param httpMethod
     * @param httpHeaders
     * @리턴타입 ResponseEntity<String>
     * @설명 호출 url, request DTO object, jwt token 필요여부, http method 타입, httpHeaders 을
     *     입력받아 ResponseEntity 를 응답한다.
     */
    public ResponseEntity<String> exchangesForEntity(Object url, final Object body, boolean isJwtToken, final HttpMethod httpMethod, HttpHeaders httpHeaders) {
        String bodyString = body != null ? this.objectMapper.writeValueAsString(body) : null;
        log.info(">>>>> API    REQUEST[{}]  >>>>> [REQUEST URL : {}, isJwtToken : {}, body : {}]", httpMethod, url, isJwtToken, bodyString);
        HttpEntity<String> httpEntity = this.jwtHeaderHelper.entity(bodyString, isJwtToken, httpHeaders);

        ResponseEntity<String> response = null;
        if (url instanceof URI) {
            response = this.restTemplate.exchange((URI) url, httpMethod, httpEntity, String.class);
        } else if (url instanceof String) {
            response = this.restTemplate.exchange((String) url, httpMethod, httpEntity, String.class);
        } else {
            log.error("String, URI 형식으로만 호출 가능합니다. 전달받은 형식 : [[ {} ]]", url.getClass().getName());
        }

        log.info("<<<<< API    RESPONSE[{}] <<<<< [RESPONSE URL : {}, RESPONSE : {}]", httpMethod, url, response.getBody());
/*
        ExchangeResult.remove();
        ExchangeMessage.remove();
        TargetUrlDescription.remove();
*/
        return response;
    }

    /**
     * <pre>
     * ApiHandler.exchangeForEntity
     * </pre>
     *
     * @param url
     * @param body
     * @param isJwtToken
     * @param httpMethod
     * @param httpHeaders
     * @리턴타입 ResponseEntity<String>
     * @설명 호출 url, request DTO object, jwt token 필요여부, http method 타입, httpHeaders 을
     *     입력받아 ResponseEntity 를 응답한다.
     */
    public ResponseEntity<String> exchangeForEntity(Object url, final Object body, boolean isJwtToken, final HttpMethod httpMethod, HttpHeaders httpHeaders) {
        String bodyString = body != null ? this.objectMapper.writeValueAsString(body) : null;
        log.info(">>>>> API    REQUEST[{}]  >>>>> [REQUEST URL : {}, isJwtToken : {}, body : {}]", httpMethod, url, isJwtToken, bodyString);
        HttpEntity<String> httpEntity = this.jwtHeaderHelper.entity(bodyString, isJwtToken, httpHeaders);

        ResponseEntity<String> response = null;
        if (url instanceof URI) {
            response = this.restTemplate.exchange((URI) url, httpMethod, httpEntity, String.class);
        } else if (url instanceof String) {
            response = this.restTemplate.exchange((String) url, httpMethod, httpEntity, String.class);
        } else {
            log.error("String, URI 형식으로만 호출 가능합니다. 전달받은 형식 : [[ {} ]]", url.getClass().getName());
        }

        log.info("<<<<< API    RESPONSE[{}] <<<<< [RESPONSE URL : {}, RESPONSE : {}]", httpMethod, url, response.getBody());

        /*
        ExchangeResult.remove();
        ExchangeMessage.remove();
        TargetUrlDescription.remove();
        */
        return response;
    }

    /**
     * <pre>
     * ApiHandler.readValue
     * </pre>
     *
     * @param <D>
     * @param arg
     * @param returnDto
     * @리턴타입 D
     * @설명 objectMapper readValue wrapper
     */
    public <D> D readValue(String arg, Class<D> returnDto) {
        return this.objectMapper.readValue(arg, returnDto);
    }
}

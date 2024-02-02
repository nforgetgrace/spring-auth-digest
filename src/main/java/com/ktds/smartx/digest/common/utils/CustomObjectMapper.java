package com.ktds.smartx.digest.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;

import com.ktds.smartx.digest.common.aop.custom.MaskLogsAnnotationIntrospector;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;


/**
 * packageName    : com.ktds.smartx.digest.common.utils
 * fileName       : Hello
 * author         : Jae Gook Jung
 * date           : 2023/08/23
 * description    : 오브젝트 매퍼 커드터마이징
 * project name   : digest
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/08/23        Jae Gook Jung       최초 생성
 */

@RequiredArgsConstructor
@Component
public class CustomObjectMapper {

    private final String CONVERTING_ERR_MSG = "failed to convert";
    private final ObjectMapper mapper;

    public String writeValueAsString(InputStream is) {
        Object value = readValue(is, Object.class);
        if (value == null) return null;
        return writeValueAsString(value);
    }

    public String writeValueAsString(Object o) {
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(CONVERTING_ERR_MSG, e);
        }
    }

    public <T> T readValue(String content, Class<T> valueType) {
        try {
            mapper.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);
            return mapper.readValue(content, valueType);
        } catch (IOException e) {
            throw new RuntimeException(CONVERTING_ERR_MSG, e);
        }
    }

    public <T> T readValue(String content, TypeReference<T> valueType) {
        try {
            return mapper.readValue(content, valueType);
        } catch (IOException e) {
            throw new RuntimeException("failed to convert", e);
        }
    }

    private <T> T readValue(InputStream src, Class<T> valueType) {
        try {
            return mapper.readValue(src, valueType);
        } catch (JsonProcessingException pe) {
            /**
             * json 형태가 아닌 경우 에러 발생시키지 않는다.
             */
            return null;
        } catch (IOException e) {
            throw new RuntimeException(CONVERTING_ERR_MSG, e);
        }
    }

    public String writeValueAsPrettyString(Object o) {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(CONVERTING_ERR_MSG, e);
        }
    }

    /**
     * <pre> gooksObjectMapper.writeValueAsMaskString </pre>
     * @param object
     * @리턴타입 String
     * @설명 client req/res 로깅에 사용함, objectMapper 객체가 분리되어야 해서(instrospector 적용) 생성자 호출처리했음.
    **/
    public String writeValueAsMaskString(Object object) {
        ObjectMapper loggingMapper = new ObjectMapper();
        AnnotationIntrospector introspector = loggingMapper.getSerializationConfig().getAnnotationIntrospector();
        AnnotationIntrospector pairIntrospector = AnnotationIntrospectorPair.pair(introspector, new MaskLogsAnnotationIntrospector());

        try {
            return loggingMapper.setAnnotationIntrospector(pairIntrospector).writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(CONVERTING_ERR_MSG, e);
        }
    }

    public MultiValueMap<String, String> toMultiValueMap(final Object o) {
        @SuppressWarnings("unchecked")
        Map<String, String> map = (Map<String, String>) mapper.convertValue(o, Map.class);
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<String, String>();
        multiValueMap.setAll(map);
        return multiValueMap;
    }

    public <R> R convert(Object t, Class<R> clazz) {
        return mapper.convertValue(t, clazz);
    }
}

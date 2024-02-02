package com.ktds.smartx.digest.common.aop.custom;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.ktds.smartx.digest.common.aop.custom.annotation.MaskLogs;

import java.io.IOException;

/**
 * packageName    : com.ktds.smartx.digest.common.aop.custom
 * fileName       : MaskLogsSerializer
 * author         : Jae Gook Jung
 * date           : 2023/08/23
 * description    :
 * project name   : digest
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/08/23        Jae Gook Jung       최초 생성
 */
public class MaskLogsSerializer extends StdSerializer<String> implements ContextualSerializer {

    private static final long serialVersionUID = 1L;

    private String maskValue;

    public MaskLogsSerializer() {
        super(String.class);
    }

    public MaskLogsSerializer(String maskValue) {
        super(String.class);
        this.maskValue = maskValue;
    }

    @Override
    public void serialize(String value, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
        jsonGenerator.writeString(maskValue);
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        String maskValue = null;
        MaskLogs maskLogs = null;
        if (beanProperty != null) {
            maskLogs = beanProperty.getAnnotation(MaskLogs.class);
        }
        if (maskLogs != null) {
            maskValue = maskLogs.value();
        }
        return new MaskLogsSerializer(maskValue);
    }
}

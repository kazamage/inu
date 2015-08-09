package jp.pgw.develop.swallow.inu.jackson.datatype.threetenbp.ser.key;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class ThreeTenNullKeySerializer extends JsonSerializer<Object> {

    public static final String NULL_KEY = "";

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException,
            JsonProcessingException {
        if (value != null) {
            throw new JsonMappingException("ThreeTenNullKeySerializer is only for serializing null values.");
        }
        gen.writeFieldName(NULL_KEY);
    }

}

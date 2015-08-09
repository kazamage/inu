package jp.pgw.develop.swallow.inu.jackson.datatype.threetenbp.deser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.threeten.bp.DateTimeException;

import java.io.IOException;

abstract class ThreeTenDeserializerBase<T> extends StdScalarDeserializer<T> {

    private static final long serialVersionUID = 1L;
    
    protected ThreeTenDeserializerBase(Class<T> supportedType) {
        super(supportedType);
    }

    @Override
    public Object deserializeWithType(JsonParser parser, DeserializationContext context, TypeDeserializer deserializer)
            throws IOException {
        return deserializer.deserializeTypedFromAny(parser, context);
    }

}

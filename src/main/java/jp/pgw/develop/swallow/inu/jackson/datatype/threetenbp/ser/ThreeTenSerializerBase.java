package jp.pgw.develop.swallow.inu.jackson.datatype.threetenbp.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

abstract class ThreeTenSerializerBase<T> extends StdSerializer<T> {

    private static final long serialVersionUID = 1L;

    protected ThreeTenSerializerBase(Class<?> supportedType) {
        super(supportedType, false);
    }

    @Override
    public void serializeWithType(T value, JsonGenerator generator, SerializerProvider provider,
                                  TypeSerializer serializer) throws IOException {
        serializer.writeTypePrefixForScalar(value, generator);
        this.serialize(value, generator, provider);
        serializer.writeTypeSuffixForScalar(value, generator);
    }
}

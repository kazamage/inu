package jp.pgw.develop.swallow.inu.jackson.datatype.threetenbp.deser.key;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import jp.pgw.develop.swallow.inu.jackson.datatype.threetenbp.ser.key.ThreeTenNullKeySerializer;

abstract class ThreeTenKeyDeserializer extends KeyDeserializer {

    @Override
    public final Object deserializeKey(String key, DeserializationContext ctxt) {
        if (ThreeTenNullKeySerializer.NULL_KEY.equals(key)) {
            return null;
        }
        return deserialize(key, ctxt);
    }

    protected abstract Object deserialize(String key, DeserializationContext ctxt);

}
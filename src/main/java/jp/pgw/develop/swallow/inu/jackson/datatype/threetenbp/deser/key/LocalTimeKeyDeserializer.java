package jp.pgw.develop.swallow.inu.jackson.datatype.threetenbp.deser.key;

import com.fasterxml.jackson.databind.DeserializationContext;
import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;

public class LocalTimeKeyDeserializer extends ThreeTenKeyDeserializer {

    public static final LocalTimeKeyDeserializer INSTANCE = new LocalTimeKeyDeserializer();

    private final DateTimeFormatter formatter;

    private LocalTimeKeyDeserializer() {
        this(DateTimeFormatter.ISO_LOCAL_TIME);
    }

    public LocalTimeKeyDeserializer(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    protected LocalTime deserialize(String key, DeserializationContext ctxt) {
        return LocalTime.parse(key, formatter);
    }

}

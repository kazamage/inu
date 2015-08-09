package jp.pgw.develop.swallow.inu.jackson.datatype.threetenbp.deser.key;

import com.fasterxml.jackson.databind.DeserializationContext;
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

public class LocalDateKeyDeserializer extends ThreeTenKeyDeserializer {

    public static final LocalDateKeyDeserializer INSTANCE = new LocalDateKeyDeserializer();

    private final DateTimeFormatter formatter;

    private LocalDateKeyDeserializer() {
        this(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    private LocalDateKeyDeserializer(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    protected LocalDate deserialize(String key, DeserializationContext ctxt) {
        return LocalDate.parse(key, formatter);
    }

}

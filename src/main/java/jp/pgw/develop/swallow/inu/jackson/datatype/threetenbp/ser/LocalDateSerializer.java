package jp.pgw.develop.swallow.inu.jackson.datatype.threetenbp.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.io.IOException;

public class LocalDateSerializer extends ThreeTenFormattedSerializerBase<LocalDate> {

    private static final long serialVersionUID = 1L;

    public static final LocalDateSerializer INSTANCE = new LocalDateSerializer();

    protected LocalDateSerializer() {
        super(LocalDate.class);
    }

    protected LocalDateSerializer(LocalDateSerializer base,
            Boolean useTimestamp, DateTimeFormatter dtf) {
        super(base, useTimestamp, dtf);
    }

    public LocalDateSerializer(DateTimeFormatter formatter) {
        super(LocalDate.class, formatter);
    }

    @Override
    protected LocalDateSerializer withFormat(Boolean useTimestamp, DateTimeFormatter dtf) {
        return new LocalDateSerializer(this, useTimestamp, dtf);
    }

    @Override
    public void serialize(LocalDate date, JsonGenerator generator, SerializerProvider provider) throws IOException {
        if (useTimestamp(provider)) {
            generator.writeStartArray();
            generator.writeNumber(date.getYear());
            generator.writeNumber(date.getMonthValue());
            generator.writeNumber(date.getDayOfMonth());
            generator.writeEndArray();
        } else {
            String str = (_formatter == null) ? date.toString() : date.format(_formatter);
            generator.writeString(str);
        }
    }

}

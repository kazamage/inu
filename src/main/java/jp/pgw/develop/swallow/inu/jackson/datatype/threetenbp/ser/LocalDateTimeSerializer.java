package jp.pgw.develop.swallow.inu.jackson.datatype.threetenbp.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.ChronoField;

import java.io.IOException;

public class LocalDateTimeSerializer extends ThreeTenFormattedSerializerBase<LocalDateTime> {

    private static final long serialVersionUID = 1L;

    public static final LocalDateTimeSerializer INSTANCE = new LocalDateTimeSerializer();

    protected LocalDateTimeSerializer() {
        this(null);
    }

    public LocalDateTimeSerializer(DateTimeFormatter formatter) {
        super(LocalDateTime.class, formatter);
    }

    private LocalDateTimeSerializer(LocalDateTimeSerializer base, Boolean useTimestamp, DateTimeFormatter formatter) {
        super(base, useTimestamp, formatter);
    }

    @Override
    protected ThreeTenFormattedSerializerBase<LocalDateTime> withFormat(Boolean useTimestamp, DateTimeFormatter formatter) {
        return new LocalDateTimeSerializer(this, useTimestamp, formatter);
    }

    @Override
    public void serialize(LocalDateTime dateTime, JsonGenerator generator, SerializerProvider provider)
            throws IOException {
        if (useTimestamp(provider)) {
            generator.writeStartArray();
            generator.writeNumber(dateTime.getYear());
            generator.writeNumber(dateTime.getMonthValue());
            generator.writeNumber(dateTime.getDayOfMonth());
            generator.writeNumber(dateTime.getHour());
            generator.writeNumber(dateTime.getMinute());
            if(dateTime.getSecond() > 0 || dateTime.getNano() > 0)
            {
                generator.writeNumber(dateTime.getSecond());
                if(dateTime.getNano() > 0)
                {
                    if(provider.isEnabled(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS))
                        generator.writeNumber(dateTime.getNano());
                    else
                        generator.writeNumber(dateTime.get(ChronoField.MILLI_OF_SECOND));
                }
            }
            generator.writeEndArray();
        } else {
            String str = (_formatter == null) ? dateTime.toString() : dateTime.format(_formatter);
            generator.writeString(str);
        }
    }

}

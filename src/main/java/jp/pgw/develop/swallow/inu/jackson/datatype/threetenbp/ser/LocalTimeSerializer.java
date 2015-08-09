package jp.pgw.develop.swallow.inu.jackson.datatype.threetenbp.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.ChronoField;

import java.io.IOException;

public class LocalTimeSerializer extends ThreeTenFormattedSerializerBase<LocalTime> {

    private static final long serialVersionUID = 1L;

    public static final LocalTimeSerializer INSTANCE = new LocalTimeSerializer();

    protected LocalTimeSerializer() {
        this(null);
    }

    public LocalTimeSerializer(DateTimeFormatter formatter) {
        super(LocalTime.class, formatter);
    }

    protected LocalTimeSerializer(LocalTimeSerializer base, Boolean useTimestamp, DateTimeFormatter formatter) {
        super(base, useTimestamp, formatter);
    }

    @Override
    protected ThreeTenFormattedSerializerBase<LocalTime> withFormat(Boolean useTimestamp, DateTimeFormatter dtf) {
        return new LocalTimeSerializer(this, useTimestamp, dtf);
    }

    @Override
    public void serialize(LocalTime time, JsonGenerator generator, SerializerProvider provider) throws IOException {
        if (useTimestamp(provider)) {
            generator.writeStartArray();
            generator.writeNumber(time.getHour());
            generator.writeNumber(time.getMinute());
            if(time.getSecond() > 0 || time.getNano() > 0)
            {
                generator.writeNumber(time.getSecond());
                if(time.getNano() > 0)
                {
                    if(provider.isEnabled(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS))
                        generator.writeNumber(time.getNano());
                    else
                        generator.writeNumber(time.get(ChronoField.MILLI_OF_SECOND));
                }
            }
            generator.writeEndArray();
        } else {
            String str = (_formatter == null) ? time.toString() : time.format(_formatter);
            generator.writeString(str);
        }
    }

}

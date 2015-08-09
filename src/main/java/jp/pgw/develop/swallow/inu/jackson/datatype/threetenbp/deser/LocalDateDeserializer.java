package jp.pgw.develop.swallow.inu.jackson.datatype.threetenbp.deser;

import java.io.IOException;

import com.fasterxml.jackson.core.*;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

public class LocalDateDeserializer extends ThreeTenDateTimeDeserializerBase<LocalDate> {

    private static final long serialVersionUID = 1L;

    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    
    public static final LocalDateDeserializer INSTANCE = new LocalDateDeserializer();

    private LocalDateDeserializer() {
        this(DEFAULT_FORMATTER);
    }

    public LocalDateDeserializer(DateTimeFormatter dtf) {
        super(LocalDate.class, dtf);
    }

    @Override
    protected JsonDeserializer<LocalDate> withDateFormat(DateTimeFormatter dtf) {
        return new LocalDateDeserializer(dtf);
    }
    
    @Override
    public LocalDate deserialize(JsonParser parser, DeserializationContext context) throws IOException {
    	if (!parser.hasToken(JsonToken.VALUE_STRING)) {
            throw context.wrongTokenException(parser, JsonToken.VALUE_STRING, "Expected string.");
        }
        final String string = parser.getText().trim();
        if (string.length() == 0) {
            return null;
        }
        return LocalDate.parse(string, formatter);
    }
}

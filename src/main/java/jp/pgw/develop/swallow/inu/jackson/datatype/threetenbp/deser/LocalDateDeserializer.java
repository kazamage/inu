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
    public LocalDate deserialize(JsonParser parser, DeserializationContext context) throws IOException
    {
    	if (parser.hasToken(JsonToken.VALUE_STRING)) {
            String string = parser.getText().trim();
            if (string.length() == 0) {
                return null;
            }
            // as per [datatype-jsr310#37], only check for optional (and, incorrect...) time marker 'T'
            // if we are using default formatter
            DateTimeFormatter format = _formatter;
            if (format == DEFAULT_FORMATTER) {
	            if (string.contains("T")) {
	                return LocalDate.parse(string, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
	            }
            }
            return LocalDate.parse(string, format);
    	}
    	if (parser.isExpectedStartArrayToken()) {
    		if (parser.nextToken() == JsonToken.END_ARRAY) {
    			return null;
    		}
            int year = parser.getIntValue();

            parser.nextToken();
            int month = parser.getIntValue();

            parser.nextToken();
            int day = parser.getIntValue();

            if (parser.nextToken() != JsonToken.END_ARRAY) {
                throw context.wrongTokenException(parser, JsonToken.END_ARRAY, "Expected array to end.");
            }
            return LocalDate.of(year, month, day);
        }

        throw context.wrongTokenException(parser, JsonToken.START_ARRAY, "Expected array or string.");
    }
}

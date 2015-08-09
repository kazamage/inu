package jp.pgw.develop.swallow.inu.jackson.datatype.threetenbp.deser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.io.IOException;

public class LocalDateTimeDeserializer extends ThreeTenDateTimeDeserializerBase<LocalDateTime> {

    private static final long serialVersionUID = 1L;

    public static final LocalDateTimeDeserializer INSTANCE = new LocalDateTimeDeserializer();

    private LocalDateTimeDeserializer() {
        this(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public LocalDateTimeDeserializer(DateTimeFormatter formatter) {
        super(LocalDateTime.class, formatter);
    }
    
    @Override
    protected JsonDeserializer<LocalDateTime> withDateFormat(DateTimeFormatter formatter) {
        return new LocalDateTimeDeserializer(formatter);
    }
    
    @Override
    public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
    	if (parser.hasTokenId(JsonTokenId.ID_STRING)) {
            String string = parser.getText().trim();
            if (string.length() == 0) {
                return null;
            }
            return LocalDateTime.parse(string, _formatter);
    	}
    	if (parser.isExpectedStartArrayToken()) {
    		if(parser.nextToken() == JsonToken.END_ARRAY) {
    			return null;
    		}
            int year = parser.getIntValue();

            parser.nextToken();
            int month = parser.getIntValue();

            parser.nextToken();
            int day = parser.getIntValue();

            parser.nextToken();
            int hour = parser.getIntValue();

            parser.nextToken();
            int minute = parser.getIntValue();

            if(parser.nextToken() != JsonToken.END_ARRAY) {
            	int second = parser.getIntValue();

            	if (parser.nextToken() != JsonToken.END_ARRAY) {
            		int partialSecond = parser.getIntValue();
            		if(partialSecond < 1_000 &&
            				!context.isEnabled(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS))
            			partialSecond *= 1_000_000; // value is milliseconds, convert it to nanoseconds

            		if(parser.nextToken() != JsonToken.END_ARRAY) {
            			throw context.wrongTokenException(parser, JsonToken.END_ARRAY, "Expected array to end.");
            		}
            		return LocalDateTime.of(year, month, day, hour, minute, second, partialSecond);
            	}
            	return LocalDateTime.of(year, month, day, hour, minute, second);
            }
            return LocalDateTime.of(year, month, day, hour, minute);
    	}
        throw context.wrongTokenException(parser, JsonToken.START_ARRAY, "Expected array or string.");
    }

}

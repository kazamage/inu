package jp.pgw.develop.swallow.inu.jackson.datatype.threetenbp.deser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.io.IOException;

public class LocalTimeDeserializer extends ThreeTenDateTimeDeserializerBase<LocalTime> {

    private static final long serialVersionUID = 1L;

    public static final LocalTimeDeserializer INSTANCE = new LocalTimeDeserializer();

    private LocalTimeDeserializer() {
        this(DateTimeFormatter.ISO_LOCAL_TIME);
    }

    public LocalTimeDeserializer(DateTimeFormatter formatter) {
        super(LocalTime.class, formatter);
    }

    @Override
    protected JsonDeserializer<LocalTime> withDateFormat(DateTimeFormatter formatter) {
        return new LocalTimeDeserializer(formatter);
    }
    
    @Override
    public LocalTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
    	if (!parser.hasToken(JsonToken.VALUE_STRING)) {
            throw context.wrongTokenException(parser, JsonToken.VALUE_STRING, "Expected string.");
        }
        final String string = parser.getText().trim();
        if(string.length() == 0) {
            return null;
        }
        return LocalTime.parse(string, formatter);
    }

}

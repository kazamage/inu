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
    	if (!parser.hasTokenId(JsonTokenId.ID_STRING)) {
            throw context.wrongTokenException(parser, JsonToken.VALUE_STRING, "Expected string.");
        }
        final String string = parser.getText().trim();
        if (string.length() == 0) {
            return null;
        }
        return LocalDateTime.parse(string, formatter);
    }

}

package jp.pgw.develop.swallow.inu.jackson.datatype.threetenbp.deser;

import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.introspect.Annotated;
import org.threeten.bp.format.DateTimeFormatter;

public abstract class ThreeTenDateTimeDeserializerBase<T>
    extends ThreeTenDeserializerBase<T>
    implements ContextualDeserializer {

    protected final DateTimeFormatter formatter;

    protected ThreeTenDateTimeDeserializerBase(Class<T> supportedType, DateTimeFormatter formatter) {
        super(supportedType);
        this.formatter = formatter;
    }

    protected abstract JsonDeserializer<T> withDateFormat(DateTimeFormatter formatter);

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt,
            BeanProperty property) throws JsonMappingException {
        if (property == null) {
            return this;
        }
        JsonFormat.Value format = ctxt.getAnnotationIntrospector().findFormat((Annotated) property.getMember());
        if (format == null) {
            return this;
        }
        if (format.hasPattern()) {
            final String pattern = format.getPattern();
            final Locale locale = format.hasLocale() ? format.getLocale() : ctxt.getLocale();
            final DateTimeFormatter dtf;
            if (locale == null) {
                dtf = DateTimeFormatter.ofPattern(pattern);
            } else {
                dtf = DateTimeFormatter.ofPattern(pattern, locale);
            }
            return withDateFormat(dtf);
        }
        // any use for TimeZone?
        return this;
   }

}

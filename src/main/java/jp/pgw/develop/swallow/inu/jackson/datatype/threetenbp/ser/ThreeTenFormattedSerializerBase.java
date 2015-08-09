package jp.pgw.develop.swallow.inu.jackson.datatype.threetenbp.ser;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormat;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import org.threeten.bp.format.DateTimeFormatter;

import java.lang.reflect.Type;
import java.util.Locale;

abstract class ThreeTenFormattedSerializerBase<T>
    extends ThreeTenSerializerBase<T>
    implements ContextualSerializer {

    private static final long serialVersionUID = 1L;

    /**
     * Flag that indicates that serialization must be done as the
     * Java timestamp, regardless of other settings.
     */
    protected final Boolean _useTimestamp;
    
    /**
     * Specific format to use, if not default format: non null value
     * also indicates that serialization is to be done as JSON String,
     * not numeric timestamp, unless {@link #_useTimestamp} is true.
     */
    protected final DateTimeFormatter _formatter;

    protected ThreeTenFormattedSerializerBase(Class<T> supportedType) {
        this(supportedType, null);
    }

    protected ThreeTenFormattedSerializerBase(Class<T> supportedType,
                                              DateTimeFormatter formatter) {
        super(supportedType);
        _useTimestamp = null;
        _formatter = formatter;
    }
    
    protected ThreeTenFormattedSerializerBase(ThreeTenFormattedSerializerBase<?> base,
                                              Boolean useTimestamp, DateTimeFormatter dtf) {
        super(base.handledType());
        _useTimestamp = useTimestamp;
        _formatter = dtf;
    }

    protected abstract ThreeTenFormattedSerializerBase<?> withFormat(Boolean useTimestamp,
            DateTimeFormatter dtf);

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov,
            BeanProperty property) throws JsonMappingException {
        if (property != null) {
            JsonFormat.Value format = prov.getAnnotationIntrospector().findFormat((Annotated)property.getMember());
            if (format != null) {
                Boolean useTimestamp = null;

               // Simple case first: serialize as numeric timestamp?
                JsonFormat.Shape shape = format.getShape();
                if (shape == JsonFormat.Shape.ARRAY || shape.isNumeric() ) {
                    useTimestamp = Boolean.TRUE;
                } else {
                    useTimestamp = (shape == JsonFormat.Shape.STRING) ? Boolean.FALSE : null;
                }
                DateTimeFormatter dtf = _formatter;

                // If not, do we have a pattern?
                if (format.hasPattern()) {
                    final String pattern = format.getPattern();
                    final Locale locale = format.hasLocale() ? format.getLocale() : prov.getLocale();
                    if (locale == null) {
                        dtf = DateTimeFormatter.ofPattern(pattern);
                    } else {
                        dtf = DateTimeFormatter.ofPattern(pattern, locale);
                    }
                }
                if (useTimestamp != _useTimestamp || dtf != _formatter) {
                    return withFormat(useTimestamp, dtf);
                }
            }
        }
        return this;
    }

    @Override
    public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
        return this.createSchemaNode(
                provider.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) ? "array" : "string", true
        );
    }

    protected boolean useTimestamp(SerializerProvider provider) {
        if (_useTimestamp != null) {
            return _useTimestamp.booleanValue();
        }
        // assume that explicit formatter definition implies use of textual format
        if (_formatter != null) { 
            return false;
        }
        return provider.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
        SerializerProvider provider = visitor.getProvider();
        boolean useTimestamp = (provider != null) && useTimestamp(provider);
        if (useTimestamp) {
            _acceptTimestampVisitor(visitor, typeHint);
        } else {
            JsonStringFormatVisitor v2 = visitor.expectStringFormat(typeHint);
            if (v2 != null) {
                v2.format(JsonValueFormat.DATE_TIME);
            }
        }
    }
    
    protected void _acceptTimestampVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
        // By default, most sub-types use JSON Array, so do this:
        JsonArrayFormatVisitor v2 = visitor.expectArrayFormat(typeHint);
        if (v2 != null) {
            v2.itemsFormat(JsonFormatTypes.INTEGER);
        }
    }
}

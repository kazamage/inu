package jp.pgw.develop.swallow.inu.jackson.datatype.threetenbp;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.ValueInstantiators;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.module.SimpleModule;
import jp.pgw.develop.swallow.inu.jackson.datatype.threetenbp.deser.LocalDateDeserializer;
import jp.pgw.develop.swallow.inu.jackson.datatype.threetenbp.deser.LocalDateTimeDeserializer;
import jp.pgw.develop.swallow.inu.jackson.datatype.threetenbp.deser.LocalTimeDeserializer;
import jp.pgw.develop.swallow.inu.jackson.datatype.threetenbp.deser.key.LocalDateKeyDeserializer;
import jp.pgw.develop.swallow.inu.jackson.datatype.threetenbp.deser.key.LocalDateTimeKeyDeserializer;
import jp.pgw.develop.swallow.inu.jackson.datatype.threetenbp.deser.key.LocalTimeKeyDeserializer;
import jp.pgw.develop.swallow.inu.jackson.datatype.threetenbp.ser.LocalDateSerializer;
import jp.pgw.develop.swallow.inu.jackson.datatype.threetenbp.ser.LocalDateTimeSerializer;
import jp.pgw.develop.swallow.inu.jackson.datatype.threetenbp.ser.LocalTimeSerializer;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZoneId;

public final class ThreeTenModule extends SimpleModule {

    private static final long serialVersionUID = 1L;

    public ThreeTenModule() {
        super(PackageVersion.VERSION);
        addDeserializer(LocalDateTime.class, LocalDateTimeDeserializer.INSTANCE);
        addDeserializer(LocalDate.class, LocalDateDeserializer.INSTANCE);
        addDeserializer(LocalTime.class, LocalTimeDeserializer.INSTANCE);
        addSerializer(LocalDateTime.class, LocalDateTimeSerializer.INSTANCE);
        addSerializer(LocalDate.class, LocalDateSerializer.INSTANCE);
        addSerializer(LocalTime.class, LocalTimeSerializer.INSTANCE);
        addKeyDeserializer(LocalDateTime.class, LocalDateTimeKeyDeserializer.INSTANCE);
        addKeyDeserializer(LocalDate.class, LocalDateKeyDeserializer.INSTANCE);
        addKeyDeserializer(LocalTime.class, LocalTimeKeyDeserializer.INSTANCE);
    }

    @Override
    public void setupModule(SetupContext context) {
        super.setupModule(context);
        context.addValueInstantiators(new ValueInstantiators.Base() {
            @Override
            public ValueInstantiator findValueInstantiator(DeserializationConfig config,
                    BeanDescription beanDesc, ValueInstantiator defaultInstantiator)
            {
                Class<?> raw = beanDesc.getBeanClass();
                // 15-May-2015, tatu: In theory not safe, but in practice we do need to do "fuzzy" matching
                // because we will (for now) be getting a subtype, but in future may want to downgrade
                // to the common base type. Even more, serializer may purposefully force use of base type.
                // So... in practice it really should always work, in the end. :)
                if (ZoneId.class.isAssignableFrom(raw)) {
                    // let's assume we should be getting "empty" StdValueInstantiator here:
                    if (defaultInstantiator instanceof StdValueInstantiator) {
                        StdValueInstantiator inst = (StdValueInstantiator) defaultInstantiator;
                        // one further complication: we need ZoneId info, not sub-class
                        AnnotatedClass ac;
                        if (raw == ZoneId.class) {
                            ac = beanDesc.getClassInfo();
                        } else {
                            // we don't need Annotations, so constructing directly is fine here
                            // even if it's not generally recommended
                            ac = AnnotatedClass.construct(ZoneId.class, null, null);
                        }
                        if (!inst.canCreateFromString()) {
                            AnnotatedMethod factory = _findFactory(ac, "of", String.class);
                            if (factory != null) {
                                inst.configureFromStringCreator(factory);
                            }
                            // otherwise... should we indicate an error?
                        }
                        // return ZoneIdInstantiator.construct(config, beanDesc, defaultInstantiator);
                    }
                }
                return defaultInstantiator;
            }
        });
    }

    // For
    protected AnnotatedMethod _findFactory(AnnotatedClass cls, String name, Class<?>... argTypes) {
        final int argCount = argTypes.length;
        for (AnnotatedMethod method : cls.getStaticMethods()) {
            if (!name.equals(method.getName())
                    || (method.getParameterCount() != argCount)) {
                continue;
            }
            for (int i = 0; i < argCount; ++i) {
                Class<?> argType = method.getParameter(i).getRawType();
                if (!argType.isAssignableFrom(argTypes[i])) {
                    continue;
                }
            }
            return method;
        }
        return null;
    }

}

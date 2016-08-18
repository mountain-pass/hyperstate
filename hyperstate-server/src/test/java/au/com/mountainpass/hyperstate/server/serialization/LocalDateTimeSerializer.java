package au.com.mountainpass.hyperstate.server.serialization;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

public class LocalDateTimeSerializer
        extends StdScalarSerializer<LocalDateTime> {

    /**
     * 
     */
    private static final long serialVersionUID = 7394975417633676662L;

    public LocalDateTimeSerializer() {
        super(LocalDateTime.class);
    }

    @Override
    public void serialize(final LocalDateTime value, final JsonGenerator jgen,
            final SerializerProvider provider)
                    throws IOException, JsonGenerationException {
        jgen.writeString(value.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

    }

}

package au.com.mountainpass.hyperstate.server.serialization;

import java.io.IOException;

import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

public class MediaTypeSerializer extends StdScalarSerializer<MediaType> {

    /**
     * 
     */
    private static final long serialVersionUID = 7394975417633676662L;

    public MediaTypeSerializer() {
        super(MediaType.class);
    }

    @Override
    public void serialize(MediaType value, JsonGenerator jgen,
            SerializerProvider provider)
                    throws IOException, JsonGenerationException {
        jgen.writeString(value.toString());

    }

}

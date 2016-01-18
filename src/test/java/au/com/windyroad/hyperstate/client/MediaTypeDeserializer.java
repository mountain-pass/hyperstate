package au.com.windyroad.hyperstate.client;

import java.io.IOException;

import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;

public class MediaTypeDeserializer extends FromStringDeserializer<MediaType> {
    /**
     * 
     */
    private static final long serialVersionUID = 1726311002290206480L;

    public MediaTypeDeserializer() {
        super(MediaType.class);
    }

    @Override
    protected MediaType _deserialize(String value, DeserializationContext ctxt)
            throws IOException {
        return MediaType.valueOf(value);
    }
}
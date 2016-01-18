package au.com.windyroad.hyperstate.server.serialization;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

@Component
public class MessageSourceAwareSerializer extends JsonSerializer<String> {

    @Autowired
    private MessageSource messageSource;

    public MessageSourceAwareSerializer() {
    }

    @Override
    public void serialize(String value, JsonGenerator jgen,
            SerializerProvider provider)
                    throws IOException, JsonProcessingException {
        if (messageSource == null) {
            jgen.writeString(value);
        } else {
            jgen.writeString(interpolate(value));
        }
    }

    public String interpolate(CharSequence value) {
        Pattern patt = Pattern.compile("\\{(.*?)\\}");
        Matcher m = patt.matcher(value);
        StringBuffer sb = new StringBuffer(value.length());
        while (m.find()) {
            String code = m.group(1);
            m.appendReplacement(sb, Matcher.quoteReplacement(messageSource
                    .getMessage(code, null, LocaleContextHolder.getLocale())));
        }
        m.appendTail(sb);
        return sb.toString();
    }
}
package au.com.mountainpass.hyperstate.client.builder;

import java.net.URI;

import javax.annotation.PostConstruct;

import org.springframework.http.HttpMethod;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import au.com.mountainpass.hyperstate.client.CreateAction;
import au.com.mountainpass.hyperstate.client.DeleteAction;
import au.com.mountainpass.hyperstate.client.GetAction;
import au.com.mountainpass.hyperstate.client.RestAddress;
import au.com.mountainpass.hyperstate.client.RestTemplateResolver;
import au.com.mountainpass.hyperstate.client.UpdateAction;
import au.com.mountainpass.hyperstate.core.Action;
import au.com.mountainpass.hyperstate.core.Parameter;

public class RestActionBuilder {

    private Parameter[] fields = {};
    private URI href;
    private String name;
    private HttpMethod method;

    private final RestTemplateResolver resolver;

    @JsonCreator
    public RestActionBuilder(
            @JacksonInject final RestTemplateResolver resolver) {
        this.resolver = resolver;
    }

    public Action<?> build() {
        RestAddress address = new RestAddress(resolver, href);
        switch (method) {
        case POST:
            return new CreateAction(name, address, fields);
        case DELETE:
            return new DeleteAction(name, address, fields);
        case GET:
            return new GetAction(name, address, fields);
        case PUT:
            return new UpdateAction(name, address, fields);
        default:
            return null;
        }
    }

    @PostConstruct
    public void postConstruct() {

    }

    @JsonProperty("fields")
    public RestActionBuilder setFields(final Parameter[] fields) {
        this.fields = fields;
        return this;
    }

    @JsonProperty("href")
    public RestActionBuilder setHref(final URI href) {
        this.href = href;
        return this;
    }

    @JsonProperty("method")
    public RestActionBuilder setMethod(final HttpMethod method) {
        this.method = method;
        return this;
    }

    @JsonProperty("name")
    public RestActionBuilder setName(final String name) {
        this.name = name;
        return this;
    }
}

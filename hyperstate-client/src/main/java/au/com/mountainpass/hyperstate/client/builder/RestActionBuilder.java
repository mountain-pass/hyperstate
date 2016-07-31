package au.com.mountainpass.hyperstate.client.builder;

import java.net.URI;

import javax.annotation.PostConstruct;

import org.springframework.http.HttpMethod;
import org.springframework.web.client.AsyncRestTemplate;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import au.com.mountainpass.hyperstate.client.CreateAction;
import au.com.mountainpass.hyperstate.client.DeleteAction;
import au.com.mountainpass.hyperstate.client.GetAction;
import au.com.mountainpass.hyperstate.client.RestLink;
import au.com.mountainpass.hyperstate.client.UpdateAction;
import au.com.mountainpass.hyperstate.core.Action;
import au.com.mountainpass.hyperstate.core.Parameter;
import au.com.mountainpass.hyperstate.core.Resolver;

public class RestActionBuilder {

    private Parameter[] fields = {};
    private URI href;
    private String identifier;
    private HttpMethod method;

    private final Resolver resolver;

    private AsyncRestTemplate asyncRestTemplate;

    @JsonCreator
    public RestActionBuilder(@JacksonInject final Resolver resolver,
            @JacksonInject final AsyncRestTemplate asyncRestTemplate) {
        this.resolver = resolver;
        this.asyncRestTemplate = asyncRestTemplate;
    }

    public Action<?> build() {
        RestLink link = new RestLink(resolver, asyncRestTemplate, href,
                identifier, null);
        switch (method) {
        case POST:
            return new CreateAction(resolver, identifier, link, fields);
        case DELETE:
            return new DeleteAction(resolver, identifier, link, fields);
        case GET:
            return new GetAction(resolver, identifier, link, fields);
        case PUT:
            return new UpdateAction(resolver, identifier, link, fields);
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
    public RestActionBuilder setName(final String identifier) {
        this.identifier = identifier;
        return this;
    }
}

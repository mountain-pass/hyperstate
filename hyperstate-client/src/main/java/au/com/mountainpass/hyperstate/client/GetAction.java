package au.com.mountainpass.hyperstate.client;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpMethod;

import au.com.mountainpass.hyperstate.core.Action;
import au.com.mountainpass.hyperstate.core.Address;
import au.com.mountainpass.hyperstate.core.Parameter;
import au.com.mountainpass.hyperstate.core.entities.EntityWrapper;

public class GetAction extends Action<EntityWrapper<?>> {

    public GetAction(final String name, final Address address,
            final Parameter[] fields) {
        super(name, address, fields);
    }

    @Override
    public CompletableFuture<EntityWrapper<?>> doInvoke(
            final Map<String, Object> filteredParameters) {
        return getAddress().get(filteredParameters);
    }

    @Override
    public HttpMethod getNature() {
        return HttpMethod.GET;
    }

}

package au.com.mountainpass.hyperstate.client;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpMethod;

import au.com.mountainpass.hyperstate.core.Action;
import au.com.mountainpass.hyperstate.core.Address;
import au.com.mountainpass.hyperstate.core.Parameter;

public class DeleteAction extends Action<Void> {

    public DeleteAction(final String name, final Address address,
            final Parameter[] fields) {
        super(name, address, fields);
    }

    @Override
    public CompletableFuture<Void> doInvoke(
            final Map<String, Object> filteredParameters) {
        return getAddress().delete(filteredParameters);
    }

    /**
     * @return the nature
     */
    @Override
    public HttpMethod getNature() {
        return HttpMethod.DELETE;
    }

}

package au.com.mountainpass.hyperstate.client;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpMethod;

import au.com.mountainpass.hyperstate.core.Action;
import au.com.mountainpass.hyperstate.core.Address;
import au.com.mountainpass.hyperstate.core.Parameter;
import au.com.mountainpass.hyperstate.core.entities.CreatedEntity;

public class CreateAction extends Action<CreatedEntity> {

    public CreateAction(final String name, final Address link,
            final Parameter[] fields) {
        super(name, link, fields);
    }

    @Override
    public CompletableFuture<CreatedEntity> doInvoke(
            final Map<String, Object> filteredParameters) {
        return getAddress().create(filteredParameters);
    }

    /**
     * @return the nature
     */
    @Override
    public HttpMethod getNature() {
        return HttpMethod.POST;
    }

}

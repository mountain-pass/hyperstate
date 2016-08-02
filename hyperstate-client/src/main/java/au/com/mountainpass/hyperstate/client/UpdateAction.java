package au.com.mountainpass.hyperstate.client;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpMethod;

import au.com.mountainpass.hyperstate.core.Action;
import au.com.mountainpass.hyperstate.core.Address;
import au.com.mountainpass.hyperstate.core.Parameter;
import au.com.mountainpass.hyperstate.core.entities.UpdatedEntity;

public class UpdateAction extends Action<UpdatedEntity> {

    public UpdateAction(final String name, final Address link,
            final Parameter[] fields) {
        super(name, link, fields);
    }

    @Override
    public CompletableFuture<UpdatedEntity> doInvoke(
            final Map<String, Object> filteredParameters) {
        return getAddress().update(filteredParameters);
    }

    /**
     * @return the nature
     */
    @Override
    public HttpMethod getNature() {
        return HttpMethod.PUT;
    }

}

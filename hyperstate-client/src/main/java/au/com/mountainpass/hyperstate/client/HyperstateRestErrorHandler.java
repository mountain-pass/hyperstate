package au.com.mountainpass.hyperstate.client;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;

import au.com.mountainpass.hyperstate.exceptions.EntityNotFoundException;

public class HyperstateRestErrorHandler implements ResponseErrorHandler {

    private DefaultResponseErrorHandler defaultResponseErrorHandler = new DefaultResponseErrorHandler();

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return defaultResponseErrorHandler.hasError(response);
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new EntityNotFoundException();
        }
        defaultResponseErrorHandler.handleError(response);
    }

}

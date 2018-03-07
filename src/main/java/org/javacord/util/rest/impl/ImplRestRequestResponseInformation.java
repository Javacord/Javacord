package org.javacord.util.rest.impl;

import org.javacord.DiscordApi;
import org.javacord.util.rest.RestRequestInformation;
import org.javacord.util.rest.RestRequestResponseInformation;
import org.javacord.util.rest.RestRequestResult;

import java.util.Optional;

/**
 * The implementation of {@link RestRequestResponseInformation}.
 */
public class ImplRestRequestResponseInformation implements RestRequestResponseInformation {

    private final RestRequestInformation request;
    private final RestRequestResult restRequestResult;

    /**
     * Creates a new rest request response information.
     *
     * @param request The request which this response answered.
     * @param restRequestResult The result of the response.
     */
    public ImplRestRequestResponseInformation(RestRequestInformation request, RestRequestResult restRequestResult) {
        this.request = request;
        this.restRequestResult = restRequestResult;
    }

    /**
     * Gets the rest request result.
     *
     * @return The rest request result.
     */
    public RestRequestResult getRestRequestResult() {
        return restRequestResult;
    }

    @Override
    public DiscordApi getApi() {
        return getRequest().getApi();
    }

    @Override
    public RestRequestInformation getRequest() {
        return request;
    }

    @Override
    public int getCode() {
        return restRequestResult.getResponse().code();
    }

    @Override
    public Optional<String> getBody() {
        return restRequestResult.getStringBody();
    }

}

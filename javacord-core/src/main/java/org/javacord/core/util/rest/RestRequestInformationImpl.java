package org.javacord.core.util.rest;

import org.javacord.api.DiscordApi;
import org.javacord.api.util.rest.RestRequestInformation;

import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * The implementation of {@link RestRequestInformation}.
 */
public class RestRequestInformationImpl implements RestRequestInformation {

    private final DiscordApi api;
    private final URL url;
    private final Map<String, String> queryParameters;
    private final Map<String, String> headers;
    private final String body;

    /**
     * Creates a new rest request information.
     *
     * @param api The responsible discord api instance.
     * @param url The url, the request should be sent to.
     * @param queryParameter The query parameters of the rest request.
     * @param headers The headers of the rest request.
     * @param body The body of the rest request.
     */
    public RestRequestInformationImpl(DiscordApi api, URL url, Map<String, String> queryParameter,
                                      Map<String, String> headers, String body) {
        this.api = api;
        this.url = url;
        this.queryParameters = queryParameter;
        this.headers = headers;
        this.body = body;
    }

    @Override
    public DiscordApi getApi() {
        return api;
    }

    @Override
    public URL getUrl() {
        return url;
    }

    @Override
    public Map<String, String> getQueryParameters() {
        return Collections.unmodifiableMap(queryParameters);
    }

    @Override
    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    @Override
    public Optional<String> getBody() {
        return Optional.ofNullable(body);
    }

}

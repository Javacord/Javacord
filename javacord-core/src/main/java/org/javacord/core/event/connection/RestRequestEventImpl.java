package org.javacord.core.event.connection;

import org.javacord.api.DiscordApi;
import org.javacord.api.event.connection.RestRequestEvent;
import org.javacord.api.util.auth.Request;
import org.javacord.api.util.auth.Response;
import org.javacord.core.event.EventImpl;

/**
 * The implementation of {@link RestRequestEvent}.
 */
public class RestRequestEventImpl extends EventImpl implements RestRequestEvent {

    private final Request request;
    private final Response response;

    /**
     * Creates a new resume event.
     *
     * @param api The api instance of the event.
     * @param request The request.
     * @param response The response.
     */
    public RestRequestEventImpl(DiscordApi api, Request request, Response response) {
        super(api);
        this.request = request;
        this.response = response;
    }

    @Override
    public Request getRequest() {
        return request;
    }

    @Override
    public Response getResponse() {
        return response;
    }

}

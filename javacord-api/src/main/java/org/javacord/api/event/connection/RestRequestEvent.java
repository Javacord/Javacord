package org.javacord.api.event.connection;

import org.javacord.api.event.Event;
import org.javacord.api.util.auth.Request;
import org.javacord.api.util.auth.Response;

/**
 * A rest request event.
 */
public interface RestRequestEvent extends Event {

    /**
     * Gets the request that is used to perform the rest request call.
     *
     * @return The request.
     */
    Request getRequest();

    /**
     * Gets the response of the rest request call.
     *
     * @return The response.
     */
    Response getResponse();

}

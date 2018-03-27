package org.javacord.api.util.rest;

import org.javacord.api.DiscordApi;

import java.util.Optional;

/**
 * Some information about a rest request response.
 */
public interface RestRequestResponseInformation {

    /**
     * Gets the discord api instance which created the request.
     *
     * @return The responsible discord api instance.
     */
    DiscordApi getApi();

    /**
     * Gets the request which this response answered.
     *
     * @return The request which this response answered.
     */
    RestRequestInformation getRequest();

    /**
     * Gets the response code of the response.
     *
     * @return The response code of the response.
     */
    int getCode();

    /**
     * Gets the body of the response as string.
     *
     * @return The body of the response.
     */
    Optional<String> getBody();

}

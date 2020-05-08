package org.javacord.api.util.auth;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * This class represents an authenticator that can be used to provide authentication headers for remote servers.
 */
@FunctionalInterface
public interface Authenticator {

    /**
     * Gets authentication headers to authenticate against a remove server for the call described by the given
     * parameters.
     *
     * <p>The return value might be {@code null} or an empty map if no headers should be modified in the request, for
     * example because no challenge can be satisfied. If the map has entries, their keys are the HTTP headers to be set,
     * the values are the header values for the key. If the first entry in the values list is {@code null}, or the whole
     * values list is {@code null}, the given header values replace any existing values for that header. Otherwise all
     * header values are added additionally to eventually already existing ones.
     *
     * <i>Note:</i> It might be advisable to not return the same set of headers again if the originating request already
     * contained these headers. If the authenticator is called again, obviously the authentication did not work
     * properly. So returning the same headers again might end up in an endless loop.
     *
     * @param route    The route to which a request is done that needs to be authenticated.
     * @param request  The originating request that led to the authentication attempt.
     * @param response The response that demands authentication.
     * @return The authentication headers that should be set in the request.
     * @throws IOException If an IO error occurs.
     */
    Map<String, List<String>> authenticate(Route route, Request request, Response response) throws IOException;

}

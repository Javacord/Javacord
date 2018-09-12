package org.javacord.api.util.auth;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This class represents the originating request that led to a response that demands authentication.
 */
public interface Request {

    /**
     * The method of the request.
     *
     * @return The method of the request.
     */
    default String getMethod() {
        return "GET";
    }

    /**
     * The headers of the request.
     *
     * @return The headers of the request.
     */
    default Map<String, List<String>> getHeaders() {
        return Collections.emptyMap();
    }

    /**
     * The headers of the request with the given name.
     *
     * @param headerName The name of the header for which to return the values.
     * @return The headers of the request with the given name.
     */
    default List<String> getHeaders(String headerName) {
        return getHeaders().get(headerName);
    }

    /**
     * The body of the request.
     *
     * @return The body of the request.
     * @throws IOException If an IO error occurs.
     */
    default Optional<String> getBody() throws IOException {
        return Optional.empty();
    }

}

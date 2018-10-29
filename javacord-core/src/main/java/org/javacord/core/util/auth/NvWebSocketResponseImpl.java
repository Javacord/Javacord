package org.javacord.core.util.auth;

import org.javacord.api.util.auth.Response;

import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * The implementation of {@link Response} for NV web socket.
 */
public class NvWebSocketResponseImpl implements Response {

    @Override
    public int getCode() {
        return HttpURLConnection.HTTP_PROXY_AUTH;
    }

    @Override
    public String getMessage() {
        return "Proxy Authentication Required";
    }

    @Override
    public Map<String, List<String>> getHeaders() {
        return Collections.singletonMap("Proxy-Authenticate", Collections.singletonList("Basic realm=proxy"));
    }

}

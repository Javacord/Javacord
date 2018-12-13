package org.javacord.core.util.auth;

import okhttp3.RequestBody;
import okio.Buffer;
import org.javacord.api.util.auth.Request;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * The implementation of {@link Request} for OkHttp.
 */
public class OkHttpRequestImpl implements Request {

    /**
     * The real request from OkHttp.
     */
    private final okhttp3.Request request;

    /**
     * Creates a new OkHttpRequestImpl.
     *
     * @param request The real request from OkHttp.
     */
    public OkHttpRequestImpl(okhttp3.Request request) {
        this.request = request;
    }

    @Override
    public String getMethod() {
        return request.method();
    }

    @Override
    public Map<String, List<String>> getHeaders() {
        return request.headers().toMultimap();
    }

    @Override
    public List<String> getHeaders(String headerName) {
        return request.headers(headerName);
    }

    @Override
    public Optional<String> getBody() throws IOException {
        RequestBody requestBody = request.body();
        if (requestBody == null) {
            return Optional.empty();
        }

        Buffer buffer = new Buffer();
        requestBody.writeTo(buffer);
        return Optional.of(buffer.readUtf8());
    }

    @Override
    public boolean equals(Object o) {
        try {
            if (this == o) {
                return true;
            }
            if ((o == null) || (getClass() != o.getClass())) {
                return false;
            }
            OkHttpRequestImpl that = (OkHttpRequestImpl) o;
            return Objects.equals(getMethod(), that.getMethod())
                    && Objects.equals(getHeaders(), that.getHeaders())
                    && Objects.equals(getBody(), that.getBody());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int hashCode() {
        try {
            return Objects.hash(getMethod(), getHeaders(), getBody());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        String body;
        try {
            body = getBody().toString();
        } catch (IOException e) {
            body = "<unknown>";
        }
        return String.format("Request (method: %s, headers: %s, body: %s)", getMethod(), getHeaders(), body);
    }

}

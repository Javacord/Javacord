package org.javacord.core.util.auth;

import okhttp3.ResponseBody;
import org.javacord.api.util.auth.Response;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * The implementation of {@link Response} for OkHttp.
 */
public class OkHttpResponseImpl implements Response {

    /**
     * The real response from OkHttp.
     */
    private final okhttp3.Response response;

    /**
     * Creates a new OkHttpResponseImpl.
     *
     * @param response The real response from OkHttp.
     */
    public OkHttpResponseImpl(okhttp3.Response response) {
        this.response = response;
    }

    @Override
    public int getCode() {
        return response.code();
    }

    @Override
    public String getMessage() {
        return response.message();
    }

    @Override
    public Map<String, List<String>> getHeaders() {
        return response.headers().toMultimap();
    }

    @Override
    public Optional<String> getBody() throws IOException {
        ResponseBody responseBody = response.body();
        if (responseBody == null) {
            return Optional.empty();
        }

        return Optional.of(responseBody.string());
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
            OkHttpResponseImpl that = (OkHttpResponseImpl) o;
            return getCode() == that.getCode()
                    && Objects.equals(getMessage(), that.getMessage())
                    && Objects.equals(getHeaders(), that.getHeaders())
                    && Objects.equals(getBody(), that.getBody());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int hashCode() {
        try {
            return Objects.hash(getCode(), getMessage(), getHeaders(), getBody());
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
        return String.format(
                "Response (code: %d, message: %s, headers: %s, body: %s)", getCode(), getMessage(), getHeaders(), body);
    }

}

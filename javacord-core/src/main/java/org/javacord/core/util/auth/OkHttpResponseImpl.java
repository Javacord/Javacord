package org.javacord.core.util.auth;

import okhttp3.ResponseBody;
import org.javacord.api.util.auth.Response;
import org.javacord.core.util.rest.RestRequest;
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

    private final String stringBody;

    /**
     * Creates a new OkHttpResponseImpl.
     *
     * @param response The real response from OkHttp.
     */
    public OkHttpResponseImpl(okhttp3.Response response) {
        this.response = response;

        String bodyString = null;
        try (ResponseBody responseBody = response.body()) {
            bodyString = responseBody == null ? null : responseBody.string();
        } catch (IOException ignored) {
        }
        stringBody = bodyString;
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
    public Optional<String> getStringBody() {
        return Optional.ofNullable(stringBody);
    }

    /**
     * Gets the response of the {@link RestRequest}.
     *
     * @return The response of the RestRequest.
     */
    public okhttp3.Response getResponse() {
        return response;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        OkHttpResponseImpl that = (OkHttpResponseImpl) o;
        return getCode() == that.getCode()
                && Objects.equals(getMessage(), that.getMessage())
                && Objects.equals(getHeaders(), that.getHeaders());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCode(), getMessage(), getHeaders());
    }

    @Override
    public String toString() {
        String body;
        body = getStringBody().orElse("");
        return String.format(
                "Response (code: %d, message: %s, headers: %s, body: %s)", getCode(), getMessage(), getHeaders(), body);
    }

}

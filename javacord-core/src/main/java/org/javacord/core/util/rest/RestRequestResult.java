package org.javacord.core.util.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.util.Optional;

/**
 * The result of a {@link RestRequest}.
 */
public class RestRequestResult {

    private final RestRequest<?> request;
    private final Response response;
    private final ResponseBody body;
    private final String stringBody;
    private final JsonNode jsonBody;

    /**
     * Creates a new RestRequestResult.
     *
     * @param request The request of the result.
     * @param response The response of the RestRequest.
     * @throws IOException Passed on from {@link ResponseBody#string()}.
     */
    public RestRequestResult(RestRequest<?> request, Response response) throws IOException {
        this.request = request;
        this.response = response;
        this.body = response.body();
        if (body == null) {
            stringBody = null;
            jsonBody = NullNode.getInstance();
        } else {
            stringBody = body.string();
            ObjectMapper mapper = request.getApi().getObjectMapper();
            JsonNode jsonBody = mapper.readTree(stringBody);
            this.jsonBody = jsonBody == null ? NullNode.getInstance() : jsonBody;
        }
    }

    /**
     * Gets the {@link RestRequest} which belongs to this result.
     *
     * @return The Request which belongs to this result.
     */
    public RestRequest<?> getRequest() {
        return request;
    }

    /**
     * Gets the response of the {@link RestRequest}.
     *
     * @return The response of the RestRequest.
     */
    public Response getResponse() {
        return response;
    }

    /**
     * Gets the body of the response.
     *
     * @return The body of the response.
     */
    public Optional<ResponseBody> getBody() {
        return Optional.ofNullable(body);
    }

    /**
     * Gets the string body of the response.
     *
     * @return The string body of the response.
     */
    public Optional<String> getStringBody() {
        return Optional.ofNullable(stringBody);
    }

    /**
     * Gets the json body of the response.
     * Returns a {@link NullNode} if the response had no body.
     *
     * @return The json body of the response.
     */
    public JsonNode getJsonBody() {
        return jsonBody;
    }

}

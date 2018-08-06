package org.javacord.core.util.rest;

import com.fasterxml.jackson.databind.JsonNode;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.exception.DiscordException;
import org.javacord.api.util.rest.RestRequestInformation;
import org.javacord.api.util.rest.RestRequestResponseInformation;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.util.logging.LoggerUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * This class is used to wrap a rest request.
 */
public class RestRequest<T> {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(RestRequest.class);

    private final DiscordApiImpl api;
    private final RestMethod method;
    private final RestEndpoint endpoint;

    private volatile boolean includeAuthorizationHeader = true;
    private volatile String[] urlParameters = new String[0];
    private final Map<String, String> queryParameters = new HashMap<>();
    private final Map<String, String> headers = new HashMap<>();
    private volatile String body = null;

    private final CompletableFuture<RestRequestResult> result = new CompletableFuture<>();

    /**
     * The multipart body of the request.
     */
    private MultipartBody multipartBody;

    /**
     * The custom major parameter if it's not included in the url (e.g. for reactions)
     */
    private String customMajorParam = null;

    /**
     * The origin of the rest request.
     */
    private final Exception origin;

    /**
     * Creates a new instance of this class.
     *
     * @param api The api which will be used to execute the request.
     * @param method The http method of the request.
     * @param endpoint The endpoint to which the request should be sent.
     */
    public RestRequest(DiscordApi api, RestMethod method, RestEndpoint endpoint) {
        this.api = (DiscordApiImpl) api;
        this.method = method;
        this.endpoint = endpoint;

        this.origin = new Exception("origin of RestRequest call");
    }

    /**
     * Gets the api which is used for this request.
     *
     * @return The api which is used for this request.
     */
    public DiscordApiImpl getApi() {
        return api;
    }

    /**
     * Gets the method of this request.
     *
     * @return The method of this request.
     */
    public RestMethod getMethod() {
        return method;
    }

    /**
     * Gets the endpoint of this request.
     *
     * @return The endpoint of this request.
     */
    public RestEndpoint getEndpoint() {
        return endpoint;
    }

    /**
     * Gets an array with all used url parameters.
     *
     * @return An array with all used url parameters.
     */
    public String[] getUrlParameters() {
        return urlParameters;
    }

    /**
     * Gets the body of this request.
     *
     * @return The body of this request.
     */
    public Optional<String> getBody() {
        return Optional.ofNullable(body);
    }

    /**
     * Gets the major url parameter of this request.
     * If an request has a major parameter, it means that the ratelimits for this request are based on this parameter.
     *
     * @return The major url parameter used for ratelimits.
     */
    public Optional<String> getMajorUrlParameter() {
        if (customMajorParam != null) {
            return Optional.of(customMajorParam);
        }
        Optional<Integer> majorParameterPosition = endpoint.getMajorParameterPosition();
        if (!majorParameterPosition.isPresent()) {
            return Optional.empty();
        }
        if (majorParameterPosition.get() >= urlParameters.length) {
            return Optional.empty();
        }
        return Optional.of(urlParameters[majorParameterPosition.get()]);
    }

    /**
     * Gets the origin of the rest request.
     *
     * @return The origin of the rest request.
     */
    public Exception getOrigin() {
        return origin;
    }

    /**
     * Adds a query parameter to the url.
     *
     * @param key The key of the parameter.
     * @param value The value of the parameter.
     * @return The current instance in order to chain call methods.
     */
    public RestRequest<T> addQueryParameter(String key, String value) {
        queryParameters.put(key, value);
        return this;
    }

    /**
     * Adds a header to the request.
     *
     * @param name The name of the header.
     * @param value The value of the header.
     * @return The current instance in order to chain call methods.
     */
    public RestRequest<T> addHeader(String name, String value) {
        headers.put(name, value);
        return this;
    }

    /**
     * Adds a {@code X-Audit-Log-Reason} header to the request with the given reason.
     *
     * @param reason The reason.
     * @return The current instance in order to chain call methods.
     */
    public RestRequest<T> setAuditLogReason(String reason) {
        if (reason != null) {
            addHeader("X-Audit-Log-Reason", reason);
        }
        return this;
    }

    /**
     * Sets the url parameters, e.g. a channel id.
     *
     * @param parameters The parameters.
     * @return The current instance in order to chain call methods.
     */
    public RestRequest<T> setUrlParameters(String... parameters) {
        this.urlParameters = parameters;
        return this;
    }

    /**
     * Sets the multipart body of the request.
     * If a multipart body is set, the {@link #setBody(String)} method is ignored!
     *
     * @param multipartBody The multipart body of the request.
     * @return The current instance in order to chain call methods.
     */
    public RestRequest<T> setMultipartBody(MultipartBody multipartBody) {
        this.multipartBody = multipartBody;
        return this;
    }

    /**
     * Sets a custom major parameter.
     *
     * @param customMajorParam The custom parameter to set.
     * @return The current instance in order to chain call methods.
     */
    public RestRequest<T> setCustomMajorParam(String customMajorParam) {
        this.customMajorParam = customMajorParam;
        return this;
    }

    /**
     * Sets the body of the request.
     *
     * @param body The body of the request.
     * @return The current instance in order to chain call methods.
     */
    public RestRequest<T> setBody(JsonNode body) {
        return setBody(body.toString());
    }

    /**
     * Sets the body of the request.
     *
     * @param body The body of the request.
     * @return The current instance in order to chain call methods.
     */
    public RestRequest<T> setBody(String body) {
        this.body = body;
        return this;
    }

    /**
     * Sets if an authorization header should be included in this request.
     *
     * @param includeAuthorizationHeader Whether the authorization header should be included or not.
     * @return The current instance in order to chain call methods.
     */
    public RestRequest<T> includeAuthorizationHeader(boolean includeAuthorizationHeader) {
        this.includeAuthorizationHeader = includeAuthorizationHeader;
        return this;
    }

    /**
     * Executes the request. This will automatically retry if we hit a ratelimit.
     *
     * @param function A function which processes the rest response to the requested object.
     * @return A future which will contain the output of the function.
     */
    public CompletableFuture<T> execute(Function<RestRequestResult, T> function) {
        api.getRatelimitManager().queueRequest(this);
        CompletableFuture<T> future = new CompletableFuture<>();
        result.whenComplete((result, throwable) -> {
            if (throwable != null) {
                future.completeExceptionally(throwable);
                return;
            }
            try {
                future.complete(function.apply(result));
            } catch (Throwable t) {
                future.completeExceptionally(t);
            }
        });
        return future;
    }

    /**
     * Gets the result of this request. This will not start executing, just return the result!
     *
     * @return Gets the result of this request.
     */
    public CompletableFuture<RestRequestResult> getResult() {
        return result;
    }

    /**
     * Gets the information for this rest request.
     *
     * @return The information for this rest request.
     */
    public RestRequestInformation asRestRequestInformation() {
        try {
            return new RestRequestInformationImpl(
                    api, new URL(endpoint.getFullUrl(urlParameters)), queryParameters, headers, body);
        } catch (MalformedURLException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Executes the request blocking.
     *
     * @return The result of the request.
     * @throws Exception If something went wrong while executing the request.
     */
    public RestRequestResult executeBlocking() throws Exception {
        Request.Builder requestBuilder = new Request.Builder();
        HttpUrl.Builder httpUrlBuilder = endpoint.getOkHttpUrl(urlParameters).newBuilder();
        queryParameters.forEach(httpUrlBuilder::addQueryParameter);
        requestBuilder.url(httpUrlBuilder.build());

        RequestBody requestBody;
        if (multipartBody != null) {
            requestBody = multipartBody;
        } else if (body != null) {
            requestBody = RequestBody.create(MediaType.parse("application/json"), body);
        } else {
            requestBody = RequestBody.create(null, new byte[0]);
        }

        switch (method) {
            case GET:
                requestBuilder.get();
                break;
            case POST:
                requestBuilder.post(requestBody);
                break;
            case PUT:
                requestBuilder.put(requestBody);
                break;
            case DELETE:
                requestBuilder.delete(requestBody);
                break;
            case PATCH:
                requestBuilder.patch(requestBody);
                break;
            default:
                throw new IllegalArgumentException("Unsupported http method!");
        }
        if (includeAuthorizationHeader) {
            requestBuilder.addHeader("authorization", api.getPrefixedToken());
        }
        headers.forEach(requestBuilder::addHeader);
        logger.debug("Trying to send {} request to {}{}",
                method::name, () -> endpoint.getFullUrl(urlParameters), () -> body != null ? " with body " + body : "");

        try (Response response = getApi().getHttpClient().newCall(requestBuilder.build()).execute()) {
            RestRequestResult result = new RestRequestResult(this, response);
            logger.debug("Sent {} request to {} and received status code {} with{} body{}",
                    method::name, () -> endpoint.getFullUrl(urlParameters), response::code,
                    () -> result.getBody().map(b -> "").orElse(" empty"),
                    () -> result.getStringBody().map(s -> " " + s).orElse(""));

            if (response.code() >= 300 || response.code() < 200) {

                RestRequestInformation requestInformation = asRestRequestInformation();
                RestRequestResponseInformation responseInformation = new RestRequestResponseInformationImpl(
                        requestInformation, result);
                Optional<RestRequestHttpResponseCode> responseCode = RestRequestHttpResponseCode
                        .fromCode(response.code());

                // Check if the response body contained a know error code
                if (!result.getJsonBody().isNull() && result.getJsonBody().has("code")) {
                    int code = result.getJsonBody().get("code").asInt();
                    String message = result.getJsonBody().has("message")
                            ? result.getJsonBody().get("message").asText()
                            : null;
                    Optional<? extends DiscordException> discordException =
                            RestRequestResultErrorCode.fromCode(code, responseCode.orElse(null))
                                    .flatMap(restRequestResultCode -> restRequestResultCode.getDiscordException(
                                            origin, (message == null) ? restRequestResultCode.getMeaning() : message,
                                            requestInformation, responseInformation));
                    // There's an exception for this specific response code
                    if (discordException.isPresent()) {
                        throw discordException.get();
                    }
                }

                switch (response.code()) {
                    case 429:
                        // A 429 will be handled in the RatelimitManager class
                        return result;
                    default:
                        // There are specific exceptions for specific response codes (e.g. NotFoundException for 404)
                        Optional<? extends DiscordException> discordException = responseCode
                                .flatMap(restRequestHttpResponseCode ->
                                                 restRequestHttpResponseCode.getDiscordException(
                                                         origin,
                                                         "Received a " + response.code() + " response from Discord with"
                                                         + (result.getBody().isPresent() ? "" : " empty")
                                                         + " body"
                                                         + result.getStringBody().map(s -> " " + s).orElse("")
                                                         + "!",
                                                         requestInformation, responseInformation));
                        if (discordException.isPresent()) {
                            throw discordException.get();
                        } else {
                            // No specific exception was defined for the response code, so throw a "normal"
                            throw new DiscordException(
                                    origin, "Received a " + response.code() + " response from Discord with"
                                            + (result.getBody().isPresent() ? "" : " empty") + " body"
                                            + result.getStringBody().map(s -> " " + s).orElse("") + "!",
                                    requestInformation, responseInformation);
                        }
                }
            }
            return result;
        }
    }

}

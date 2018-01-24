package de.btobastian.javacord.utils.rest;

import com.fasterxml.jackson.databind.JsonNode;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.exceptions.DiscordException;
import de.btobastian.javacord.utils.logging.LoggerUtil;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
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

    private final DiscordApi api;
    private final RestMethod method;
    private final RestEndpoint endpoint;

    private boolean includeAuthorizationHeader = true;
    private int ratelimitRetries = 50;
    private String[] urlParameters = new String[0];
    private List<String[]> queryParameters = new ArrayList<>();
    private String body = null;

    private int retryCounter = 0;

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
        this.api = api;
        this.method = method;
        this.endpoint = endpoint;

        this.origin = new Exception();
    }

    /**
     * Gets the api which is used for this request.
     *
     * @return The api which is used for this request.
     */
    public DiscordApi getApi() {
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
        this.queryParameters.add(new String[]{key, value});
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
     * Sets the amount of ratelimit retries we should use with this request.
     *
     * @param retries The amount of ratelimit retries.
     * @return The current instance in order to chain call methods.
     */
    public RestRequest<T> setRatelimitRetries(int retries) {
        if (retries < 0) {
            throw new IllegalArgumentException("Retries cannot be less than 0!");
        }
        this.ratelimitRetries = retries;
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
     * Increments the amounts of ratelimit retries.
     *
     * @return <code>true</code> if the maximum ratelimit retries were exceeded.
     */
    public boolean incrementRetryCounter() {
        return ++retryCounter > ratelimitRetries;
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
     * Executes the request blocking.
     *
     * @return The result of the request.
     * @throws Exception If something went wrong while executing the request.
     */
    public RestRequestResult executeBlocking() throws Exception {
        Request.Builder requestBuilder = new Request.Builder();
        HttpUrl.Builder httpUrlBuilder = endpoint.getOkHttpUrl(urlParameters).newBuilder();
        for (String[] queryParameter : queryParameters) {
            httpUrlBuilder.addQueryParameter(queryParameter[0], queryParameter[1]);
        }
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
            requestBuilder.addHeader("authorization", api.getToken());
        }
        logger.debug("Trying to send {} request to {}{}",
                method.name(), endpoint.getFullUrl(urlParameters), body != null ? " with body " + body : "");

        try (Response response = getApi().getHttpClient().newCall(requestBuilder.build()).execute()) {
            RestRequestResult result = new RestRequestResult(this, response);
            logger.debug("Sent {} request to {} and received status code {} with{} body{}",
                    method.name(), endpoint.getFullUrl(urlParameters), response.code(),
                    result.getBody().map(b -> "").orElse(" empty"),
                    result.getStringBody().map(s -> " " + s).orElse(""));
            if (response.code() >= 300 || response.code() < 200) {
                if (!result.getJsonBody().isNull() && result.getJsonBody().has("code")) {
                    int code = result.getJsonBody().get("code").asInt();
                    String message = result.getJsonBody().has("message") ?
                            result.getJsonBody().get("message").asText() : null;
                    Optional<? extends DiscordException> discordException = RestRequestResultErrorCode.fromCode(code)
                            .flatMap(restRequestResultCode -> restRequestResultCode.getDiscordException(
                                    origin, (message == null) ? restRequestResultCode.getMeaning() : message,
                                    this, result));
                    if (discordException.isPresent()) {
                        throw discordException.get();
                    }
                }

                switch (response.code()) {
                    case 429:
                        // A 429 will be handled in the RatelimitManager class
                        return result;

                    default:
                        Optional<? extends DiscordException> discordException =
                                RestRequestHttpResponseCode.fromCode(response.code())
                                        .flatMap(restRequestHttpResponseCode ->
                                                         restRequestHttpResponseCode.getDiscordException(
                                                                 origin, "Received a " + response.code()
                                                                         + " response from Discord with"
                                                                         + (result.getBody().isPresent()
                                                                            ? ""
                                                                            : " empty")
                                                                         + " body"
                                                                         + result.getStringBody()
                                                                                 .map(s -> " " + s)
                                                                                 .orElse("")
                                                                         + "!", this, result));
                        if (discordException.isPresent()) {
                            throw discordException.get();
                        } else {
                            throw new DiscordException(
                                    origin, "Received a " + response.code() + " response from Discord with"
                                            + (result.getBody().isPresent() ? "" : " empty") + " body"
                                            + result.getStringBody().map(s -> " " + s).orElse("") + "!", this, result);
                        }
                }
            }
            return result;
        }
    }

}

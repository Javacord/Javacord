package de.btobastian.javacord.utils.rest;

import com.mashape.unirest.http.HttpMethod;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.BaseRequest;
import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.mashape.unirest.request.body.MultipartBody;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.exceptions.CannotMessageUserException;
import de.btobastian.javacord.exceptions.DiscordException;
import de.btobastian.javacord.exceptions.MissingPermissionsException;
import de.btobastian.javacord.utils.logging.LoggerUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.io.File;
import java.io.InputStream;
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
    private final HttpMethod method;
    private final RestEndpoint endpoint;

    private boolean includeAuthorizationHeader = true;
    private int ratelimitRetries = 5;
    private String[] urlParameters = new String[0];
    private List<String[]> queryParameters = new ArrayList<>();
    private String body = null;

    private int retryCounter = 0;

    private final CompletableFuture<HttpResponse<JsonNode>> result = new CompletableFuture<>();

    /**
     * This consumer is called to add a file to the request.
     */
    private Function<HttpRequestWithBody, MultipartBody> fileSupplier = request -> null;

    /**
     * This consumer is called to add fields to the request.
     */
    private List<Function<MultipartBody, MultipartBody>> fieldSupplier = new ArrayList<>();

    /**
     * The custom major parameter if it's not included in the url (e.g. for reactions)
     */
    private String customMajorParam = null;

    /**
     * Creates a new instance of this class.
     *
     * @param api The api which will be used to execute the request.
     * @param method The http method of the request.
     * @param endpoint The endpoint to which the request should be sent.
     */
    public RestRequest(DiscordApi api, HttpMethod method, RestEndpoint endpoint) {
        this.api = api;
        this.method = method;
        this.endpoint = endpoint;
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
    public HttpMethod getMethod() {
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
     * Adds a field to the request.
     *
     * @param name The name of the field.
     * @param file The file.
     * @return The current instance in order to chain call methods.
     */
    public RestRequest<T> addField(String name, File file) {
        fileSupplier = request -> request.field(name, file);
        return this;
    }

    /**
     * Adds a field to the request.
     *
     * @param name The name of the field.
     * @param stream The stream of the data.
     * @param fileName The name of the file.
     * @return The current instance in order to chain call methods.
     */
    public RestRequest<T> addField(String name, InputStream stream, String fileName) {
        fileSupplier = request -> request.field(name, stream, fileName);
        return this;
    }

    /**
     * Adds a field to the request.
     * This will only work, if a file was added, too!
     *
     * @param name The name of the field.
     * @param value The value of the field.
     * @return The current instance in order to chain call methods.
     */
    public RestRequest<T> addField(String name, String value) {
        fieldSupplier.add(request -> request.field(name, value));
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
    public RestRequest<T> setBody(JSONObject body) {
        return setBody(body.toString());
    }

    /**
     * Sets the body of the request.
     *
     * @param body The body of the request.
     * @return The current instance in order to chain call methods.
     */
    public RestRequest<T> setBody(JSONArray body) {
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
    public CompletableFuture<T> execute(Function<HttpResponse<JsonNode>, T> function) {
        api.getRatelimitManager().queueRequest(this);
        CompletableFuture<T> future = new CompletableFuture<>();
        result.whenComplete((response, throwable) -> {
            if (throwable != null) {
                future.completeExceptionally(throwable);
                return;
            }
            try {
                future.complete(function.apply(response));
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
    public CompletableFuture<HttpResponse<JsonNode>> getResult() {
        return result;
    }

    /**
     * Executes the request blocking.
     *
     * @return The response of the request.
     * @throws Exception If something went wrong while executing the request.
     */
    public HttpResponse<JsonNode> executeBlocking() throws Exception {
        BaseRequest request;
        switch (method) {
            case GET:
                request = Unirest.get(endpoint.getFullUrl(urlParameters));
                break;
            case POST:
                request = Unirest.post(endpoint.getFullUrl(urlParameters));
                break;
            case PUT:
                request = Unirest.put(endpoint.getFullUrl(urlParameters));
                break;
            case DELETE:
                request = Unirest.delete(endpoint.getFullUrl(urlParameters));
                break;
            case PATCH:
                request = Unirest.patch(endpoint.getFullUrl(urlParameters));
                break;
            case HEAD:
                request = Unirest.head(endpoint.getFullUrl(urlParameters));
                break;
            case OPTIONS:
                request = Unirest.options(endpoint.getFullUrl(urlParameters));
                break;
            default:
                throw new IllegalArgumentException("Unsupported http method!");
        }
        for (String[] queryParameter : queryParameters) {
            ((HttpRequest) request).queryString(queryParameter[0], queryParameter[1]);
        }
        if (includeAuthorizationHeader) {
            ((HttpRequest) request).header("authorization", api.getToken());
        }
        if (request instanceof HttpRequestWithBody) {
            if (fileSupplier.apply(Unirest.post("")) != null) {
                MultipartBody multipartBody = fileSupplier.apply((HttpRequestWithBody) request);
                for (Function<MultipartBody, MultipartBody> func : fieldSupplier) {
                    multipartBody = func.apply(multipartBody);
                }
                request = multipartBody;
            } else if (body != null) {
                ((HttpRequestWithBody) request).body(body);
                ((HttpRequest) request).header("content-type", "application/json");
            }
        }
        logger.debug("Trying to send {} request to {}{}",
                method.name(), endpoint.getFullUrl(urlParameters), body != null ? " with body " + body : "");
        HttpResponse<JsonNode> response = request.asJson();
        logger.debug("Sent {} request to {} and received status code {} with{} body{}",
                method.name(), endpoint.getFullUrl(urlParameters), response.getStatus(),
                response.getBody() == null ? " empty" : "",
                response.getBody() == null ? "" : " " + response.getBody().toString());
        if (response.getStatus() >= 300 || response.getStatus() < 200) {
            if (!response.getBody().isArray() && response.getBody().getObject().has("code")) {
                int code = response.getBody().getObject().getInt("code");
                String message = response.getBody().getObject().has("message")
                        ? null : response.getBody().getObject().getString("message");
                switch (code) {
                    case 50007:
                        throw new CannotMessageUserException(
                                message == null ? "Cannot send message to this user" : message, response, this);
                }
            }
            switch (response.getStatus()) {
                case 429:
                    // A 429 will be handled in the RatelimitManager class
                    return response;
                case 403:
                    throw new MissingPermissionsException(
                            "Received a " + response.getStatus() + " response from Discord with body "
                                    + response.getBody().toString() + "!", response, this);
                default:
                    throw new DiscordException(
                            "Received a " + response.getStatus() + " response from Discord with body "
                                    + response.getBody().toString() + "!", response, this);
            }
        }
        return response;
    }

}

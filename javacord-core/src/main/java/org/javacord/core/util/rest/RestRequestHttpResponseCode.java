package org.javacord.core.util.rest;

import org.javacord.api.exception.BadRequestException;
import org.javacord.api.exception.DiscordException;
import org.javacord.api.exception.MissingPermissionsException;
import org.javacord.api.exception.NotFoundException;
import org.javacord.api.util.rest.RestRequestInformation;
import org.javacord.api.util.rest.RestRequestResponseInformation;
import org.javacord.core.util.exception.DiscordExceptionInstantiator;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * An enum with all rest request result codes as defined by
 * <a href="https://discord.com/developers/docs/topics/response-codes#json-error-response">Discord</a>.
 */
public enum RestRequestHttpResponseCode {

    /**
     * The request completed successfully.
     */
    OK(200, "The request completed successfully"),

    /**
     * The entity was created successfully.
     */
    CREATED(201, "The entity was created successfully"),

    /**
     * The request completed successfully but returned no content.
     */
    NO_CONTENT(204, "The request completed successfully but returned no content"),

    /**
     * The entity was not modified (no action was taken).
     */
    NOT_MODIFIED(304, "The entity was not modified (no action was taken)"),

    /**
     * The request was improperly formatted, or the server couldn't understand it.
     */
    BAD_REQUEST(400, "The request was improperly formatted, or the server couldn't understand it",
                BadRequestException::new, BadRequestException.class),

    /**
     * The Authorization header was missing or invalid.
     */
    UNAUTHORIZED(401, "The Authorization header was missing or invalid"),

    /**
     * The Authorization token you passed did not have permission to the resource.
     */
    FORBIDDEN(403, "The Authorization token you passed did not have permission to the resource",
              MissingPermissionsException::new, MissingPermissionsException.class),

    /**
     * The resource at the location specified doesn't exist.
     */
    NOT_FOUND(404, "The resource at the location specified doesn't exist",
              NotFoundException::new, NotFoundException.class),

    /**
     * The HTTP method used is not valid for the location specified.
     */
    METHOD_NOT_ALLOWED(405, "The HTTP method used is not valid for the location specified"),

    /**
     * You've made too many requests.
     *
     * @see <a href="https://discord.com/developers/docs/topics/rate-limits#rate-limits">Rate Limits</a>
     */
    TOO_MANY_REQUESTS(429, "You've made too many requests"),

    /**
     * There was not a gateway available to process your request. Wait a bit and retry.
     */
    GATEWAY_UNAVAILABLE(502, "There was not a gateway available to process your request. Wait a bit and retry");

    /**
     * A map for retrieving the enum instances by code.
     */
    private static final Map<Integer, RestRequestHttpResponseCode> instanceByCode;

    /**
     * A map for retrieving the enum instances by exception class.
     */
    private static final Map<Class<? extends DiscordException>, RestRequestHttpResponseCode> instanceByExceptionClass;

    /**
     * The actual numeric result code.
     */
    private final int code;

    /**
     * The textual meaning.
     */
    private final String meaning;

    /**
     * The discord exception instantiator that produces instances to throw for this kind of result code.
     */
    private final DiscordExceptionInstantiator<?> discordExceptionInstantiator;

    /**
     * The discord exception class to throw for this kind of result code.
     */
    private final Class<? extends DiscordException> discordExceptionClass;

    static {
        instanceByCode = Collections.unmodifiableMap(
                Arrays.stream(values())
                        .collect(Collectors.toMap(RestRequestHttpResponseCode::getCode, Function.identity())));

        instanceByExceptionClass = Collections.unmodifiableMap(
                Arrays.stream(values())
                        .filter(restRequestHttpResponseCode ->
                                        restRequestHttpResponseCode.getDiscordExceptionClass().isPresent())
                        .collect(Collectors.toMap(restRequestHttpResponseCode ->
                                                          restRequestHttpResponseCode.getDiscordExceptionClass()
                                                                  .orElse(null),
                                                  Function.identity())));
    }

    /**
     * Creates a new rest request http response code.
     *
     * @param code The actual numeric response code.
     * @param meaning The textual meaning.
     */
    RestRequestHttpResponseCode(int code, String meaning) {
        this(code, meaning, null, null);
    }

    /**
     * Creates a new rest request http response code.
     *
     * @param code The actual numeric response code.
     * @param meaning The textual meaning.
     * @param discordExceptionInstantiator The discord exception instantiator that produces
     *                                     instances to throw for this kind of result code.
     */
    <T extends DiscordException> RestRequestHttpResponseCode(
            int code, String meaning,
            DiscordExceptionInstantiator<T> discordExceptionInstantiator, Class<T> discordExceptionClass) {
        this.code = code;
        this.meaning = meaning;
        this.discordExceptionInstantiator = discordExceptionInstantiator;
        this.discordExceptionClass = discordExceptionClass;

        if ((discordExceptionInstantiator == null) && (discordExceptionClass != null)
                || (discordExceptionInstantiator != null) && (discordExceptionClass == null)) {

            throw new IllegalArgumentException("discordExceptionInstantiator and discordExceptionClass do not match");
        }
    }

    /**
     * Gets the rest request http response code by actual numeric response code.
     *
     * @param code The actual numeric response code.
     * @return The rest request http response code with the actual numeric response code.
     */
    public static Optional<RestRequestHttpResponseCode> fromCode(int code) {
        return Optional.ofNullable(instanceByCode.get(code));
    }

    /**
     * Gets the rest request http response code by discord exception class.
     * If no entry for the given class is found, the parents are checked until match is found or
     * {@code DiscordException} is reached.
     *
     * @param discordExceptionClass The discord exception class.
     * @return The rest request http response code with the discord exception class.
     */
    @SuppressWarnings("unchecked")
    public static Optional<RestRequestHttpResponseCode> fromDiscordExceptionClass(
            Class<? extends DiscordException> discordExceptionClass) {
        Class<? extends DiscordException> clazz = discordExceptionClass;
        while (instanceByExceptionClass.get(clazz) == null) {
            if (clazz == DiscordException.class) {
                return Optional.empty();
            }
            clazz = (Class<? extends DiscordException>) clazz.getSuperclass();
        }
        return Optional.of(instanceByExceptionClass.get(clazz));
    }

    /**
     * Gets the actual numeric response code.
     *
     * @return The actual numeric response code.
     */
    public int getCode() {
        return code;
    }

    /**
     * Gets the textual meaning.
     *
     * @return The textual meaning.
     */
    public String getMeaning() {
        return meaning;
    }

    /**
     * Gets the discord exception to throw for this kind of result code.
     *
     * @param origin The origin of the exception.
     * @param message The message of the exception.
     * @param request The information about the request.
     * @param response The information about the response.
     * @return The discord exception to throw for this kind of result code.
     */
    public Optional<? extends DiscordException> getDiscordException(Exception origin, String message,
                                                                    RestRequestInformation request,
                                                                    RestRequestResponseInformation response) {
        return Optional.ofNullable(discordExceptionInstantiator)
                .map(instantiator -> instantiator.createInstance(origin, message, request, response));
    }

    /**
     * Gets the discord exception class to throw for this kind of result code.
     *
     * @return The discord exception class to throw for this kind of result code.
     */
    public Optional<? extends Class<? extends DiscordException>> getDiscordExceptionClass() {
        return Optional.ofNullable(discordExceptionClass);
    }

}

package org.javacord.exception;

import org.javacord.util.rest.RestRequest;
import org.javacord.util.rest.RestRequestResult;

/**
 * Represents a function that accepts four arguments ({@code Exception}, {@code String}, {@code RestRequest<?>} and
 * {@code RestRequestResult}) and produces a discord exception of type {@code T}.
 *
 * @param <T> The type of the discord exception that is produced.
 */
@FunctionalInterface
public interface DiscordExceptionInstantiator<T extends DiscordException> {

    /**
     * Creates a new instance of the class {@code T}.
     *
     * @param origin The origin of the exception.
     * @param message The message of the exception.
     * @param request The request.
     * @param restRequestResult The rest request result which caused the exception.
     * @return The new instance.
     */
    T createInstance(Exception origin, String message, RestRequest<?> request, RestRequestResult restRequestResult);

}

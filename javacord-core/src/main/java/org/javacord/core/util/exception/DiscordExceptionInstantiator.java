package org.javacord.core.util.exception;

import org.javacord.api.exception.DiscordException;
import org.javacord.api.util.rest.RestRequestInformation;
import org.javacord.api.util.rest.RestRequestResponseInformation;

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
     * @param request The information about the request.
     * @param response The information about the response.
     * @return The new instance.
     */
    T createInstance(Exception origin, String message, RestRequestInformation request,
                     RestRequestResponseInformation response);

}

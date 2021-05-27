package org.javacord.api.interaction;

import java.util.Optional;

public interface InteractionResponse {

    /**
     * Gets the type of the response.
     *
     * @return The type.
     */
    InteractionResponseType getType();

    /**
     * Gets the response messages.
     *
     * @return The response message.
     */
    Optional<InteractionApplicationCommandCallbackData> getData();
}

package org.javacord.api.interaction;

import org.javacord.api.entity.message.embed.Embed;
import org.javacord.api.entity.message.mention.AllowedMentions;

import java.util.List;
import java.util.Optional;

public interface InteractionApplicationCommandCallbackData {

    /**
     * Checks whether or not the response is text to speech.
     *
     * @return Whether or not the response is text to speech.
     */
    Optional<Boolean> isTts();

    /**
     * Gets the message content.
     *
     * @return The message content.
     */
    String getContent();

    /**
     * Gets a list with all embeds.
     *
     * @return A list with all embeds.
     */
    List<Embed> getEmbeds();

    /**
     * Gets a list with all allowed mentions.
     *
     * @return A list with all allowed mentions.
     */
    List<AllowedMentions> getAllowedMentions();

}

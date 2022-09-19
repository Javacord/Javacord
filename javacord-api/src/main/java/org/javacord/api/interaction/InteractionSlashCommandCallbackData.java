package org.javacord.api.interaction;

import org.javacord.api.entity.message.embed.Embed;
import org.javacord.api.entity.message.mention.AllowedMentions;

import java.util.List;
import java.util.Optional;

public interface InteractionSlashCommandCallbackData {

    /**
     * Checks whether the response is text to speech.
     *
     * @return Whether the response is text to speech.
     */
    Optional<Boolean> isTts();

    /**
     * Gets the message content.
     *
     * @return The message content.
     */
    String getContent();

    /**
     * Gets all embeds.
     *
     * @return All embeds.
     */
    List<Embed> getEmbeds();

    /**
     * Gets all allowed mentions.
     *
     * @return All allowed mentions.
     */
    List<AllowedMentions> getAllowedMentions();

}

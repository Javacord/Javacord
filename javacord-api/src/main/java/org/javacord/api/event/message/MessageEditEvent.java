package org.javacord.api.event.message;

import org.javacord.api.entity.message.embed.Embed;

import java.util.List;
import java.util.Optional;

/**
 * A message delete event.
 */
public interface MessageEditEvent extends RequestableMessageEvent {

    /**
     * Gets the new content of the message.
     *
     * @return The new content of the message.
     */
    String getNewContent();

    /**
     * Gets the old content of the message. It will only be present, if the message is in the cache.
     *
     * @return The old content of the message.
     */
    Optional<String> getOldContent();

    /**
     * Gets the new embeds of the message.
     *
     * @return The new embeds of the message.
     */
    List<Embed> getNewEmbeds();

    /**
     * Gets the old embeds of the message. It will only be present, if the message is in the cache.
     *
     * @return The old embeds of the message.
     */
    Optional<List<Embed>> getOldEmbeds();

}

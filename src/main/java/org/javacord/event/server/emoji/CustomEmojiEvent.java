package org.javacord.event.server.emoji;

import org.javacord.entity.emoji.KnownCustomEmoji;
import org.javacord.event.server.ServerEvent;

/**
 * A custom emoji event.
 */
public interface CustomEmojiEvent extends ServerEvent {

    /**
     * Gets the custom emoji of the event.
     *
     * @return The custom emoji of the event.
     */
    KnownCustomEmoji getEmoji();

}

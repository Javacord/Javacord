package org.javacord.api.event.server.emoji;

import org.javacord.api.entity.emoji.KnownCustomEmoji;
import org.javacord.api.event.server.ServerEvent;

/**
 * A custom emoji event.
 */
public interface KnownCustomEmojiEvent extends ServerEvent {

    /**
     * Gets the custom emoji of the event.
     *
     * @return The custom emoji of the event.
     */
    KnownCustomEmoji getEmoji();

}

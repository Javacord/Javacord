package org.javacord.event.server.emoji;

import org.javacord.entity.emoji.KnownCustomEmoji;

/**
 * A custom emoji delete event.
 */
public class CustomEmojiDeleteEvent extends CustomEmojiEvent {

    /**
     * Creates a new custom emoji delete event.
     *
     * @param emoji The deleted emoji.
     */
    public CustomEmojiDeleteEvent(KnownCustomEmoji emoji) {
        super(emoji);
    }

}

package org.javacord.core.event.server.emoji;

import org.javacord.api.entity.emoji.KnownCustomEmoji;
import org.javacord.api.event.server.emoji.CustomEmojiDeleteEvent;

/**
 * The implementation of {@link CustomEmojiDeleteEvent}.
 */
public class CustomEmojiDeleteEventImpl extends CustomEmojiEventImpl implements CustomEmojiDeleteEvent {

    /**
     * Creates a new custom emoji delete event.
     *
     * @param emoji The deleted emoji.
     */
    public CustomEmojiDeleteEventImpl(KnownCustomEmoji emoji) {
        super(emoji);
    }

}

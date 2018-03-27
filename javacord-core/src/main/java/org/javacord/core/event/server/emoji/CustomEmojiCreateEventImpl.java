package org.javacord.core.event.server.emoji;

import org.javacord.api.entity.emoji.KnownCustomEmoji;
import org.javacord.api.event.server.emoji.CustomEmojiCreateEvent;

/**
 * The implementation of {@link CustomEmojiCreateEvent}.
 */
public class CustomEmojiCreateEventImpl extends CustomEmojiEventImpl implements CustomEmojiCreateEvent {

    /**
     * Creates a new custom emoji create event.
     *
     * @param emoji The created emoji.
     */
    public CustomEmojiCreateEventImpl(KnownCustomEmoji emoji) {
        super(emoji);
    }

}

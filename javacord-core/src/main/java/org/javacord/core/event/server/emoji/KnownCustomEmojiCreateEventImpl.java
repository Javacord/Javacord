package org.javacord.core.event.server.emoji;

import org.javacord.api.entity.emoji.KnownCustomEmoji;
import org.javacord.api.event.server.emoji.KnownCustomEmojiCreateEvent;

/**
 * The implementation of {@link KnownCustomEmojiCreateEvent}.
 */
public class KnownCustomEmojiCreateEventImpl extends KnownCustomEmojiEventImpl implements KnownCustomEmojiCreateEvent {

    /**
     * Creates a new custom emoji create event.
     *
     * @param emoji The created emoji.
     */
    public KnownCustomEmojiCreateEventImpl(KnownCustomEmoji emoji) {
        super(emoji);
    }

}

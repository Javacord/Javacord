package org.javacord.core.event.server.emoji;

import org.javacord.api.entity.emoji.KnownCustomEmoji;
import org.javacord.api.event.server.emoji.KnownCustomEmojiDeleteEvent;

/**
 * The implementation of {@link KnownCustomEmojiDeleteEvent}.
 */
public class KnownCustomEmojiDeleteEventImpl extends KnownCustomEmojiEventImpl implements KnownCustomEmojiDeleteEvent {

    /**
     * Creates a new custom emoji delete event.
     *
     * @param emoji The deleted emoji.
     */
    public KnownCustomEmojiDeleteEventImpl(KnownCustomEmoji emoji) {
        super(emoji);
    }

}

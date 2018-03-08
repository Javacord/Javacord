package org.javacord.event.server.emoji.impl;

import org.javacord.entity.emoji.KnownCustomEmoji;
import org.javacord.event.server.emoji.CustomEmojiDeleteEvent;

/**
 * The implementation of {@link CustomEmojiDeleteEvent}.
 */
public class ImplCustomEmojiDeleteEvent extends ImplCustomEmojiEvent implements CustomEmojiDeleteEvent {

    /**
     * Creates a new custom emoji delete event.
     *
     * @param emoji The deleted emoji.
     */
    public ImplCustomEmojiDeleteEvent(KnownCustomEmoji emoji) {
        super(emoji);
    }

}

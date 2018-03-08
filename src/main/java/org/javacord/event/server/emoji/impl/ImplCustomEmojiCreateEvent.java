package org.javacord.event.server.emoji.impl;

import org.javacord.entity.emoji.KnownCustomEmoji;
import org.javacord.event.server.emoji.CustomEmojiCreateEvent;

/**
 * The implementation of {@link CustomEmojiCreateEvent}.
 */
public class ImplCustomEmojiCreateEvent extends ImplCustomEmojiEvent implements CustomEmojiCreateEvent {

    /**
     * Creates a new custom emoji create event.
     *
     * @param emoji The created emoji.
     */
    public ImplCustomEmojiCreateEvent(KnownCustomEmoji emoji) {
        super(emoji);
    }

}

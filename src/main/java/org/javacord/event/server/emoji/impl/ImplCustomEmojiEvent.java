package org.javacord.event.server.emoji.impl;

import org.javacord.entity.emoji.KnownCustomEmoji;
import org.javacord.event.server.emoji.CustomEmojiEvent;
import org.javacord.event.server.impl.ImplServerEvent;

/**
 * The implementation of {@link CustomEmojiEvent}.
 */
public abstract class ImplCustomEmojiEvent extends ImplServerEvent implements CustomEmojiEvent {

    /**
     * The custom emoji of the event.
     */
    private final KnownCustomEmoji emoji;

    /**
     * Creates a new custom emoji event.
     *
     * @param emoji The custom emoji of the event.
     */
    public ImplCustomEmojiEvent(KnownCustomEmoji emoji) {
        super(emoji.getServer());
        this.emoji = emoji;
    }

    @Override
    public KnownCustomEmoji getEmoji() {
        return emoji;
    }

}

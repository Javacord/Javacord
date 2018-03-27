package org.javacord.core.event.server.emoji;

import org.javacord.api.entity.emoji.KnownCustomEmoji;
import org.javacord.api.event.server.emoji.CustomEmojiEvent;
import org.javacord.core.event.server.ServerEventImpl;

/**
 * The implementation of {@link CustomEmojiEvent}.
 */
public abstract class CustomEmojiEventImpl extends ServerEventImpl implements CustomEmojiEvent {

    /**
     * The custom emoji of the event.
     */
    private final KnownCustomEmoji emoji;

    /**
     * Creates a new custom emoji event.
     *
     * @param emoji The custom emoji of the event.
     */
    public CustomEmojiEventImpl(KnownCustomEmoji emoji) {
        super(emoji.getServer());
        this.emoji = emoji;
    }

    @Override
    public KnownCustomEmoji getEmoji() {
        return emoji;
    }

}

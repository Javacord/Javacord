package org.javacord.event.server.emoji;

import org.javacord.entity.emoji.KnownCustomEmoji;
import org.javacord.event.server.ServerEvent;

/**
 * A custom emoji event.
 */
public abstract class CustomEmojiEvent extends ServerEvent {

    /**
     * The custom emoji of the event.
     */
    private final KnownCustomEmoji emoji;

    /**
     * Creates a new custom emoji event.
     *
     * @param emoji The custom emoji of the event.
     */
    public CustomEmojiEvent(KnownCustomEmoji emoji) {
        super(emoji.getApi(), emoji.getServer());
        this.emoji = emoji;
    }

    /**
     * Gets the custom emoji of the event.
     *
     * @return The custom emoji of the event.
     */
    public KnownCustomEmoji getEmoji() {
        return emoji;
    }

}
